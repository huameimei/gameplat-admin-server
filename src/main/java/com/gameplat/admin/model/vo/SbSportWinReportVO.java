package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.I18nSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SbSportWinReportVO implements Serializable {

  @Schema(description = "投注类型名字")
  @Excel(name = "投注类型", width = 15, isImportField = "true_st")
  @JsonSerialize(using = I18nSerializerUtils.class)
  private String betTypeName;

  @Schema(description = "投注类型")
  private String betType;

  @Schema(description = "投注人数")
  @Excel(name = "投注人数", width = 15, isImportField = "true_st")
  private Long userCount = 0L;

  @Schema(description = "投注次数")
  @Excel(name = "投注次数", width = 15, isImportField = "true_st")
  private Long betCount = 0L;

  @Schema(description = "投注金额")
  @Excel(name = "投注金额", width = 15, isImportField = "true_st")
  private BigDecimal betAmount = BigDecimal.ZERO;

  @Schema(description = "有效投注金额")
  @Excel(name = "有效投注金额", width = 15, isImportField = "true_st")
  private BigDecimal validAmount = BigDecimal.ZERO;

  @Schema(description = "派奖金额")
  @Excel(name = "派奖金额", width = 15, isImportField = "true_st")
  private BigDecimal sendPrizeAmount = BigDecimal.ZERO;

  @Schema(description = "人均投注金额")
  @Excel(name = "人均投注金额", width = 15, isImportField = "true_st")
  private BigDecimal averageBetAmount = BigDecimal.ZERO;

  @Schema(description = "游戏输赢金额")
  @Excel(name = "游戏输赢金额", width = 15, isImportField = "true_st")
  private BigDecimal gameWinAmount = BigDecimal.ZERO;

  @Schema(description = "会员账号")
  private String memberAccount;

  @Schema(description = "代理账号")
  private String proxyAccount;

  @Schema(description = "是否包含直属下级")
  private Integer isDirectly;

  @Schema(description = "开始时间")
  private Long beginTime;

  @Schema(description = "结束时间")
  private Long endTime;
}
