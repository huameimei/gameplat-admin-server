package com.gameplat.admin.model.vo;

import com.gameplat.admin.enums.TimeTypeEnum;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: asky
 * @date: 2022/1/14 17:57
 * @desc:
 */
@Data
public class GameBetValidRecordVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long validId;

  private String id;

  /** 唯一编码 */
  private String billNo;

  /** 租户编码 */
  private String tenant;

  /** 用户名 */
  private String account;

  /** 平台编码 */
  private String platformCode;

  /** 游戏分类 */
  private String gameKind;

  /** 游戏编码 */
  private String gameCode;

  /** 游戏名称 */
  private String gameName;

  /** 币种 */
  private String currency;

  /** 结算状态 */
  private Integer settle;

  /** 投注金额 */
  private BigDecimal betAmount;

  /** 有效投注额 */
  private BigDecimal validAmount;

  /** 输赢金额 */
  private BigDecimal winAmount;

  /** 投注内容 */
  private String betContent;

  /** 投注时间 */
  private Date betTime;

  /** 下注美东时间 */
  private Date amesTime;

  /** 结算时间 */
  private Date settleTime;

  /** 报表统计时间(美东) */
  private Date statTime;

  /** 添加时间 */
  private Date createTime;

  /** 更新时间 */
  private Date updateTime;

  public static QueryBuilder buildBetRecordSearch(GameBetRecordQueryDTO dto) {
    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      builder.must(QueryBuilders.termQuery("account", dto.getAccount()));
    }
    if (StringUtils.isNotEmpty(dto.getBillNo())) {
      builder.must(QueryBuilders.matchQuery("billNo", dto.getBillNo()));
    }
    if (null != dto.getTimeType() && StringUtils.isNotBlank(dto.getBeginTime())) {
      String keyword = "betTime";
      if (TimeTypeEnum.BET_TIME.getValue() == dto.getTimeType()) {
        keyword = "betTime";
      }
      if (TimeTypeEnum.THIRD_TIME.getValue() == dto.getTimeType()) {
        keyword = "amesTime";
      }
      if (TimeTypeEnum.SETTLE_TIME.getValue() == dto.getTimeType()) {
        keyword = "settleTime";
      }
      if (TimeTypeEnum.STAT_TIME.getValue() == dto.getTimeType()) {
        keyword = "statTime";
      }
      builder.must(
          QueryBuilders.rangeQuery(keyword)
              .from(dto.getBeginTime())
              .to(dto.getEndTime() == null ? "now" : dto.getBeginTime())
              .format(DateUtils.DATE_TIME_PATTERN));
    }
    return builder;
  }

}
