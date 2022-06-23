package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动奖品DTO
 *
 * @since 2020-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityPrizeDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "奖品id")
  private Long activityPrizeId;

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

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "是否组合（0否，1是）")
  private Integer isGroup;

  @Schema(description = "活动组合id")
  private Long groupId;
}
