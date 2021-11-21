package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.live.cloud.backend.model.activity.entity.MemberActivityLobby;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author: whh
 * @Date: 2020/8/20 19:08
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QualificationActivityVO extends MemberActivityLobby {

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "资格id")
    private Long qualificationId;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(value = "用户状态 0:非正常 1:正常")
    private Integer userStatus;

    @ApiModelProperty(value = "用户充值层级")
    private String rank;

    @ApiModelProperty(value = "操作状态")
    private Integer status;

    @ApiModelProperty(value = "审核人")
    private String auditPerson;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "资格审核返回msg")
    private String msg;

    private Date activityStartTime;

    private Date activityEndTime;

    private String title;

    private String username;

    private String activityName;

    private Integer activityType;

}
