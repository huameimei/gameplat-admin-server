package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("等级")
  private Integer level;

  @ApiModelProperty("等级名称")
  private String levelName;

  @ApiModelProperty("成长值")
  private Long growth;

  @ApiModelProperty("等级保级成长值")
  private Long limitGrowth;

  @ApiModelProperty("升级奖励")
  private BigDecimal upReward;

  @ApiModelProperty("周俸禄")
  private BigDecimal weekWage;

  @ApiModelProperty("月俸禄")
  private BigDecimal monthWage;

  @ApiModelProperty("生日礼金")
  private BigDecimal birthGiftMoney;

  @ApiModelProperty("红包")
  private BigDecimal redEnvelope;

  @ApiModelProperty("借呗额度")
  private BigDecimal creditMoney;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty("移动端VIP图片")
  private String mobileVipImage;

  @ApiModelProperty("WEB端VIP图片")
  private String webVipImage;

  @ApiModelProperty("移动端达成背景图片")
  private String mobileReachBackImage;

  @ApiModelProperty("移动端未达成背景图片")
  private String mobileUnreachBackImage;

  @ApiModelProperty("移动端达成VIP图片")
  private String mobileReachVipImage;

  @ApiModelProperty("移动端未达成VIP图片")
  private String mobileUnreachVipImage;

  @ApiModelProperty("WEB端达成VIP图片")
  private String webReachVipImage;

  @ApiModelProperty("WEB端未达成VIP图片")
  private String webUnreachVipImage;

    @ApiModelProperty("金币成长值比例")
    private BigDecimal coinRatio;

    @ApiModelProperty("金币最大获取数")
    private Integer maxCoin;

    @ApiModelProperty("金币最大获取数")
    private Integer dailyMaxCoin;
}
