package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description VIP会员签到汇总入参
 * @date 2021/11/24
 */

@Data
public class MemberVipSignStatisDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("会员账号")
    private String userName;

    @ApiModelProperty(value = "最后签到开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty(value = "最后签到结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;


}
