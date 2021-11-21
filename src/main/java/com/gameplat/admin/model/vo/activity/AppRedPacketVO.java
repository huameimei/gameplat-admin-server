package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *@author lyq
 *@Description
 *@date 2020年6月17日 下午8:02:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppRedPacketVO implements Serializable{/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    @JsonSerialize(using= ToStringSerializer.class)
	@ApiModelProperty(value = "主键")
    private Long packetId;

    @ApiModelProperty(value = "红包时间(周一到周日)")
    private String weekTime;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "红包雨标题")
    private String realTitle;

    @ApiModelProperty(value = "红包雨位置")
    private String realLocation;

    @ApiModelProperty(value = "频率")
    private String frequency;

    @ApiModelProperty(value = "持续时长")
    private String duration;

    @ApiModelProperty(value = "红包类型（1红包雨，2运营红包）")
    private Integer packetType;

    @ApiModelProperty(value = "红包总个数")
    private Integer packetTotalNum;

    @ApiModelProperty(value = "用户抽红包总次数限制")
    private Integer packetDrawLimit;

    @ApiModelProperty(value = "启动时间(时分秒)")
    private String startTime;

    @ApiModelProperty(value = "终止时间(时分秒)")
    private String stopTime;

    @ApiModelProperty(value = "活动奖品集合")
    private List<AppActivityPrizeVO> activityPrizeVOS;

    @ApiModelProperty(value = "活动条件集合")
    private List<MemberRedPacketConditionVO> redPacketCondition;
}
