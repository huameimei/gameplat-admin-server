package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.IpAnalysisEnum;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.MemberWithdrawHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisEsVO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.service.IpAnalysisService;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.elasticsearch.page.PageResponse;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.security.context.UserCredential;
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lily
 * @description ip分析
 * @date 2022/1/19
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class IpAnalysisServiceImpl implements IpAnalysisService {

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

    @Autowired
    private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private IBaseElasticsearchService baseElasticsearchService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

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
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(IpAnalysisServiceImpl.buildSearch(dto));
//        TermsAggregationBuilder aggregation =
//                AggregationBuilders.terms("username")
//                        .subAggregation(AggregationBuilders.terms("ipAddress").field("ipAddress.keyword"))
//                        .field("username.keyword").size(200);

        TermsAggregationBuilder username = AggregationBuilders.terms("username").field("username.keyword");
        TermsAggregationBuilder ipAddress = AggregationBuilders.terms("ipAddress").field("ipAddress.keyword");

        // 去重
        CardinalityAggregationBuilder distinct_username = AggregationBuilders.cardinality("distinct_username").field("username.keyword");
        searchSourceBuilder.aggregation(username.subAggregation(ipAddress).subAggregation(distinct_username) ).sort(SortBuilders.fieldSort("createTime.keyword").order(SortOrder.DESC));
        PageResponse<JSON> result = baseElasticsearchService.search(INDEX + tenant, searchSourceBuilder, JSON.class, (int) page.getCurrent(), (int) page.getSize());
        List<JSON> log = result.getList();
            for (int i = 0; i < log.size(); i++) {
                IpAnalysisEsVO esVO = JSON.toJavaObject(log.get(i), IpAnalysisEsVO.class);
                IpAnalysisVO vo = new IpAnalysisVO(){{
                    setAccount(esVO.getUsername());
                    setIpAddress(esVO.getIpAddress());
                }};
                list.add(vo);
                SearchSourceBuilder searchSourceBuilder1 =
                        new SearchSourceBuilder().query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("ipAddress", esVO.getIpAddress())))
                        .aggregation(AggregationBuilders.count("ipAddress_count").field("ipAddress.keyword"));
                Map<String, Long> ipAddress1 = baseElasticsearchService.aggregationSearchDoc(INDEX + tenant, searchSourceBuilder1, "ipAddress_count");
                pagelist.setRecords(list);

        }
            pagelist.setTotal(list.size());
            if (ObjectUtils.isNull(page.getSize())){
                pagelist.setSize(10);
            }else{
                pagelist.setSize(page.getSize());
            }
            if (ObjectUtils.isNull(page.getCurrent())){
                pagelist.setCurrent(1);
            }else{
                pagelist.setCurrent(page.getCurrent());
            }
            pagelist.setPages(page.getTotal() % page.getSize() == 0 ? page.getTotal() / page.getSize() : page.getTotal() / page.getSize() + 1);
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
                    UserCredential userCredential = (UserCredential)redisTemplate.opsForValue().get(TOKEN_PREFIX + ipAnalysis.getAccount());
                    ipAnalysis.setUuid(userCredential.getUuid());
                    ipAnalysis.setOffline(1);
                    pagelist.getRecords().set(i, ipAnalysis);
                } else {
                    ipAnalysis.setOffline(0);
                    pagelist.getRecords().set(i, ipAnalysis);
                }
            }
        }
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
            builder.must(QueryBuilders.rangeQuery("createTime").from(dto.getBeginTime()).to(dto.getEndTime()));
        }
        return builder;
    }

    public void searchEs(IpAnalysisDTO dto){
        // 创建search请求
        SearchRequest searchRequest = new SearchRequest(INDEX+tenant);
        // 用SearchSourceBuilder来构造查询请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(IpAnalysisServiceImpl.buildSearch(dto));
        // 去重
        CardinalityAggregationBuilder distinct_count = AggregationBuilders.cardinality("distinct_count")
                .field("username.keyword");
        // 根据 会员账号 ip 分组 而后根据 会员账号去重 而后根据时间排序
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_group")
                .field("username.keyword")
                .field("ipAddress.keyword")
                .subAggregation(distinct_count)
                .order(BucketOrder.aggregation("distinct_count", false));

        sourceBuilder.aggregation(aggregation);
        searchRequest.source(sourceBuilder);
        //3、发送请求
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (RestStatus.OK.equals(searchResponse.status())) {
            // 获取聚合结果
            Aggregations aggregations = searchResponse.getAggregations();
            Terms byAgeAggregation = aggregations.get("by_group");
            for (Terms.Bucket buck : byAgeAggregation.getBuckets()) {
                String ip = buck.getKeyAsString();
                Aggregations aggregations1 = buck.getAggregations();
                Aggregation distinct_count1 = aggregations1.get("distinct_count");
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(distinct_count1));
                System.out.println(jsonObject);
                String cardinalityValue = jsonObject.getString("username");
                System.out.println(ip +"\t\t" + cardinalityValue);
            }
        }
    }

    //聚合查詢
    public Map<String, Long> aggregationSearchDoc(IpAnalysisDTO dto){
        String index = INDEX+tenant;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(IpAnalysisServiceImpl.buildSearch(dto));
//        CardinalityAggregationBuilder distinct_count = AggregationBuilders.cardinality("distinct_count").field("username.keyword");

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("username").subAggregation(AggregationBuilders.terms("ipAddress").field("ipAddress.keyword").size(10))
                .field("username")
                .order(BucketOrder.aggregation("createTime", false));
        searchSourceBuilder.aggregation(aggregation);
        Map<String, Long> by_group = baseElasticsearchService.aggregationSearchDoc(index, searchSourceBuilder, "username");
        System.out.println(by_group);
        return by_group;
    }
}
