package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpreadUnionPackageVO implements Serializable {

    @ApiModelProperty(value = "联盟名称")
    private String unionName;

    @ApiModelProperty(value = "代理账号")
    private String agentAccount;

    @ApiModelProperty(value = "渠道类型")
    private String channel;

    @ApiModelProperty(value = "联盟Id")
    private Long unionId;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "联盟包id")
    private Integer unionPackageId;

    @ApiModelProperty(value = "联盟包名称")
    private String unionPackageName;

    @ApiModelProperty(value = "联运平台")
    private String unionPlatform;

    @ApiModelProperty(value = "推广域名")
    private String promotionDomain;

    @ApiModelProperty(value = "联运专用IOS包下载地址")
    private String iosDownloadUrl;

    @ApiModelProperty(value = "联运专用安卓包下载地址")
    private String appDownloadUrl;

    @ApiModelProperty(value = "联运状态")
    private Integer unionStatus;

    @ApiModelProperty(value = "注册用户")
    private Integer sumUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


}
