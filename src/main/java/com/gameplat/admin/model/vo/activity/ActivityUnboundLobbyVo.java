package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2021/5/18 15:11
 * @desc
 */
@Data
public class ActivityUnboundLobbyVo implements Serializable {

    @ApiModelProperty(value = "活动大厅ID")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

}
