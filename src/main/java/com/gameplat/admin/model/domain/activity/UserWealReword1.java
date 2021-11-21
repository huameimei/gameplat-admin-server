package com.gameplat.admin.model.domain.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 福利奖励中心
 * @Author : cc
 * @Date : 2021/5/1
 */
@Data
public class UserWealReword1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("老等级")
    private Integer oldLevel;

    @ApiModelProperty("新等级")
    private Integer currentLevel;

    @ApiModelProperty("派发金额")
    private BigDecimal rewordAmount;

    @ApiModelProperty("提现打码量")
    private BigDecimal withdrawDml;

    @ApiModelProperty("状态： 0：待审核   1：未领取  2：已完成  3:已失效")
    private Integer status;

    @ApiModelProperty("类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
    private Integer type;

    @ApiModelProperty(value = "领取时间")
    private Date drawTime;

    @ApiModelProperty(value = "失效时间")
    private Date invalidTime;

    @ApiModelProperty(value = "流水号")
    private String serialNumber;

    @ApiModelProperty(value = "活动标题")
    private String activityTitle;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
