package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @Description : VIP会员签到历史记录入参
 * @Author : lily
 * @Date : 2021/12/07
 */
@Data
public class MemberVipSignHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("会员账号")
    private String userName;

    @ApiModelProperty(value = "签到时间-开始")
    private String signBeginTime;

    @ApiModelProperty(value = "签到时间-结束")
    private String signEndTime;
}
