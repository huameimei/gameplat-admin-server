package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 红包雨
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityRedPacket对象", description = "红包雨")
public class ActivityRedPacketAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "红包时间(周一到周日)")
    private String weekTime;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

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

    @ApiModelProperty(value = "红包雨标题")
    private String realTitle;

    @ApiModelProperty(value = "红包雨位置")
    private String realLocation;

    @ApiModelProperty(value = "频率")
    private String frequency;

    @ApiModelProperty(value = "持续时长、下雨时长")
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

    @ApiModelProperty(value = "红包条件")
    private List<ActivityRedPacketConditionDTO> redPacketConditionList;

    @ApiModelProperty(value = "奖品列表")
    private List<ActivityPrizeDTO> activityPrize;

}
