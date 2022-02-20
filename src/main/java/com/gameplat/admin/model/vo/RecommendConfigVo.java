package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 代理配置
 * @Author : cc
 * @Date : 2022/2/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendConfigVo {
    private Long id;

    @ApiModelProperty(value = "是否开启代理中心 1 开启  0 关闭")
    private Integer isOpenAgentCenter;

    @ApiModelProperty(value = "分红是否累计 1 累计 0 不累计")
    private Integer isGrand;

    @ApiModelProperty(value = "是否包含代理数据")
    private Integer isIncludeAgent;

    @ApiModelProperty(value = "是否查询直属下级  1 是  0 否")
    private Integer isDirect;

    @ApiModelProperty(value = "全名注册代理开关")
    private Integer allRegisterAgent;

    @ApiModelProperty(value = "分红模式：分红模式 1 固定模式  2 裂变模式 3 层层代模式 4 平级模式")
    private Integer divideModel;

    @ApiModelProperty(value = "全民注册代理开启后 是否开启固定模式分红配置预设")
    private Integer fixDevideIsPreset;

    @ApiModelProperty(value = "固定模式 预设值")
    private String fixPresetValue;

    @ApiModelProperty(value = "全民注册代理开启后 是否开启裂变模式分红配置预设")
    private Integer fissionDevideIsPreset;

    @ApiModelProperty(value = "裂变模式配置")
    private String fissionPresetValue;

    @ApiModelProperty(value = "裂变模式 周期预设配置")
    private String recyclePresetValue;

    @ApiModelProperty(value = "裂变模式 周期外比例")
    private BigDecimal outRecyclePresetValue;

    @ApiModelProperty(value = "全民注册代理开启后 是否开启层层代理分红配置预设")
    private Integer layerDivideIsPreset;

    @ApiModelProperty(value = "层层代模式 预设值")
    private String layerPresetValue;

    @ApiModelProperty(value = "是否开启打码量计算")
    private Integer isOpenValidWithdraw;

    @ApiModelProperty(value = "开启打码量计算后的倍数")
    private BigDecimal validWithdrawMult;

    private Date createTime;

    private String createBy;

    private String updateBy;

    private Date updateTime;

    private String remark;
}
