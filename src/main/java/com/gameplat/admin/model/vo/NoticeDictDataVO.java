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
public class NoticeDictDataVO implements Serializable {

    @ApiModelProperty(value = "公告类型")
    private List noticeType;

    @ApiModelProperty(value = "位置")
    private List noticeCategory;

    @ApiModelProperty(value = "个人消息类型")
    private List pushMessageType;

    @ApiModelProperty(value = "总类别")
    private List noticeTotalCategory;
}
