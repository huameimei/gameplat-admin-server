package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动类型查询DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityTypeQueryDTO", description = "活动类型查询DTO")
public class ActivityTypeQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "活动类型")
    private String typeCode;

    @ApiModelProperty(value = "活动类型名称")
    private String typeName;

    @ApiModelProperty(value = "状态,0 无效,1 有效")
    private Integer typeStatus;

    @ApiModelProperty(value = "浮窗状态,0 无效,1 有效")
    private Integer floatStatus;

    @ApiModelProperty(value = "语言")
    private String language;

}
