package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author bhf
 * @Description 活动信息返回
 * @Date 2020/6/11 10:29
 **/
@Data
public class AppTurntableActivityVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    private Long turntableId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
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

    @ApiModelProperty(value = "活动奖品集合")
    private List<com.live.cloud.backend.model.activity.vo.AppActivityPrizeVO> activityPrizeVOS;

    @ApiModelProperty(value = "開始時間戳")
    private Long longBeginTime;

    @ApiModelProperty(value = "結束時間戳")
    private Long longEndTime;


}
