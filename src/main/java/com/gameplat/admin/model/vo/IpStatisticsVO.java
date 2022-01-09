package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lily
 * @date: 2022/01/09
 * @desc:
 */
@Data
public class IpStatisticsVO {

    @ApiModelProperty(value = "ip地址")
    private String loginIp;

    @ApiModelProperty(value = "次数")
    private String frequency;
}
