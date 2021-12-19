package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;


/**
 * @author kenvin
 * @Description 实体层
 * @date 2020-08-20 11:27:34
 */
@Data
@TableName("activity_lobby_discount")
public class ActivityLobbyDiscount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 大厅优惠id
     */
    @TableId(value = "lobby_discount_id", type = IdType.AUTO)
    private Long lobbyDiscountId;

    /**
     * 活动大厅id
     */
    private Long lobbyId;

    /**
     * 优惠url
     */
    private String discountUrl;

    /**
     * 目标值
     */
    private Integer targetValue;

    /**
     * 赠送值
     */
    private Integer presenterValue;

    /**
     * 赠送打码
     */
    private BigDecimal presenterDml;

    /**
     * 提现打码
     */
    private Integer withdrawDml;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
