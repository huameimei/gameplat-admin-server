package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author aBen
 * @date 2021/5/6 17:02
 * @desc
 */
@Data
public class ActivityStatisticItem implements Serializable {

  @Schema(description = "会员ID")
  private Long userId;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "充值时间")
  private Date rechargeTime;

  @Schema(description = "充值时间集合")
  private List<Date> rechargeTimeList;

  @Schema(description = "充值时间字符串")
  private String rechargeTimes;

  @Schema(description = "充值金额")
  private BigDecimal rechargeAmount;

  @Schema(description = "累计充值金额")
  private BigDecimal accumulativeRechargeAmount;

  @Schema(description = "有效投注额")
  private BigDecimal validAmount;

  @Schema(description = "连续充值天数")
  private Integer seriesRechargeNum;

  @Schema(description = "累计充值天数")
  private Integer accumulativeRechargeNum;

  @Schema(description = "首充金额")
  private BigDecimal firstRechargeAmount;

  @Schema(description = "二充金额")
  private BigDecimal twoRechargeAmount;

  @Schema(description = "累计游戏打码量")
  private BigDecimal cumulativeGameDml;

  @Schema(description = "累计游戏打码天数")
  private Integer cumulativeGameDmDays;

  @Schema(description = "游戏统计时间集合")
  private List<Date> gameCountDateList;

  @Schema(description = "游戏统计时间字符串")
  private String gameCountDates;

  @Schema(description = "游戏输赢金额")
  private BigDecimal gameWinAmount;

  @Schema(description = "是否新用户 1是 2否")
  private Integer isNewUser;

  @Schema(description = "计算基数")
  private BigDecimal calculateValue;

  public ActivityStatisticItem() {
    this.rechargeAmount = new BigDecimal(0);
    this.accumulativeRechargeAmount = new BigDecimal(0);
    this.validAmount = new BigDecimal(0);
    this.seriesRechargeNum = 0;
    this.accumulativeRechargeNum = 0;
    this.firstRechargeAmount = new BigDecimal(0);
    this.cumulativeGameDml = new BigDecimal(0);
    this.cumulativeGameDmDays = 0;
    this.gameWinAmount = new BigDecimal(0);
  }
}
