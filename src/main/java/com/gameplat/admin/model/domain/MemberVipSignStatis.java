package com.gameplat.admin.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description VIP会员签到汇总实体类
 * @date 2021/11/24
 */

@Data
@TableName("member_vip_sign_statis")
public class MemberVipSignStatis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("会员姓名")
    @Excel(name = "会员账号", height = 20, width = 30, isImportField = "true_st")
    private String userName;

    @ApiModelProperty("签到次数")
    @Excel(name = "总签到次数", height = 20, width = 30, isImportField = "true_st")
    private Integer signCount;

    @ApiModelProperty("七天连续签到次数")
    private Integer continueWeekSign;

    @ApiModelProperty("15天连续签到次数")
    private Integer continueHalfMonthSign;

    @ApiModelProperty("30天连续签到次数")
    private Integer continueMonthSign;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @Excel(name = "最后签到时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st", width = 20)
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @Excel(name = "创建时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st", width = 20)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "备注")
    private String remark;

}
