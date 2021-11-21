package com.gameplat.admin.model.domain.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 首充优惠表
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MemberChargeDiscount对象", description="首充优惠表")
public class MemberChargeDiscount implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "discount_id", type = IdType.AUTO)
    private Long discountId;

    @ApiModelProperty(value = "每日首冲id")
    private Long chargeId;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "优惠（打折或者赠送数量）")
    private BigDecimal discountNum;

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

    @ApiModelProperty(value = "是否组合（0否，1是）")
    private Integer isGroup;

    @ApiModelProperty(value = "活动组合id")
    private Long groupId;


}
