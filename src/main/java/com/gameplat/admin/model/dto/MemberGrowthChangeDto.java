package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description : 会员成长值变动dto
 * @Author : lily
 * @Date : 2021/12/08
 */
@Data
public class MemberGrowthChangeDto {
    @ApiModelProperty(required = true, value = "会员id")
    private Long userId;

    @ApiModelProperty("会员账号")
    private String userName;

    @ApiModelProperty("此次变动金额")
    private Integer changeGrowth;

    @ApiModelProperty(required = true, value = "类型：0:充值  1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
    private Integer type;

    @ApiModelProperty("变动原因")
    private String remark;

    @ApiModelProperty("变动游戏分类")
    private String kindCode;

    @ApiModelProperty("变动游戏分类名称")
    private String kindName;

    @ApiModelProperty("变动倍数")
    private BigDecimal changeMult;
}
