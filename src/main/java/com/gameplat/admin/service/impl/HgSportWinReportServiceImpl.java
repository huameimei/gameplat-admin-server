package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.model.dto.HgSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;
import com.gameplat.admin.service.HgSportWinReportService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.util.Converts;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
@Service
public class HgSportWinReportServiceImpl implements HgSportWinReportService {

  @Autowired
  private TenantConfig tenantConfig;

  @Resource
  private RestHighLevelClient restHighLevelClient;

  @Override
  public List<HgSportWinReportVO> findList(HgSportWinReportQueryDTO queryDTO) {
    return findHgReportData(queryDTO);
  }

  @Override
  public IPage<HgSportWinReportVO> findListByMemberNumber(Page<HgSportWinReportVO> page, HgSportWinReportQueryDTO queryDTO) {
    return null;
  }

  @Override
  public void exportReport(HgSportWinReportQueryDTO queryDTO) {

  }

  public List<HgSportWinReportVO> findHgReportData(HgSportWinReportQueryDTO dto) {
    String tenant = tenantConfig.getTenantCode();
    long startTimestamp = DateUtils.parseDate(dto.getStartTime(), "yyyy-MM-dd HH:mm:ss").getTime();
    long endTimestamp = DateUtils.parseDate(dto.getEndTime(), "yyyy-MM-dd HH:mm:ss").getTime();

    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    builder.must(QueryBuilders.matchQuery("gameKind", GameKindEnum.HG_SPORT.code()));
    builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));
    builder.must(QueryBuilders.rangeQuery("statTime.keyword").gte(startTimestamp).lte(endTimestamp));
    if (StringUtils.isNotEmpty(dto.getMemberAccount())) {
      builder.must(QueryBuilders.termQuery("account.keyword", dto.getMemberAccount()));
    }
    if (StringUtils.isNotEmpty(dto.getProxyAccount())) {
      BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
      if (StringUtils.isNotNull(dto.getIsDirectly()) && dto.getIsDirectly() == 0) {
        builder.must(QueryBuilders.wildcardQuery("superPath", "*" + dto.getProxyAccount() + "*"));
      } else {
        builder.must(QueryBuilders.termQuery("parentName", dto.getProxyAccount()));
      }
      builder.should(builder2);
      builder.should(QueryBuilders.termQuery("account.keyword", dto.getProxyAccount()));
    }

    if (StringUtils.isNotEmpty(dto.getSportType())) {
      builder.must(QueryBuilders.termQuery("sportType.keyword", dto.getSportType()));
    }

    SearchRequest searchRequest = new SearchRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenant);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    TermsAggregationBuilder groupTerms =
            AggregationBuilders.terms("gameCodeGroup").field("gameCode.keyword");

    SumAggregationBuilder betAmountSumBuilder = AggregationBuilders.sum("betAmount").field("betAmount");
    SumAggregationBuilder validAmountSumBuilder = AggregationBuilders.sum("validAmount").field("validAmount");
    SumAggregationBuilder winAmountSumBuilder = AggregationBuilders.sum("winAmount").field("winAmount");
    ValueCountAggregationBuilder betCountCountBuilder = AggregationBuilders.count("betCount").field("billNo.keyword");

//    CollapseBuilder collapseBuilder = new CollapseBuilder("account");
//    CardinalityAggregationBuilder agg = AggregationBuilders.cardinality("userCount").field("account.keyword");

    groupTerms.subAggregation(betAmountSumBuilder);
    groupTerms.subAggregation(validAmountSumBuilder);
    groupTerms.subAggregation(winAmountSumBuilder);
    groupTerms.subAggregation(betCountCountBuilder);

//    searchSourceBuilder.collapse(collapseBuilder);
    searchSourceBuilder.size(0);
    searchSourceBuilder.query(builder);
    searchSourceBuilder.aggregation(groupTerms);
//    searchSourceBuilder.aggregation(agg);
    searchRequest.source(searchSourceBuilder);

    RequestOptions.Builder optionsBuilder = RequestOptions.DEFAULT.toBuilder();
    optionsBuilder.setHttpAsyncResponseConsumerFactory(
            new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(31457280));

    SearchResponse searchResponse = null;
    try {
      searchResponse = restHighLevelClient.search(searchRequest, optionsBuilder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }

    Terms terms = searchResponse.getAggregations().get("gameCodeGroup");
    if (StringUtils.isNull(terms)) {
      return null;
    }
    List<HgSportWinReportVO> hgSportWinReportVOList = new ArrayList<>();
    for (Terms.Bucket bucket : terms.getBuckets()) {
      Object gameCode = bucket.getKey();
      BigDecimal betAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("betAmount")).getValue())
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);
      BigDecimal validAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("validAmount")).getValue())
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);
      BigDecimal winAmount = Converts.toBigDecimal(((ParsedSum) bucket.getAggregations().get("winAmount")).getValue()).abs()
              .divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_DOWN);
      HgSportWinReportVO hgSportWinReportVO = new HgSportWinReportVO();
      hgSportWinReportVO.setBetType(gameCode.toString());
      hgSportWinReportVO.setBetAmount(betAmount);
      hgSportWinReportVO.setValidAmount(validAmount);
      hgSportWinReportVO.setGameWinAmount(winAmount.negate());
      hgSportWinReportVO.setAverageBetAmount(betAmount.subtract(winAmount.negate()));
      hgSportWinReportVOList.add(hgSportWinReportVO);
    }

    return hgSportWinReportVOList;
  }

}
