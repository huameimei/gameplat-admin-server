package com.gameplat.admin.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : VIP福利记录
 * @Author : lily
 * @Date : 2021/11/23
 */

@Data
@TableName("member_weal_reword")
public class MemberWealReword implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户账号")
    @Excel(name = "用户账号", height = 20, width = 30, isImportField = "true_st")
    private String userName;

    @ApiModelProperty("状态： 0：待审核   1：未领取  2：已完成  3:已失效")
    @Excel(name = "状态", replace = { "待审核_0", "未领取_1", "已完成_2", "已失效_3" }, isImportField = "true_st")
    private Integer status;

    @ApiModelProperty("老等级")
    private Integer oldLevel;

    @ApiModelProperty("当前等级")
    @Excel(name = "当前等级", replace = { "VIP1_1", "VIP2_2", "VIP3_3", "VIP4_4", "VIP5_5", "VIP6_6", "VIP7_7", "VIP8_8", "VIP9_9", "VIP10_10", "VIP11_11" }, height = 20, width = 30, isImportField = "true_st")
    private Integer currentLevel;

    @ApiModelProperty("派发金额")
    @Excel(name = "派发金额", height = 20, width = 30, isImportField = "true_st")
    private BigDecimal rewordAmount;

    @ApiModelProperty("提现打码量")
    private BigDecimal withdrawDml;

    @ApiModelProperty("类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
    @Excel(name = "类型", replace = { "升级奖励_0", "周俸禄_1", "月俸禄_2", "生日礼金_3", "每月红包_4" }, isImportField = "true_st")
    private Integer type;

    @ApiModelProperty(value = "流水号")
    private String serialNumber;

    @ApiModelProperty(value = "活动标题")
    private String activityTitle;

    @ApiModelProperty(value = "领取时间")
    private Date drawTime;

    @ApiModelProperty(value = "失效时间")
    private Date invalidTime;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st", width = 20)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;



}
