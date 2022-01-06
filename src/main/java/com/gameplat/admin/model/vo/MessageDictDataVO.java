package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lily
 * @description
 * @date 2021/12/27
 */

@Data
public class MessageDictDataVO implements Serializable {

    @ApiModelProperty(value = "推送范围")
    private List UserRange;

    @ApiModelProperty(value = "弹出界面")
    private List Location;

    @ApiModelProperty(value = "弹出次数")
    private List PopCount;

    @ApiModelProperty(value = "消息类别")
    private List MessageCate;

    @ApiModelProperty(value = "消息展示类型")
    private List messageShowType;
}

