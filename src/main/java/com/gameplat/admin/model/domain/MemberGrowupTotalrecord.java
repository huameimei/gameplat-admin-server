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
 * 记录会员成长记录值总数表
 * @author lily
 * @date 2021/11/28
 * @version
 */
@Data
@TableName("member_growup_totalrecord")
public class MemberGrowupTotalrecord implements Serializable{

	private static final long serialVersionUID = 1L;
	
    @ApiModelProperty(value = "主键ID")
    private Integer id;
	
	@ApiModelProperty(value = "用户ID")
	private Long userId;
	
    @ApiModelProperty(value = "会员帐号")
    private String memberAccount;
    
    @ApiModelProperty(value = "会员名称")
    private String memberName;
    
    @ApiModelProperty(value = "会员总成长值")
    private Long growupNow;
    
    @ApiModelProperty(value = "会员总的充值金额")
    private BigDecimal totalAmount;
    
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
    
    @ApiModelProperty(value = "当前层级")
    private Integer levelNow;

    @ApiModelProperty(value = "当前层级名称")
    private String levelName;

}
