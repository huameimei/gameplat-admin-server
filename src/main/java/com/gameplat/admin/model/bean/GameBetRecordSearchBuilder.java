package com.gameplat.admin.model.bean;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.GameVaildBetRecordQueryDTO;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class GameBetRecordSearchBuilder {

  public static QueryBuilder buildBetRecordSearch(GameBetRecordQueryDTO dto) {
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("account.keyword", dto.getAccount()));
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
    if (StringUtils.isNotEmpty(dto.getGameCode())) {
      builder.must(QueryBuilders.termQuery("gameCode", dto.getGameCode()));
    }
    if (StringUtils.isNotEmpty(dto.getSettle())) {
      builder.must(QueryBuilders.matchQuery("settle", dto.getSettle()));
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
    if (null != dto.getTimeType() && ObjectUtil.isNotNull(dto.getBeginTime())) {
      builder.must(
          QueryBuilders.rangeQuery(convertTimeType(dto.getTimeType()))
              .gte(DateUtil.date(Long.parseLong(dto.getBeginTime())).getTime())
              .lte(
                  dto.getEndTime() == null
                      ? DateUtil.date(System.currentTimeMillis()).getTime()
                      : DateUtil.date(Long.parseLong(dto.getEndTime())).getTime()));
    }
    return builder;
  }

  public static QueryBuilder buildBetRecordSearch(GameVaildBetRecordQueryDTO dto) {
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("account.keyword", dto.getAccount()));
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
              .from(date2TimeStamp(dto.getBeginTime()).getTime())
              .to(
                  StringUtils.isNotEmpty(dto.getEndTime())
                      ? date2TimeStamp(dto.getEndTime()).getTime()
                      : System.currentTimeMillis()));
    }

    return builder;
  }

  /** 1 -- 投注时间, 2 -- 三方时间, 3 -- 结算时间, 4 -- 报表时间 */
  public static String convertTimeType(Integer timeType) {
    String keyword = "betTime";
    if (TimeTypeEnum.BET_TIME.getValue() == timeType) {
      keyword = "betTime";
    }
    if (TimeTypeEnum.THIRD_TIME.getValue() == timeType) {
      keyword = "amesTime";
    }
    if (TimeTypeEnum.SETTLE_TIME.getValue() == timeType) {
      keyword = "settleTime";
    }
    if (TimeTypeEnum.STAT_TIME.getValue() == timeType) {
      keyword = "statTime";
    }
    return keyword;
  }

  /**
   * 22 * 日期格式字符串转换成时间戳 23 * @param date 字符串日期 24 * @param format 如：yyyy-MM-dd HH:mm:ss 25 * @return
   * 26
   */
  public static Date date2TimeStamp(String date_str) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN);
      return sdf.parse(date_str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new Date();
  }
}
