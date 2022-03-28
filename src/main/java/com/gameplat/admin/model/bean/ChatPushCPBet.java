package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChatPushCPBet implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "是否开启")
  private Integer isOpen;

  @ApiModelProperty(value = "自动分享到聊天室")
  private int autoShare;

  @ApiModelProperty(value = "直推会员")
  private String pushAccount;

  @ApiModelProperty(value = "是否只直推会员")
  private Integer onlyPushAccount;

  @ApiModelProperty(value = "推送黑名单")
  private String notPushAccount;

  @ApiModelProperty(value = "推送会员需要的总充值金额")
  private BigDecimal rechMoney;

  @ApiModelProperty(value = "推送会员需要的总充值次数")
  private Integer rechCount;

  @ApiModelProperty(value = "推送会员需要的当日总充值金额")
  private BigDecimal todayRechMoney;

  @ApiModelProperty(value = "玩法下注总金额")
  private Double totalMoney;

  @ApiModelProperty(value = "是否显示跟投")
  private Integer showHeel;

  @ApiModelProperty(value = "跟投显示最小金额")
  private double showHeelMinMoney;

  @ApiModelProperty(value = "排行榜最高显示数")
  private Integer leaderBoardTotalCount;

  @ApiModelProperty(value = "排行榜黑名单")
  private String leaderBoardBlackAccount;

  @ApiModelProperty(value = "只推送哪些玩法")
  private String pushPlayCode;

  @ApiModelProperty(value = "不推送哪些玩法")
  private String notPushPlayCode;

  //    /**
  //     *  游戏id
  //     */
  //    private String gameIds;

  @ApiModelProperty(value = "房间进入特效")
  private String vipEnterLevels;
}
