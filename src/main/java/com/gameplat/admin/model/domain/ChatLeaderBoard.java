package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("chat_leader_board")
@Accessors(chain = true)
public class ChatLeaderBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "")
    private Long userId;

    @ApiModelProperty(value = "")
    private String account;

    @ApiModelProperty(value = "")
    private Long roomId;

    @ApiModelProperty(value = "")
    private Double winMoney;

    @ApiModelProperty(value = "")
    private Integer type;

    @ApiModelProperty(value = "1:自定义开奖推送")
    private String gameId;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
