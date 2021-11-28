package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员成长值汇总实体类
 * @date 2021/11/23
 */

@Data
@TableName("member_growth_statis")
public class MemberGrowthStatis implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long userId;

    @ApiModelProperty(value = "会员账号")
    private String userName;

    @ApiModelProperty(value = "会员当前等级")
    private Integer level;

    @ApiModelProperty(value = "会员当前的成长值")
    private Integer growth;

    @ApiModelProperty(value = "充值累计成长值")
    private Integer rechargeGrowth;

    @ApiModelProperty(value = "签到累计成长值")
    private Integer signGrowth;

    @ApiModelProperty(value = "打码量累计成长值")
    private Integer damaGrowth;

    @ApiModelProperty(value = "后台修改累计成长值")
    private Integer backGrowth;

    @ApiModelProperty(value = "完善资料累计成长值")
    private Integer infoGrowth;

    @ApiModelProperty(value = "绑定银行卡累计成长值")
    private Integer bindGrowth;

    @ApiModelProperty(value = "未达到保底累计扣除成长值")
    private Integer demoteGrowth;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
