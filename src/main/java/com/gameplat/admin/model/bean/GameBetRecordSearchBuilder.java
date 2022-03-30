package com.gameplat.admin.model.bean;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.GameVaildBetRecordQueryDTO;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.game.util.GameDateUtils;
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
    if (StringUtils.isNotEmpty(dto.getGameType())) {
      builder.must(QueryBuilders.matchQuery("gameType", dto.getGameType()));
    }
    if (null != dto.getTimeType() && ObjectUtil.isNotNull(dto.getBeginTime())) {
      builder.must(
          QueryBuilders.rangeQuery(convertTimeType(dto.getTimeType()))
              .from(DateUtil.date(GameDateUtils.get0ZoneDate(dto.getBeginTime())).toJdkDate())
              .to(
                  dto.getEndTime() == null
                      ? DateUtil.date(GameDateUtils.get0ZoneDate(System.currentTimeMillis()))
                          .toJdkDate()
                      : DateUtil.date(GameDateUtils.get0ZoneDate(dto.getEndTime())).toJdkDate())
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
      builder.must(
          QueryBuilders.rangeQuery(convertTimeType(dto.getTimeType()))
              .from(DateUtil.date(GameDateUtils.get0ZoneDate(dto.getBeginTime())))
              .to(
                  dto.getEndTime() == null
                      ? DateUtil.date(GameDateUtils.get0ZoneDate(System.currentTimeMillis()))
                      : DateUtil.date(GameDateUtils.get0ZoneDate(dto.getEndTime())))
              .format(DateUtils.DATE_TIME_PATTERN));
    }
    return builder;
  }

  /** 1 -- 投注时间, 2 -- 三方时间, 3 -- 结算时间, 4 -- 报表时间 */
  public static String convertTimeType(Integer timeType) {
    String keyword = "betTime.keyword";
    if (TimeTypeEnum.BET_TIME.getValue() == timeType) {
      keyword = "betTime.keyword";
    }
    if (TimeTypeEnum.THIRD_TIME.getValue() == timeType) {
      keyword = "amesTime.keyword";
    }
    if (TimeTypeEnum.SETTLE_TIME.getValue() == timeType) {
      keyword = "settleTime.keyword";
    }
    if (TimeTypeEnum.STAT_TIME.getValue() == timeType) {
      keyword = "statTime.keyword";
    }
    return keyword;
  }
}
