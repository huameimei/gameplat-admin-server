package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("chat_red_envelope_record")
public class ChatRedEnvelopeRecord implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "红包配置ID")
    private Long redConfigId;

    @ApiModelProperty(value = "红包总额")
    private Double money;

    @ApiModelProperty(value = "红包总个数")
    private Integer sumNumber;

    @ApiModelProperty(value = "已经领取金额")
    private Double alreadyMoney;

    @ApiModelProperty(value = "已经领取个数")
    private Integer alreadyNumber;

    @ApiModelProperty(value = "所属聊天室")
    private Integer roomId;

    @ApiModelProperty(value = "红包名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "过期时间")
    private Long overdueTime;
}
