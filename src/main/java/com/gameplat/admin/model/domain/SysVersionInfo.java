package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统发版信息
 */
@Data
@TableName("app_version")
public class SysVersionInfo {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;


    @ApiModelProperty(value = "标题")
    private String title;


    @ApiModelProperty(value = "版本号")
    private String versionNum;


    @ApiModelProperty(value = "版本内容")
    private String context;

    @ApiModelProperty(value = "版本内链还是外边  1 内链  2  外链")
    private String urlType;

    @ApiModelProperty(value = "版本类型 android ios")
    private String type;


    @ApiModelProperty(value = "包地址")
    private String url;


    @ApiModelProperty(value = "IOS包名")
    private String bundleId;


    @ApiModelProperty(value = "ipa路径")
    private String ipaUrl;

    @ApiModelProperty(value = "1 强更新 2 弱更新")
    private String changeType;


    @ApiModelProperty(value = " ios企业签名 0关闭 1开启")
    private Integer isThirdSign;


    @ApiModelProperty(value = " ios 企业签 url")
    private String thirdSignUrl;


    @ApiModelProperty(value = "ios超级签 0关闭 1开启")
    private Integer superSignStatus;


    @ApiModelProperty(value = "超级签地址")
    private String plistFileId;


    @ApiModelProperty(value = "文件描述地址 url")
    private String remarkFileUrl;


    @ApiModelProperty(value = "创建人")
    private String createBy;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    @ApiModelProperty(value = "更新人")
    private String updateBy;


    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @ApiModelProperty(value = "状态 （1 有效 0 无效）")
    private String state;



}
