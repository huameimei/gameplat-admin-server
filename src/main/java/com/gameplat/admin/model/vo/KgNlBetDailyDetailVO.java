package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/8/15 21:09
 * @desc
 */
@Data
public class KgNlBetDailyDetailVO {

  @Schema(description = "会员账号")
  @Excel(name = "会员账号", width = 20, isImportField = "true_st")
  private String account;

  @Schema(description = "彩种编码")
  private String gameCode;

  @Schema(description = "彩种名字")
  @Excel(name = "彩种名字", width = 20, isImportField = "true_st")
  private String gameCodeName;

  @Schema(description = "投注次数")
  @Excel(name = "投注次数", width = 15, isImportField = "true_st")
  private Long betCount = 0L;

  @Schema(description = "投注金额")
  @Excel(name = "投注金额", width = 15, isImportField = "true_st")
  private BigDecimal betAmount = BigDecimal.ZERO;

  @Schema(description = "有效投注金额")
  @Excel(name = "有效投注金额", width = 15, isImportField = "true_st")
  private BigDecimal validAmount = BigDecimal.ZERO;

  @Schema(description = "输赢金额")
  @Excel(name = "输赢金额", width = 15, isImportField = "true_st")
  private BigDecimal winAmount = BigDecimal.ZERO;

  @Schema(description = "派奖金额")
  @Excel(name = "派奖金额", width = 15, isImportField = "true_st")
  private BigDecimal sendPrizeAmount = BigDecimal.ZERO;


}
