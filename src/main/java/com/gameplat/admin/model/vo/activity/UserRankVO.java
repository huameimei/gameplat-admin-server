package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: whh
 * @Date: 2020/8/21 10:34
 * @Description:  用户充值层级
 */
@Data
public class UserRankVO implements Serializable {

    private static final long serialVersionUID = 2623543951570062528L;
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "用户充值层级")
    private String rank;

}
