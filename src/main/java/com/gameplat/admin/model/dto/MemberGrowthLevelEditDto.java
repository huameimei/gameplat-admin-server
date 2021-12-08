package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author lily
 * @description 修改VIP等级入参
 * @date 2021/11/21
 */

@Data
@ApiModel(value="修改VIP等级入参",description="修改VIP等级入参")
public class MemberGrowthLevelEditDto implements Serializable {

    @ApiModelProperty(value = "主键", name = "id", required = true)
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "等级", required = false)
    private Integer level;

    @ApiModelProperty(value = "等级名称", required = false)
    private String levelName;

    @ApiModelProperty(value = "晋升下级所需成长值", required = false)
    private Integer growth;

    @ApiModelProperty(value = "保级成长值", required = false)
    private Integer limitGrowth;

    @ApiModelProperty(value = "升级奖励", required = false)
    private BigDecimal upReward;

    @ApiModelProperty(value = "周俸禄", required = false)
    private BigDecimal weekWage;

    @ApiModelProperty(value = "月俸禄", required = false)
    private BigDecimal monthWage;

    @ApiModelProperty(value = "生日礼金", required = false)
    private BigDecimal birthGiftMoney;

    @ApiModelProperty(value = "每月红包", required = false)
    private BigDecimal redEnvelope;

    @ApiModelProperty(value = "更新人", required = false)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", required = false)
    private Date updateTime;

    @ApiModelProperty(value = "语言", required = false)
    private String language;


}
