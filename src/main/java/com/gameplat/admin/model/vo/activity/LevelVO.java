package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : dlei
 * @date : 2020-07-17
 * @Description: 用户等级请求对象
 **/
@Data
public class LevelVO {

    @ApiModelProperty(value = "等级信息", required = true)
    private com.live.cloud.backend.model.activity.vo.SysUserLevelVO userLevel;

    @ApiModelProperty(value = "等级信息", required = true)
    private com.live.cloud.backend.model.activity.vo.SysFansLevelVO fansLevel;

    @ApiModelProperty(value = "等级信息", required = true)
    private com.live.cloud.backend.model.activity.vo.SysLiveLevelVO liveLevel;

}
