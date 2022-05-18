package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 用户成长值等级配置出参
 * @date 2021/11/20
 */
@Data
public class MemberGrowthLevelVO {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "等级")
  private Integer level;

  @Schema(description = "等级名称")
  private String levelName;

  @Schema(description = "成长值")
  private Long growth;

  @Schema(description = "等级保级成长值")
  private Long limitGrowth;

  @Schema(description = "升级奖励")
  private BigDecimal upReward;

  @Schema(description = "周俸禄")
  private BigDecimal weekWage;

  @Schema(description = "月俸禄")
  private BigDecimal monthWage;

  @Schema(description = "生日礼金")
  private BigDecimal birthGiftMoney;

  @Schema(description = "红包")
  private BigDecimal redEnvelope;

  @Schema(description = "借呗额度")
  private BigDecimal creditMoney;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "移动端VIP图片")
  private String mobileVipImage;

  @Schema(description = "WEB端VIP图片")
  private String webVipImage;

  @Schema(description = "移动端达成背景图片")
  private String mobileReachBackImage;

  @Schema(description = "移动端未达成背景图片")
  private String mobileUnreachBackImage;

  @Schema(description = "移动端达成VIP图片")
  private String mobileReachVipImage;

  @Schema(description = "移动端未达成VIP图片")
  private String mobileUnreachVipImage;

  @Schema(description = "WEB端达成VIP图片")
  private String webReachVipImage;

  @Schema(description = "WEB端未达成VIP图片")
  private String webUnreachVipImage;

  @Schema(description = "金币成长值比例")
  private BigDecimal coinRatio;

  @Schema(description = "金币最大获取数")
  private Integer maxCoin;

  @Schema(description = "每日金币上限")
  private Integer dailyMaxCoin;

  @Schema(description = "借呗额度")
  private BigDecimal loanMoney;
}
