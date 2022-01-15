package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员权益
 * @date 2022/1/15
 */

@Data
@TableName("member_weal_config")
public class MemberWealConfig implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权益名字")
    private String name;

    @ApiModelProperty(value = "最低专享等级")
    private Integer level;

    @ApiModelProperty(value = "未达到最低专享等级描述")
    private String turnDownDesc;

    @ApiModelProperty(value = "达到最低专享等级描述")
    private String turnUpDesc;

    @ApiModelProperty(value = "权益图片")
    private String image;

    @ApiModelProperty(value = "web端权益图标")
    private String webImage;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateB;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "语种：zh-CN   en-US   in-ID   th-TH   vi-VN")
    private String language;

    @ApiModelProperty(value = "36 代表 前端展示生日礼金 38周俸禄  39 月俸禄")
    private Integer type;
}
