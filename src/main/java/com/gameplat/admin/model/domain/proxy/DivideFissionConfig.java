package com.gameplat.admin.model.domain.proxy;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 代理配置
 * @Author : cc
 * @Date : 2022/2/20
 */
@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TableName("divide_fission_config")
public class DivideFissionConfig extends Model<DivideFissionConfig> {
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "代理ID")
    private Long userId;

    @ApiModelProperty(value = "代理账号")
    private String userName;

    @ApiModelProperty(value = "分红配置")
    private String divideConfig;

    @ApiModelProperty(value = "周期配置")
    private String recycleConfig;

    @ApiModelProperty(value = "周期外分红点")
    private BigDecimal recycleOutConfig;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
