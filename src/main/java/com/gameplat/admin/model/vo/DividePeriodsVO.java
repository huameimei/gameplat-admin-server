package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 分红期数VO @Author : cc @Date : 2022/2/26
 */
@Data
public class DividePeriodsVO implements Serializable {

  private static final long serialVersionUID = 7535872776555957753L;

  @ApiModelProperty(value = "主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @ApiModelProperty(value = "期数结算起始日期")
  private String startDate;

  @ApiModelProperty(value = "期数结算截止日期")
  private String endDate;

  @ApiModelProperty(value = "结算状态 1 未结算  2 已结算")
  private Integer settleStatus;

  @ApiModelProperty(value = "派发状态 1 未派发  2 已派发")
  private Integer grantStatus;

  @ApiModelProperty(value = "结算时业主开启的分红模式 1 固定比例  2 裂变  3 层层代 4 平级")
  private Integer divideType;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "备注")
  private String remark;
}
