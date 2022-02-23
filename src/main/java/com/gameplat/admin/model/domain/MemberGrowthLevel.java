package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @Description : 用户成长等级配置
 * @Author : lily
 * @Date : 2021/11/20
 */

@Data
@TableName("member_growth_level")
public class MemberGrowthLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("成长值")
    private Long growth;

    @ApiModelProperty("等级保级成长值")
    private Long limitGrowth;

    @ApiModelProperty("升级奖励")
    private BigDecimal upReward;

    @ApiModelProperty("周俸禄")
    private BigDecimal weekWage;

    @ApiModelProperty("月俸禄")
    private BigDecimal monthWage;

    @ApiModelProperty("生日礼金")
    private BigDecimal birthGiftMoney;

    @ApiModelProperty("红包")
    private BigDecimal redEnvelope;

    @ApiModelProperty("借呗额度")
    private BigDecimal creditMoney;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "移动端VIP图片")
    private String imageOne;

    @ApiModelProperty(value = "WEB端VIP图片")
    private String imageTwo;

    @ApiModelProperty(value = "移动端达成背景")
    private String imageThree;

    @ApiModelProperty(value = "移动端未达成背景")
    private String imageSmallOne;

    @ApiModelProperty(value = "移动端达成VIP图")
    private String imageSmallTwo;

    @ApiModelProperty(value = "移动端未达成VIP图")
    private String imageSmallThree;

    @ApiModelProperty(value = "WEB端达成VIP图")
    private String backImageOne;

    @ApiModelProperty(value = "WEB端未达成VIP图")
    private String backImageTwo;

    @ApiModelProperty(value = "WEB端未达成VIP图")
    private String backImageThree;

    @ApiModelProperty("移动端VIP图片")
    private String mobileVipImage;

    @ApiModelProperty("WEB端VIP图片")
    private String webVipImage;

    @ApiModelProperty("移动端达成背景图片")
    private String mobileReachBackImage;

    @ApiModelProperty("移动端未达成背景图片")
    private String mobileUnreachBackImage;

    @ApiModelProperty("移动端达成VIP图片")
    private String mobileReachVipImage;

    @ApiModelProperty("移动端未达成VIP图片")
    private String mobileUnreachVipImage;

    @ApiModelProperty("WEB端达成VIP图片")
    private String webReachVipImage;

    @ApiModelProperty("WEB端未达成VIP图片")
    private String webUnreachVipImage;
}
