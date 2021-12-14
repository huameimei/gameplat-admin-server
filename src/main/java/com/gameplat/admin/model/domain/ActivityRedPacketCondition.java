package com.gameplat.admin.model.domain;

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
 *
 * </p>
 *
 * @author 沙漠
 * @since 2020-06-16
 */
@Data
@TableName("activity_red_packet_condition")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityRedPacketCondition对象")
public class ActivityRedPacketCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long conditionId;

    @ApiModelProperty(value = "红包id")
    private Long redPacketId;

    @ApiModelProperty(value = "充值金额")
    private Integer topUpMoney;

    @ApiModelProperty(value = "抽奖次数")
    private Integer drawNum;

    @ApiModelProperty(value = "红包雨最小金额")
    private Integer packetMinMoney;

    @ApiModelProperty(value = "红包雨最大金额")
    private Integer packetMaxMoney;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
