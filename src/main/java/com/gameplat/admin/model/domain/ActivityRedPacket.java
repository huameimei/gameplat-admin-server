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
 * 红包雨
 * </p>
 *
 * @author 沙漠
 */
@Data
@TableName("activity_red_packet")
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "packet_id", type = IdType.AUTO)
    private Long packetId;

    /**
     * 红包时间(周一到周日)
     */
    private String weekTime;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态 0下线 1上线
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 红包雨标题
     */
    private String realTitle;

    /**
     * 红包雨位置
     */
    private String realLocation;

    /**
     * 频率
     */
    private String frequency;

    /**
     * 持续时长
     */
    private String duration;

    /**
     * 红包类型（1红包雨，2运营红包）
     */
    private Integer packetType;

    /**
     * 红包总个数
     */
    private Integer packetTotalNum;

    /**
     * 用户抽红包总次数限制
     */
    private Integer packetDrawLimit;

    /**
     * 启动时间(时分秒)
     */
    private String startTime;

    /**
     * 终止时间(时分秒)
     */
    private String stopTime;

}
