package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

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
@EqualsAndHashCode(callSuper = false)
public class MemberTurntableVO implements Serializable {

    private static final long serialVersionUID=1L;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    @Mapping(value = "turntableId")
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

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "幸运值满赠送")
    private Long luckyGive;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "转盘标题")
    private String turnTitle;

    @ApiModelProperty(value = "红包时间(周一到周日)")
    private String weekTime;

}
