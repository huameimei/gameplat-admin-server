package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 转盘表
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@TableName("activity_turntable")
@EqualsAndHashCode(callSuper = false)
public class ActivityTurntableAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  @TableId(value = "turntable_id", type = IdType.AUTO)
  private Long turntableId;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "类型（数据字典：game游戏，live：直播）")
  private String type;

  @Schema(description = "展示位置")
  private String display;

  @Schema(description = "转1次消耗")
  private Integer turnOne;

  @Schema(description = "转10此消耗")
  private Integer turnTen;

  @Schema(description = "转1次幸运值")
  private Integer turnOneLucky;

  @Schema(description = "转10次幸运值")
  private Integer turnTenLucky;

  @Schema(description = "总幸运值")
  private Integer totalLucky;

  @Schema(description = "幸运值满赠送")
  private Long luckyGive;

  @Schema(description = "红包时间(周日到周六，用1到7表示)")
  private String weekTime;

  @Schema(description = "状态 0下线 1上线")
  private Integer status;

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

  @Schema(description = "转盘标题")
  private String turnTitle;
}
