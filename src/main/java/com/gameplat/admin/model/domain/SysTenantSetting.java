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

    /** 主键 */
    private Integer id;

    /** 租户标识 */
    private String tenant;

    /** 设置类型 */
    private String settingType;

    /** 设置code */
    private String settingCode;

    /** 设置名称 */
    private String settingLabel;

    /** 设置值/url地址 */
    private String settingValue;

    /** 设置描述 */
    private String settingDesc;

    /** 是否启用(0/1) */
    private Integer display;

    /**排序*/
    private Integer sort;

    /** 文件地址 */
    private String filePath;

    /** 文件地址 */
    private String picturePath;

    /** 是否默认首页 **/
    private Integer isIndex;

    /** 国际化语言 */
    private String i18n;

    private String zhCn;
    private String enUs;
    private String thTh;
    private String viVn;
    private String inId;

    /** 扩展字段1 */
    private String extend1;

    /** 扩展字段2 */
    private String extend2;

    /** 扩展字段3 */
    private String extend3;

    /** 扩展字段4 */
    private String extend4;
    /** 主题 */
    private String theme;

    /** 扩展字段5 */
    private String extend5;

    @ApiModelProperty(value = "维护状态 0正常 1维护中")
    private Integer status;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TimeZones.GMT_ID + "+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TimeZones.GMT_ID + "+8")
    private Date updateTime;
}
