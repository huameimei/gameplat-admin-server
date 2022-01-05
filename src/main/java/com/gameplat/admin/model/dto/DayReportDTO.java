package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员日报表查询入参
 * @date 2022/1/5
 */
@Data
public class DayReportDTO implements Serializable {

    private static final long serialVersionUID = 7169928923640880406L;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员账号(精确)")
    private String account;

    @ApiModelProperty(value = "会员所属上级路径")
    private String superPath;

    @ApiModelProperty(value = "开始时间")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;
}
