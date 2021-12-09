package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动类型DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityType", description = "活动类型")
public class ActivityTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "活动类型")
    private String typeCode;

    @ApiModelProperty(value = "活动类型名称")
    private String typeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态,0 无效,1 有效")
    private Integer typeStatus;

    @ApiModelProperty(value = "浮窗状态,0 无效,1 有效")
    private Integer floatStatus;

    @ApiModelProperty(value = "浮窗logo")
    private String floatLogo;

    @ApiModelProperty(value = "浮窗url")
    private String floatUrl;

    @ApiModelProperty(value = "语言")
    private String language;

}
