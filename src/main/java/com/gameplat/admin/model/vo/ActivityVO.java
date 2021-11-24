package com.gameplat.admin.model.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 活动VO
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    @Mapping(value = "activityId")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "是否参与红包雨 0否 1是")
    private Integer isPackage;

    @ApiModelProperty(value = "是否参与每日首充 0否 1是")
    private Integer isCharge;

    @ApiModelProperty(value = "是否参与转盘 0否 1是")
    private Integer isTurntable;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;


}
