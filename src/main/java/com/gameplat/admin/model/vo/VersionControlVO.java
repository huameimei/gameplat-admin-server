package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 系统发版信息
 */
@Data
public class VersionControlVO {

    private Long id;

    private String title;

    private String version;

    private Integer forceUpdate;

    private String content;

    private String type;

    private Integer state;

    private String androidUrl;

    private String urlType;

    private String iosEnterpriseSing;

    private String iosSuperSing;

    private String iosDescribeUrl;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}
