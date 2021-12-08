package com.gameplat.admin.model.domain;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lily
 * @since 2021/11/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysLayerConfig对象", description="")
public class SysLayerConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "layer_id", type = IdType.AUTO)
    private Integer layerId;

    @ApiModelProperty(value = "层级名称")
    private String layerName;

    @ApiModelProperty(value = "层级值")
    private Integer layerValue;

    @ApiModelProperty(value = "层级类型")
    private Integer layerType;

    @ApiModelProperty(value = "充值总次数阈值")
    private Long rechargeTotalTime;

    @ApiModelProperty(value = "充值总金额阈值")
    private BigDecimal rechargeAmountTotal;

    @ApiModelProperty(value = "锁定会员数量")
    private Long lockMemberNum;

    @ApiModelProperty(value = "会员出款次数")
    private Long memberOutTime;

    @ApiModelProperty(value = "是否限制提现 0不限制 1限制")
    private Integer withdrawLimit;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = " 状态 0禁用 1启用")
    private Integer status;
    
    @ApiModelProperty(value = "层级人数")
    private Long layerPersonNum;

    @ApiModelProperty(value = " 锁定（0 锁定  1带锁定）")
    private Integer lockState;

}
