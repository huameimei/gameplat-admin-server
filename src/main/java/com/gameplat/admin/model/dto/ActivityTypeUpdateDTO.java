package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动类型更新DTO
 *
 * @author aguai
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityTypeUpdateDTO", description = "活动类型DTO")
public class ActivityTypeUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "活动类型")
    private String typeCode;

    @ApiModelProperty(value = "活动类型名称")
    private String typeName;

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
