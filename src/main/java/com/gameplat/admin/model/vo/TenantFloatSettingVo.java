package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author key
 * @Date 2022/3/10
 * 游戏浮窗对象 tenant_float_setting
 */
@Data
public class TenantFloatSettingVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 浮窗类型typeID */
    private Integer typeId;

    /** 浮窗排序 */
    private Integer floatIndex;

    /** 是否显示(0否/1是) */
    private Integer whetherOpen;

    /** 是否可以拖拽(0否/1是) */
    private Integer canDrag;

    /** 跳转地址 */
    private String jumpUrl;
    /** 图片地址 */
    private String iconPath;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
