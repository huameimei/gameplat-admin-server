package com.gameplat.admin.model.dto;

import com.gameplat.admin.model.vo.GameDivideVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DivideConfigDTO {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "代理ID")
    private Long userId;

    @ApiModelProperty(value = "代理账号")
    private String userName;

    @ApiModelProperty(value = "代理账号")
    private String agentName;

    @ApiModelProperty(value = "上级账号")
    private String parentName;

    @ApiModelProperty(value = "分红配置")
    private String divideConfig;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否只查询下级")
    private Integer isOnlyQueryChild;

    @ApiModelProperty(value = "查询下级属性  1 只查直属下级  2 查询所有下级")
    private Integer queryChild;

    @ApiModelProperty(value = "修改的分红参数")
    Map<String, List<GameDivideVo>> ownerConfigMap;

    @ApiModelProperty(value = "修改的分红参数")
    Map<String, List<GameDivideVo>> ownerFixConfigMap;
}
