package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.time.TimeZones;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_tenant_setting")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysTenantSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    public SysTenantSetting(){}

    public SysTenantSetting(String tenant) {
        this.tenant = tenant;
    }

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "租户标识")
    private String tenant;

    @ApiModelProperty(value = "设置类型")
    private String settingType;

    @ApiModelProperty(value = "设置code")
    private String settingCode;

    @ApiModelProperty(value = "设置名称")
    private String settingLabel;

    @ApiModelProperty(value = "设置值/url地址")
    private String settingValue;

    @ApiModelProperty(value = "设置描述")
    private String settingDesc;

    @ApiModelProperty(value = "是否启用(0/1)")
    private Integer display;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "文件地址")
    private String filePath;

    @ApiModelProperty(value = "文件地址")
    private String picturePath;

    @ApiModelProperty(value = "是否默认首页")
    private Integer isIndex;

    @ApiModelProperty(value = "扩展字段1")
    @TableField(value = "extend_1")
    private String extend1;

    @ApiModelProperty(value = "扩展字段2")
    @TableField(value = "extend_2")
    private String extend2;

    @ApiModelProperty(value = "扩展字段3")
    @TableField(value = "extend_3")
    private String extend3;

    @ApiModelProperty(value = "扩展字段4")
    @TableField(value = "extend_4")
    private String extend4;

    @ApiModelProperty(value = "扩展字段5")
    @TableField(value = "extend_5")
    private String extend5;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TimeZones.GMT_ID + "+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TimeZones.GMT_ID + "+8")
    private Date updateTime;

    @ApiModelProperty(value = "主题")
    @TableField(exist = false)
    private String theme;
    @ApiModelProperty(value = "维护状态 0正常 1维护中")
    @TableField(exist = false)
    private Integer status;
    @TableField(exist = false)
    private String i18n;
    @TableField(exist = false)
    private String zhCn;
    @TableField(exist = false)
    private String enUs;
    @TableField(exist = false)
    private String thTh;
    @TableField(exist = false)
    private String viVn;
    @TableField(exist = false)
    private String inId;
}
