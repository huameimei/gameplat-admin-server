package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChatPushCPBet implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "是否开启")
  private Integer isOpen;

  @Schema(description = "自动分享到聊天室")
  private int autoShare;

  @Schema(description = "直推会员")
  private String pushAccount;

  @Schema(description = "是否只直推会员")
  private Integer onlyPushAccount;

  @Schema(description = "推送黑名单")
  private String notPushAccount;

  @Schema(description = "推送会员需要的总充值金额")
  private BigDecimal rechMoney;

  @Schema(description = "推送会员需要的总充值次数")
  private Integer rechCount;

  @Schema(description = "推送会员需要的当日总充值金额")
  private BigDecimal todayRechMoney;

  @Schema(description = "玩法下注总金额")
  private Double totalMoney;

  @Schema(description = "是否显示跟投")
  private Integer showHeel;

  @Schema(description = "跟投显示最小金额")
  private double showHeelMinMoney;

  @Schema(description = "排行榜最高显示数")
  private Integer leaderBoardTotalCount;

  @Schema(description = "排行榜黑名单")
  private String leaderBoardBlackAccount;

  @Schema(description = "只推送哪些玩法")
  private String pushPlayCode;

  @Schema(description = "不推送哪些玩法")
  private String notPushPlayCode;

  //    /**
  //     *  游戏id
  //     */
  //    private String gameIds;

  @Schema(description = "房间进入特效")
  private String vipEnterLevels;
}
