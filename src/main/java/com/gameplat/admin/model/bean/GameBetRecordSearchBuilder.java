package com.gameplat.admin.model.bean;

import cn.hutool.core.util.ObjectUtil;
import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.GameVaildBetRecordQueryDTO;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("account", dto.getAccount()));
    }

    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getBillNo())) {
      builder.must(QueryBuilders.matchQuery("billNo", dto.getBillNo()));
    }

    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getPlatformCode())) {
      builder.must(QueryBuilders.matchQuery("platformCode", dto.getPlatformCode()));
    }

    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getGameKind())) {
      builder.must(QueryBuilders.matchQuery("gameKind", dto.getGameKind()));
    }

    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getState())) {
      builder.must(QueryBuilders.matchQuery("settle", dto.getState()));
    }

    if (ObjectUtil.isNotEmpty(dto.getBeginTime())) {
      String keyword = "betTime.keyword";
      builder.must(
          QueryBuilders.rangeQuery(keyword)
                  .gte(
                          DateUtils.get0ZoneDate(
                                  date2TimeStamp(dto.getBeginTime()), DateUtils.DATE_TIME_PATTERN))
                  .lte(
                          dto.getEndTime() == null
                                  ? new Date()
                                  : DateUtils.get0ZoneDate(
                                  date2TimeStamp(dto.getEndTime()), DateUtils.DATE_TIME_PATTERN)));
    }

    return builder;
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
