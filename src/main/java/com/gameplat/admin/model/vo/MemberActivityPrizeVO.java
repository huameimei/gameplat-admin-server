package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;

/**
 * @author lyq @Description 活动奖品VO
 * @date 2020年6月5日 下午1:28:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberActivityPrizeVO implements Serializable {
  /** */
  private static final long serialVersionUID = 1L;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "活动奖品id")
  @Mapping(value = "id")
  private Long id;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "奖品id")
  private Long activityPrizeId;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "活动id")
  private Long activityId;

  @ApiModelProperty(value = "活动类型（1红包雨，2转盘）")
  private Integer type;

  @ApiModelProperty(value = "中奖概率")
  private Integer prizeChance;

  @ApiModelProperty(value = "奖品库存")
  private Integer prizeRepertory;

  @ApiModelProperty(value = "赠送数量")
  private Integer giveAmount;

  @ApiModelProperty(value = "一批次发放总数量")
  private Integer onceTotalNum;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "奖品主键")
  private Long prizeId;

  @ApiModelProperty(value = "奖品类型")
  private String prizeType;

  @ApiModelProperty(value = "奖品等级")
  private String prizeLevel;

  @ApiModelProperty(value = "奖品图片")
  private String prizeIcon;

  @ApiModelProperty(value = "奖品名称")
  private String prizeName;

  @ApiModelProperty(value = "券码")
  private String ticketYard;

  @ApiModelProperty(value = "是否组合（0否，1是）")
  private Integer isGroup;

  @ApiModelProperty(value = "活动组合id")
  private Integer groupId;
}
