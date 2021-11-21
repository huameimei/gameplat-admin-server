package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dl
 * @since 2020-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysExperienceSource对象", description="")
public class SysExperienceSourceVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id：1、用户经验; 2、粉丝经验; 3、直播间等级经验值" )
    private Integer id;

    @ApiModelProperty(value = "充值货币打赏经验值")
    private Long rechargeExperience;

    @ApiModelProperty(value = "充值货币打赏经验开关（0、关闭 1、开启）")
    private Integer rechargeSwitch;

    @ApiModelProperty(value = "积分货币打赏经验值")
    private Long integralExperience;

    @ApiModelProperty(value = "积分货币打赏经验开关（0、关闭 1、开启）")
    private Integer integralSwitch;

    @ApiModelProperty(value = "人民币充值经验")
    private Long rmbExperience;

    @ApiModelProperty(value = "人民币充值开关（0、关闭 1、开启）")
    private Integer rmbSwitch;

    @ApiModelProperty(value = "任务经验（0、关闭   1、开启）")
    private Integer taskExperience;


}
