package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ActivityTypeVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "编号")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @Schema(description = "活动类型")
  private String typeCode;

  @Schema(description = "活动类型名称")
  private String typeName;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "排序")
  private Integer sort;

  @Schema(description = "状态")
  private Integer typeStatus;

  @Schema(description = "浮窗状态")
  private Integer floatStatus;

  @Schema(description = "浮窗logo")
  private String floatLogo;

  @Schema(description = "浮窗url")
  private String floatUrl;

  @Schema(description = "语言")
  private String language;
}
