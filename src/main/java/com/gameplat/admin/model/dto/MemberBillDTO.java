package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author lily
 * @description 查询现金流水入参
 * @date 2021/12/2
 */
@Data
public class MemberBillDTO implements Serializable {

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "订单号，关联其他业务订单号")
    private String orderNo;

    @ApiModelProperty(value = "账变类型：TranTypes中值")
    private List<Integer> tranTypes;

    @ApiModelProperty(value = "添加开始时间")
    private String beginTime;

    @ApiModelProperty(value = "添加结束时间")
    private String endTime;

    @ApiModelProperty(value = "分表ID")
    private Integer tableIndex;
}

