package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.model.dto.HgSportWinReportQueryDTO;
import com.gameplat.admin.model.dto.SbSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;
import com.gameplat.admin.service.SbSportWinReportService;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.util.Converts;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.GameKindEnum;
import com.gameplat.common.enums.SettleStatusEnum;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
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
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
@Slf4j
public class SbSportWinReportServiceImpl implements SbSportWinReportService {

  @Autowired
  private SysTheme sysTheme;

  @Resource
  private RestHighLevelClient restHighLevelClient;

  @Override
  public List<HgSportWinReportVO> findList(SbSportWinReportQueryDTO queryDTO) {
    return null;
  }

  @Override
  public void exportReport(SbSportWinReportQueryDTO queryDTO, HttpServletResponse response) {

  }

  public List<HgSportWinReportVO> findHgReportData(HgSportWinReportQueryDTO dto) {
    String tenant = sysTheme.getTenantCode();
    long startTimestamp = DateUtils.parseDate(dto.getBeginTime(), "yyyy-MM-dd HH:mm:ss").getTime();
    long endTimestamp = DateUtils.parseDate(dto.getEndTime(), "yyyy-MM-dd HH:mm:ss").getTime();

    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    builder.must(QueryBuilders.matchQuery("gameKind", GameKindEnum.HG_SPORT.code()));
    builder.must(QueryBuilders.termQuery("settle", SettleStatusEnum.YES.getValue()));
    builder.must(QueryBuilders.rangeQuery("statTime.keyword").gte(startTimestamp).lte(endTimestamp));
    if (StringUtils.isNotEmpty(dto.getMemberAccount())) {
      builder.must(QueryBuilders.termQuery("account.keyword", dto.getMemberAccount()));
    }
    if (StringUtils.isNotNull(dto.getSportType())) {
      builder.must(QueryBuilders.termQuery("sportType", dto.getSportType()));
    }
    if (StringUtils.isNotEmpty(dto.getProxyAccount())) {
      BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
      if (StringUtils.isNotNull(dto.getIsDirectly()) && dto.getIsDirectly() == 0) {
        builder2.should(QueryBuilders.wildcardQuery("superPath", "*" + dto.getProxyAccount() + "*"));
      } else {
        // 默认只查询直属下级
        builder2.should(QueryBuilders.termQuery("parentName", dto.getProxyAccount()));
      }
      builder2.should(QueryBuilders.termQuery("account", dto.getProxyAccount()));
      builder.must(builder2);
    }

    SearchRequest searchRequest = new SearchRequest(ContextConstant.ES_INDEX.BET_RECORD_ + tenant);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    TermsAggregationBuilder groupTerms =
            AggregationBuilders.terms("gameCodeGroup").field("gameCode.keyword");

    SumAggregationBuilder betAmountSumBuilder = AggregationBuilders.sum("betAmount").field("betAmount");
    SumAggregationBuilder validAmountSumBuilder = AggregationBuilders.sum("validAmount").field("validAmount");
    SumAggregationBuilder winAmountSumBuilder = AggregationBuilders.sum("winAmount").field("winAmount");
    ValueCountAggregationBuilder betCountCountBuilder = AggregationBuilders.count("betCount").field("billNo.keyword");
    TopHitsAggregationBuilder gameNameCountBuilder = AggregationBuilders.topHits("gameName").size(1).fetchSource("gameName", Strings.EMPTY);
    CardinalityAggregationBuilder userCountCountBuilder = AggregationBuilders.cardinality("userCount").field("account.keyword");

    groupTerms.subAggregation(betAmountSumBuilder);
    groupTerms.subAggregation(validAmountSumBuilder);
    groupTerms.subAggregation(winAmountSumBuilder);
    groupTerms.subAggregation(betCountCountBuilder);
    groupTerms.subAggregation(gameNameCountBuilder);
    groupTerms.subAggregation(userCountCountBuilder);
    searchSourceBuilder.size(0);
    searchSourceBuilder.query(builder);
    searchSourceBuilder.aggregation(groupTerms);
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
      hgSportWinReportVO.setBetTypeName(JSONObject.parseObject(((ParsedTopHits) bucket.getAggregations().get("gameName")).getHits().getHits()[0].getSourceRef().utf8ToString()).getString("gameName"));
      hgSportWinReportVO.setBetCount(((ParsedValueCount) bucket.getAggregations().get("betCount")).getValue());
      hgSportWinReportVO.setUserCount(((ParsedCardinality) bucket.getAggregations().get("userCount")).getValue());
      hgSportWinReportVO.setBetAmount(betAmount);
      hgSportWinReportVO.setValidAmount(validAmount);
      hgSportWinReportVO.setGameWinAmount(winAmount.negate());
      hgSportWinReportVO.setSendPrizeAmount(betAmount.subtract(winAmount.negate()));
      hgSportWinReportVO.setAverageBetAmount(betAmount.divide(new BigDecimal(hgSportWinReportVO.getUserCount()), 2, BigDecimal.ROUND_DOWN));
      hgSportWinReportVO.setMemberAccount(dto.getMemberAccount());
      hgSportWinReportVO.setProxyAccount(dto.getProxyAccount());
      hgSportWinReportVO.setIsDirectly(dto.getIsDirectly());
      hgSportWinReportVO.setBeginTime(startTimestamp);
      hgSportWinReportVO.setEndTime(endTimestamp);
      hgSportWinReportVOList.add(hgSportWinReportVO);
    }
    return hgSportWinReportVOList;
  }

}
