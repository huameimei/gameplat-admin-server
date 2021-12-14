package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 转盘表
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@TableName("activity_turntable")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityTurntable对象", description = "转盘表")
public class ActivityTurntableAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "turntable_id", type = IdType.AUTO)
    private Long turntableId;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "类型（数据字典：game游戏，live：直播）")
    private String type;

    @ApiModelProperty(value = "展示位置")
    private String display;

    @ApiModelProperty(value = "转1次消耗")
    private Integer turnOne;

    @ApiModelProperty(value = "转10此消耗")
    private Integer turnTen;

    @ApiModelProperty(value = "转1次幸运值")
    private Integer turnOneLucky;

    @ApiModelProperty(value = "转10次幸运值")
    private Integer turnTenLucky;

    @ApiModelProperty(value = "总幸运值")
    private Integer totalLucky;

    @ApiModelProperty(value = "幸运值满赠送")
    private Long luckyGive;

    @ApiModelProperty(value = "红包时间(周日到周六，用1到7表示)")
    private String weekTime;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "转盘标题")
    private String turnTitle;

}
