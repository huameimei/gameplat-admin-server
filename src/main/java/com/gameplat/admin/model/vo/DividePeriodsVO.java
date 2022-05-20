package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 分红期数VO @Author : cc @Date : 2022/2/26
 */
@Data
public class DividePeriodsVO implements Serializable {

  private static final long serialVersionUID = 7535872776555957753L;

  @Schema(description = "主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @Schema(description = "期数结算起始日期")
  private String startDate;

  @Schema(description = "期数结算截止日期")
  private String endDate;

  @Schema(description = "结算状态 1 未结算  2 已结算")
  private Integer settleStatus;

  @Schema(description = "派发状态 1 未派发  2 已派发")
  private Integer grantStatus;

  @Schema(description = "结算时业主开启的分红模式 1 固定比例  2 裂变  3 层层代 4 平级")
  private Integer divideType;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "备注")
  private String remark;
}
