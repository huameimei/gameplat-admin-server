package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2021/5/6 17:02
 * @desc
 */
@Data
public class ActivityMemberInfo implements Serializable {

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "0非正常  1正常")
    private Integer status;

    @ApiModelProperty(value = "用户充值层级")
    private String userRank;

    @ApiModelProperty(value = "用户VIP等级")
    private Integer userLevel;

    @ApiModelProperty(value = "登录IP")
    private String lastLoginIp;
}
