package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: asky
 * @date: 2021/4/10 13:44
 * @desc:
 */
@Data
public class IpStatisticsVO {

    @ApiModelProperty(value = "ip地址")
    private String loginIp;

    @ApiModelProperty(value = "次数")
    private String frequency;
}
