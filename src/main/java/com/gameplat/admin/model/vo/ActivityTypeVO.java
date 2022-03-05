package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动类型VO
 *
 * @author aguai
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityTypeVO", description = "活动类型VO")
public class ActivityTypeVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "编号")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @ApiModelProperty(value = "活动类型")
  private String typeCode;

  @ApiModelProperty(value = "活动类型名称")
  private String typeName;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "排序")
  private Integer sort;

  @ApiModelProperty(value = "状态")
  private Integer typeStatus;

  @ApiModelProperty(value = "浮窗状态")
  private Integer floatStatus;

  @ApiModelProperty(value = "浮窗logo")
  private String floatLogo;

  @ApiModelProperty(value = "浮窗url")
  private String floatUrl;

  @ApiModelProperty(value = "语言")
  private String language;
}
