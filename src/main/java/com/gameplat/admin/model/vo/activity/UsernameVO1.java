package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: lyq
 * @Date: 2020/8/24 17:20
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UsernameVO1 implements Serializable {

    private static final long serialVersionUID = -8071294399708379974L;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "用户账号")
    private String username;
}
