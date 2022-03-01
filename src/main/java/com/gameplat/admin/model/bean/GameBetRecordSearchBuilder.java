package com.gameplat.admin.model.bean;

import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.GameVaildBetRecordQueryDTO;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class GameBetRecordSearchBuilder {

  public static QueryBuilder buildBetRecordSearch(GameBetRecordQueryDTO dto) {
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("account", dto.getAccount()));
    }
    if (StringUtils.isNotEmpty(dto.getBillNo())) {
      builder.must(QueryBuilders.matchQuery("billNo", dto.getBillNo()));
    }
    if (StringUtils.isNotEmpty(dto.getPlatformCode())) {
      builder.must(QueryBuilders.matchQuery("platformCode", dto.getPlatformCode()));
    }
    if (StringUtils.isNotEmpty(dto.getGameKind())) {
      builder.must(QueryBuilders.matchQuery("gameKind", dto.getGameKind()));
    }
    if (StringUtils.isNotEmpty(dto.getLiveGameSuperType())) {
      builder.must(QueryBuilders.matchQuery("gameType", dto.getLiveGameSuperType()));
    }
    if (null != dto.getTimeType() && StringUtils.isNotBlank(dto.getBeginTime())) {
      String keyword = "betTime.keyword";
      if (TimeTypeEnum.BET_TIME.getValue() == dto.getTimeType()) {
        keyword = "betTime.keyword";
      }
      if (TimeTypeEnum.THIRD_TIME.getValue() == dto.getTimeType()) {
        keyword = "amesTime.keyword";
      }
      if (TimeTypeEnum.SETTLE_TIME.getValue() == dto.getTimeType()) {
        keyword = "settleTime.keyword";
      }
      if (TimeTypeEnum.STAT_TIME.getValue() == dto.getTimeType()) {
        keyword = "statTime.keyword";
      }
      builder.must(
          QueryBuilders.rangeQuery(keyword)
              .from(dto.getBeginTime())
              .to(dto.getEndTime() == null ? "now" : dto.getEndTime())
              .format(DateUtils.DATE_TIME_PATTERN));
    }
    return builder;
  }

  public static QueryBuilder buildBetRecordSearch(GameVaildBetRecordQueryDTO dto) {
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("account", dto.getAccount()));
    }
    if (StringUtils.isNotEmpty(dto.getBillNo())) {
      builder.must(QueryBuilders.matchQuery("billNo", dto.getBillNo()));
    }
    if (StringUtils.isNotEmpty(dto.getPlatformCode())) {
      builder.must(QueryBuilders.matchQuery("platformCode", dto.getPlatformCode()));
    }
    if (StringUtils.isNotEmpty(dto.getGameKind())) {
      builder.must(QueryBuilders.matchQuery("gameKind", dto.getGameKind()));
    }
    if (StringUtils.isNotEmpty(dto.getState())) {
      builder.must(QueryBuilders.matchQuery("settle", dto.getState()));
    }
    if (null != dto.getTimeType() && StringUtils.isNotBlank(dto.getBeginTime())) {
      String keyword = "betTime.keyword";
      if (TimeTypeEnum.BET_TIME.getValue() == dto.getTimeType()) {
        keyword = "betTime.keyword";
      }
      if (TimeTypeEnum.THIRD_TIME.getValue() == dto.getTimeType()) {
        keyword = "amesTime.keyword";
      }
      if (TimeTypeEnum.SETTLE_TIME.getValue() == dto.getTimeType()) {
        keyword = "settleTime.keyword";
      }
      if (TimeTypeEnum.STAT_TIME.getValue() == dto.getTimeType()) {
        keyword = "statTime.keyword";
      }
      builder.must(
          QueryBuilders.rangeQuery(keyword)
              .from(dto.getBeginTime())
              .to(dto.getEndTime() == null ? "now" : dto.getEndTime())
              .format(DateUtils.DATE_TIME_PATTERN));
    }
    return builder;
  }
}
