package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/3/7 0:34
 * @Version 1.0
 */
@Data
public class ThreeRechReportVo implements Serializable {

    @ApiModelProperty(value = "第三方接口编码")
    private String tpInterfaceCode;

    @ApiModelProperty(value = "第三方接口名称")
    private String tpInterfaceName;


    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;
}
