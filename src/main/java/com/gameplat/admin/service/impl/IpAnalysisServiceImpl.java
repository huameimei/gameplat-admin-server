package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.IpAnalysisEnum;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.MemberWithdrawHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.service.IpAnalysisService;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.security.context.UserCredential;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lily
 * @description ip分析
 * @date 2022/1/19
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class IpAnalysisServiceImpl implements IpAnalysisService {

  @Autowired private OnlineUserService onlineUserService;

  @Autowired private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

  @Autowired private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

  @Autowired private MemberMapper memberMapper;

  @Autowired private IBaseElasticsearchService baseElasticsearchService;

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Autowired private RestHighLevelClient restHighLevelClient;

  private static final String TOKEN_PREFIX = "token:web:";

  @Value("${syslog.tenant}")
  private String tenant;

  private static final String INDEX = "login-log_";

  /** ip分析 */
  @Override
  public IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto) {
    IPage<IpAnalysisVO> pagelist = new PageDTO<>();
    List<IpAnalysisVO> list = new ArrayList<>();
    if (Objects.equals(dto.getType(), null)) {
      throw new ServiceException("分析类型不能为空");
    }
    if (IpAnalysisEnum.REGISTER.getCode().equals(dto.getType())) {
      // 注册
      pagelist = memberMapper.page(page, dto);
      list = pagelist.getRecords();
    } else if (IpAnalysisEnum.LOGIN.getCode().equals(dto.getType())) {
      // 登录
      list = aggregationSearchDoc(page, dto);
      pagelist.setRecords(list);
      pagelist.setTotal(list.size());
      if (ObjectUtils.isNull(page.getSize())) {
        pagelist.setSize(10);
      } else {
        pagelist.setSize(page.getSize());
      }
      if (ObjectUtils.isNull(page.getCurrent())) {
        pagelist.setCurrent(1);
      } else {
        pagelist.setCurrent(page.getCurrent());
      }
      pagelist.setPages(
          page.getTotal() % page.getSize() == 0
              ? page.getTotal() / page.getSize()
              : page.getTotal() / page.getSize() + 1);
    } else if (IpAnalysisEnum.RECHARGE.getCode().equals(dto.getType())) {
      // 充值
      pagelist = rechargeOrderHistoryMapper.page(page, dto);
      list = pagelist.getRecords();
    } else if (IpAnalysisEnum.WITHDRAW.getCode().equals(dto.getType())) {
      pagelist = memberWithdrawHistoryMapper.page(page, dto);
      list = pagelist.getRecords();
    }
    // 判断会员是否在线

    if (CollectionUtils.isNotEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        IpAnalysisVO ipAnalysis = list.get(i);
        if (redisTemplate.hasKey(TOKEN_PREFIX + ipAnalysis.getAccount())) {
          UserCredential userCredential =
              (UserCredential)
                  redisTemplate.opsForValue().get(TOKEN_PREFIX + ipAnalysis.getAccount());
          ipAnalysis.setUuid(userCredential.getUuid());
          ipAnalysis.setOffline(1);
          pagelist.getRecords().set(i, ipAnalysis);
        } else {
          ipAnalysis.setOffline(0);
          pagelist.getRecords().set(i, ipAnalysis);
        }
      }
    }
    if (ObjectUtils.isNotEmpty(dto.getOnline())) {
      list =
          pagelist.getRecords().stream()
              .filter(x -> x.getOffline().equals(dto.getOnline()))
              .collect(Collectors.toList());
    }
    Integer total = list.size();
    Long size = page.getSize();
    Long pages = total % size == 0 ? total / size : total / size + 1;
    pagelist.setCurrent(page.getCurrent());
    pagelist.setSize(size);
    pagelist.setTotal(total);
    pagelist.setPages(pages);
    pagelist.setRecords(list);
    return pagelist;
  }

  public static QueryBuilder buildSearch(IpAnalysisDTO dto) {
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("username.keyword", dto.getAccount()));
    }
    if (ObjectUtils.isNotNull(dto.getLoginIp())) {
      builder.must(QueryBuilders.matchQuery("ipAddress", dto.getLoginIp()));
    }
    if (ObjectUtils.isNotNull(dto.getBeginTime()) && ObjectUtils.isNotNull(dto.getEndTime())) {
      builder.must(
          QueryBuilders.rangeQuery("createTime").from(dto.getBeginTime()).to(dto.getEndTime()));
    }
    return builder;
  }

  // es聚合查询
  public List<IpAnalysisVO> aggregationSearchDoc(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto) {
    SearchSourceBuilder searchSourceBuilder =
        new SearchSourceBuilder().query(IpAnalysisServiceImpl.buildSearch(dto));
    TermsAggregationBuilder username =
        AggregationBuilders.terms("username").field("username.keyword").size((int) page.getSize());
    TermsAggregationBuilder ipAddress =
        AggregationBuilders.terms("ipAddress").field("ipAddress.keyword").size(1);

    BucketSortPipelineAggregationBuilder bucketSort =
        new BucketSortPipelineAggregationBuilder("bucket_sort", null)
            .from(((int) page.getCurrent() - 1) * (int) page.getSize())
            .size((int) page.getSize());

    ipAddress.subAggregation(bucketSort);
    username.subAggregation(ipAddress);

    searchSourceBuilder.aggregation(username);
    SearchRequest searchRequest = new SearchRequest(INDEX + tenant);

    searchSourceBuilder.sort(SortBuilders.fieldSort("createTime.keyword").order(SortOrder.DESC));
    searchRequest.source(searchSourceBuilder);

    RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
    optionsBuilder.setHttpAsyncResponseConsumerFactory(
        new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
    List<IpAnalysisVO> list = new ArrayList<>();
    Set<String> ips = new HashSet<>();
    try {
      SearchResponse search = restHighLevelClient.search(searchRequest, optionsBuilder.build());
      Terms terms = search.getAggregations().get("username");
      for (Terms.Bucket bucket : terms.getBuckets()) {
        String account = bucket.getKeyAsString();
        Terms terms2 = bucket.getAggregations().get("ipAddress");
        for (Terms.Bucket bucket2 : terms2.getBuckets()) {
          String ip = bucket2.getKeyAsString();
          long docCount = bucket2.getDocCount();
          ips.add(ip);
          IpAnalysisVO vo = new IpAnalysisVO();
          vo.setAccount(account);
          vo.setIpAddress(ip);
          vo.setIpCount((int) docCount);
          list.add(vo);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    Map<String, Integer> ipMap = aggregationSearchDoc(ips);
    list.forEach(l -> l.setIpCount(ipMap.get(l.getIpAddress())));
    return list;
  }

  public Map<String, Integer> aggregationSearchDoc(Set<String> ips) {
    Map<String, Integer> map = new HashMap<>();
    if (ips == null || ips.isEmpty()) {
      return map;
    }
    SearchSourceBuilder searchSourceBuilder =
        new SearchSourceBuilder().query(QueryBuilders.termsQuery("ipAddress", ips));
    TermsAggregationBuilder ipCount =
        AggregationBuilders.terms("ipCount").field("ipAddress.keyword");
    searchSourceBuilder.aggregation(ipCount);
    SearchRequest searchRequest = new SearchRequest(INDEX + tenant);
    searchSourceBuilder.size(0);
    searchRequest.source(searchSourceBuilder);

    RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
    optionsBuilder.setHttpAsyncResponseConsumerFactory(
        new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));
    try {
      SearchResponse search = restHighLevelClient.search(searchRequest, optionsBuilder.build());
      Terms terms = search.getAggregations().get("ipCount");
      for (Terms.Bucket bucket : terms.getBuckets()) {
        String ip = bucket.getKeyAsString();
        long docCount = bucket.getDocCount();
        map.put(ip, (int) docCount);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }
}
