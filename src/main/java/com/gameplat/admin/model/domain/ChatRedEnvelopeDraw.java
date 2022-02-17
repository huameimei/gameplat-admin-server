package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 红包领取记录
 * @date 2022/2/15
 */

@Data
@TableName("chat_red_envelope_draw")
public class ChatRedEnvelopeDraw implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "红包记录id")
    private String redEnvelopeRecordId;

    @ApiModelProperty(value = "领取总额")
    private Double drawMoney;

    @ApiModelProperty(value = "领取时间")
    private Long drawTime;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "领取账号")
    private String nickname;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "1. 待领取 2.已领取")
    private Integer status;
}
