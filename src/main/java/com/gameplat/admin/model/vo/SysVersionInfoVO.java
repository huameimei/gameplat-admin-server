package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 系统发版信息
 */
@Data
public class SysVersionInfoVO {

    private Long id;

    /**
     * 标题
     */
    private String title;


    /**
     * 版本号
     */
    private String versionNum;


    /**
     * 版本内容
     */
    private String context;

    /**
     * 版本内链还是外边  1 内链  2  外链
     */
    private String urlType;



    /**
     * 版本类型 android ios
     */
    private String type;

    /**
     * 包地址
     */
    private String url;



    /**
     * IOS包名
     */
    private String bundleId;


    /**
     * ipa路径
     */
    private String ipaUrl;

    /**
     *  1 强更新 2 弱更新
     */
    private String changeType;

    /**
     *  ios企业签名 0关闭 1开启
     */
    private Integer isThirdSign;

    /**
     *  ios 企业签 url
     */
    private String thirdSignUrl;

    /**
     *  ios超级签 0关闭 1开启
     */
    private Integer superSignStatus;
    /**
     *  超级签地址
     */
    private String plistFileId;

    /**
     *  文件描述地址 url
     */
    private String remarkFileUrl;


    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新人
     */
    private String updateBy;


    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 状态 （1 有效 0 无效）
     */
    private String state;

}
