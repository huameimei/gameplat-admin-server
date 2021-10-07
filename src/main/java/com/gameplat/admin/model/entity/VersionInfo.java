package com.gameplat.admin.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lenovo
 */
@Data
public class VersionInfo implements Serializable {

    /**
     * 版本编码
     */
    private String versionCode;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 版本描述
     */
    private String versionDesc;

    /**
     * 版本号
     */
    private String versionName;

}
