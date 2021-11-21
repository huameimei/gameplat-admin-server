package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: lyq
 * @Date: 2020/8/27 14:32
 * @Description:
 */
@Data
public class ActivityDistributeSumVO {

    @ApiModelProperty(value = "小计")
    private BigDecimal subtotalMoney;

    @ApiModelProperty(value = "总计")
    private BigDecimal allMoney;


}
