package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description vip等级汇总查询出参
 * @date 2021/11/24
 */
@Data
public class MemberGrowthStatisVO implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

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

    @ApiModelProperty(value = "未达到保底累计扣除")
    private Integer demoteGrowth;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;

}
