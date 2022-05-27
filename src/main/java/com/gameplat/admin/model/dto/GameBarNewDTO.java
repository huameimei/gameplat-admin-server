package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gameplat.security.SecurityUserHolder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GameBarNewDTO implements Serializable {
    /**
     * 数据主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 导航名称
     */
    private String name;

    /**
     * 导航状态
     */
    private Integer state;

    /**
     * 是否展示返水    0 不展示   1  展示返水
     */
    private Integer isEnableWater;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * PC 图片
     */
    private String pcImg;

    /**
     * 导航Logo
     */
    private String barLogo;


    /**
     * 游戏相关图片配置
     */
    private String gameImgConfig;


    /**
     * 创建时间
     */
    private Date createTime  = new Date();

    /**
     * 创建人
     */
    private String createBy =  SecurityUserHolder.getCredential().getUsername();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

    /**
     * 更新人
     */
    private String updateBy  =  SecurityUserHolder.getCredential().getUsername();
}
