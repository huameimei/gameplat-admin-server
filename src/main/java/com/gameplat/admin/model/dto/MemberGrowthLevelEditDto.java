package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 修改VIP等级入参
 * @date 2021/11/21
 */
@Data
public class MemberGrowthLevelEditDto implements Serializable {

  @Schema(description = "主键", name = "id", required = true)
  @NotNull(message = "编号不能为空")
  private Long id;

  @Schema(description = "等级", required = false)
  private Integer level;

  @Schema(description = "等级名称", required = false)
  private String levelName;

  @Schema(description = "晋升下级所需成长值", required = false)
  private Long growth;

  @Schema(description = "保级成长值", required = false)
  private Long limitGrowth;

  @Schema(description = "每日金币上限", required = false)
  private Integer dailyMaxCoin;

  @Schema(description = "借呗额度", required = false)
  private BigDecimal loanMoney;

  @Schema(description = "升级奖励", required = false)
  private BigDecimal upReward;

  @Schema(description = "周俸禄", required = false)
  private BigDecimal weekWage;

  @Schema(description = "月俸禄", required = false)
  private BigDecimal monthWage;

  @Schema(description = "生日礼金", required = false)
  private BigDecimal birthGiftMoney;

  @Schema(description = "每月红包", required = false)
  private BigDecimal redEnvelope;

  @Schema(description = "更新人", required = false)
  private String updateBy;

  @Schema(description = "更新时间", required = false)
  private Date updateTime;

  @Schema(description = "语言", required = false)
  private String language;
}
