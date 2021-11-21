package com.gameplat.admin.model.vo.activity;

import com.live.cloud.backend.model.activity.entity.QualificationManage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: whh
 * @Date: 2020/8/25 18:02
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityVO extends QualificationManage {

    @ApiModelProperty(value = "账号黑名单")
    private String accountBlacklist;

    @ApiModelProperty(value = "ip黑名单")
    private String ipBlacklist;

    @ApiModelProperty(value = "参与层级")
    private String graded;

    @ApiModelProperty(value = "红包类型（0 关闭，1 开启，2 失效）")
    private Integer type;

    @ApiModelProperty(value = "隔天申请（0 否，1 是）（在有效统计时间的次日申请活动彩金)")
    private Integer nextDayApply;

    @ApiModelProperty(value = "申请方式（1 手动，2 自动）")
    private Integer applyWay;

    @ApiModelProperty(value = "审核方式（1 手动，2 自动）")
    private Integer auditWay;

    @ApiModelProperty(value = "活动多重彩金是否开启  多重彩金（0 否，1 是）")
    private Integer multipleHandsel;

    @ApiModelProperty(value = "是否失效（0 失效，1 未失效）")
    private Integer disabled;

    @ApiModelProperty(value = "会员充值层级")
    private Integer memberPayLevel;


}
