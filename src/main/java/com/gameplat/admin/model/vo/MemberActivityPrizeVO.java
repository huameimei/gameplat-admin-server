package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberActivityPrizeVO implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "活动奖品id")
  @Mapping(value = "id")
  private Long id;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "奖品id")
  private Long activityPrizeId;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "活动id")
  private Long activityId;

  @Schema(description = "活动类型（1红包雨，2转盘）")
  private Integer type;

  @Schema(description = "中奖概率")
  private Integer prizeChance;

  @Schema(description = "奖品库存")
  private Integer prizeRepertory;

  @Schema(description = "赠送数量")
  private Integer giveAmount;

  @Schema(description = "一批次发放总数量")
  private Integer onceTotalNum;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "奖品主键")
  private Long prizeId;

  @Schema(description = "奖品类型")
  private String prizeType;

  @Schema(description = "奖品等级")
  private String prizeLevel;

  @Schema(description = "奖品图片")
  private String prizeIcon;

  @Schema(description = "奖品名称")
  private String prizeName;

  @Schema(description = "券码")
  private String ticketYard;

  @Schema(description = "是否组合（0否，1是）")
  private Integer isGroup;

  @Schema(description = "活动组合id")
  private Integer groupId;
}
