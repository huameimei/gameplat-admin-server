package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 红包雨VO
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityRedPacketVO对象", description = "红包雨")
public class ActivityRedPacketVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long packetId;

    @ApiModelProperty(value = "红包时间(周一到周日)")
    private String weekTime;

    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date endTime;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

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

}
