package com.gameplat.admin.model.vo;

import com.gameplat.model.entity.game.GameTransferInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameAccountDataReportVo implements Serializable {

  @Schema(description = "登录人数")
  private int regNum;

  @Schema(description = "注册人数")
  private int logNum;

  @Schema(description = "全部余额")
  private BigDecimal goodMoney;

  @Schema(description = "过期时间")
  private String expiredTime;

  @Schema(description = "游戏可用额度")
  private BigDecimal gameQuota;

  @Schema(description = " 全部会员")
  private int accountNum;

  @Schema(description = "全部游戏余额")
  private BigDecimal totalGameBalance;

  private List<GameTransferInfo> transferInfoList = new ArrayList<>();

  private List<AccountReportVo> list = new ArrayList<>();
}
