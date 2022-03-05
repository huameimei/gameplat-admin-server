package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "会员ID")
  private Long userId;

  @ApiModelProperty(value = "会员账号")
  private String userName;

  @ApiModelProperty(value = "充值时间")
  private Date rechargeTime;

  @ApiModelProperty(value = "充值时间集合")
  private List<Date> rechargeTimeList;

  @ApiModelProperty(value = "充值时间字符串")
  private String rechargeTimes;

  @ApiModelProperty(value = "充值金额")
  private BigDecimal rechargeAmount;

  @ApiModelProperty(value = "累计充值金额")
  private BigDecimal accumulativeRechargeAmount;

  @ApiModelProperty(value = "有效投注额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "连续充值天数")
  private Integer seriesRechargeNum;

  @ApiModelProperty(value = "累计充值天数")
  private Integer accumulativeRechargeNum;

  @ApiModelProperty(value = "首充金额")
  private BigDecimal firstRechargeAmount;

  @ApiModelProperty(value = "累计游戏打码量")
  private BigDecimal cumulativeGameDml;

  @ApiModelProperty(value = "累计游戏打码天数")
  private Integer cumulativeGameDmDays;

  @ApiModelProperty(value = "游戏统计时间集合")
  private List<Date> gameCountDateList;

  @ApiModelProperty(value = "游戏统计时间字符串")
  private String gameCountDates;

  @ApiModelProperty(value = "游戏输赢金额")
  private BigDecimal gameWinAmount;

  @ApiModelProperty(value = "是否新用户 1是 2否")
  private Integer isNewUser;

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
