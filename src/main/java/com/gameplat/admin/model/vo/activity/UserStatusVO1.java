package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: whh
 * @Date: 2020/8/21 11:28
 * @Description:
 */
@Data
public class UserStatusVO1 implements Serializable {

    private static final long serialVersionUID = -8355913159056700838L;
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "0非正常  1正常")
    private Integer status;

}
