package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动大厅更新状态DTO
 * @Author: kenvin
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityLobbyUpdateStatusDTO", description = "活动大厅更新状态DTO")
public class ActivityLobbyUpdateStatusDTO implements Serializable {

    private static final long serialVersionUID = 6060013282905693277L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotBlank(message = "活动类型必选")
    @ApiModelProperty(value = "活动类型（1 充值活动，2 游戏活动")
    private Integer activityType;

    @NotBlank(message = "活动状态必选")
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "ip黑名单")
    private String ipBlacklist;

    @ApiModelProperty(value = "申请路径")
    private String applyUrl;

    @NotBlank(message = "活动状态必选")
    @ApiModelProperty(value = "活动状态（0 关闭，1 开启，2 失效）")
    private Integer status;

    @ApiModelProperty(value = "是否可以重复参加")
    private Integer isRepeat;

    @ApiModelProperty(value = "是否自动派发")
    private Integer isAutoDistribute;

}
