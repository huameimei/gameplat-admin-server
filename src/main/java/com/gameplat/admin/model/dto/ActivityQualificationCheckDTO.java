package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动资格检测DTO
 *
 * @Author: kenvin
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationCheckDTO implements Serializable {

    private static final long serialVersionUID = -3594282509149807621L;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "统计日期")
    private String countDate;

}
