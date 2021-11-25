package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author lyq
 * @Description 实体层
 * @date 2020-08-20 11:27:34
 */
@Data
@TableName("activity_lobby_discount")
public class ActivityLobbyDiscount implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "大厅优惠id")
    private Long lobbyDiscountId;

    @ApiModelProperty(value = "活动大厅id")
    private Long lobbyId;

    @ApiModelProperty(value = "优惠url")
    private String discountUrl;

    @ApiModelProperty(value = "目标值")
    private Integer targetValue;

    @ApiModelProperty(value = "赠送值")
    private Integer presenterValue;

    @ApiModelProperty(value = "赠送打码")
    private BigDecimal presenterDml;

    @ApiModelProperty(value = "提现打码")
    private Integer withdrawDml;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
