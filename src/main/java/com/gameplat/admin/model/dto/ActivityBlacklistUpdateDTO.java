package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 活动黑名单DTO
 *
 * @author aBen
 * @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
public class ActivityBlacklistUpdateDTO implements Serializable {

    private static final long serialVersionUID = -1005615158531421103L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "限制内容")
    private String limitedContent;

    @ApiModelProperty(value = "限制类型 1会员账号  2 ip地址")
    private Integer limitedType;

    @ApiModelProperty(value = "会员账号")
    private String userName;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "删除的ID集合")
    private List<Long> ids;


}
