package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 红包雨VO
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long packetId;

  @Schema(description = "红包时间(周一到周日)")
  private String weekTime;

  @Schema(description = "开始时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date beginTime;

  @Schema(description = "结束时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date endTime;

  @Schema(description = "状态 0下线 1上线")
  private Integer status;

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

  @Schema(description = "红包雨标题")
  private String realTitle;

  @Schema(description = "红包雨位置")
  private String realLocation;

  @Schema(description = "频率")
  private String frequency;

  @Schema(description = "持续时长")
  private String duration;

  @Schema(description = "红包类型（1红包雨，2运营红包）")
  private Integer packetType;

  @Schema(description = "红包总个数")
  private Integer packetTotalNum;

  @Schema(description = "用户抽红包总次数限制")
  private Integer packetDrawLimit;

  @Schema(description = "启动时间(时分秒)")
  private String startTime;

  @Schema(description = "终止时间(时分秒)")
  private String stopTime;
}
