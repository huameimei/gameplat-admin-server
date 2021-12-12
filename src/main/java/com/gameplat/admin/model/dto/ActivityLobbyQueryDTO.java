package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动大厅查询DTO
 *
 * @Author: kenvin
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityLobbyQueryDTO", description = "活动大厅查询DTO")
public class ActivityLobbyQueryDTO implements Serializable {

    private static final long serialVersionUID = 6060013282905693277L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "活动状态（0 关闭，1 开启，2 失效）")
    private Integer status;

    @ApiModelProperty(value = "活动状态（0 关闭，1 开启，2 失效）")
    private Integer failure;

}
