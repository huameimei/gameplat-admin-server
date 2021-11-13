package com.gameplat.admin.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 短信配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysSmsConfig implements Serializable {

    @ApiModelProperty(value = "运营商（1:阿里云 2:聚合）")
    private Integer operator;

    @ApiModelProperty(value = "运营商名称")
    private String operatorName;

    @ApiModelProperty(value = "短信签名")
    private String sign = "";

    @ApiModelProperty(value = "帐号（聚合）")
    private String account = "";

    @ApiModelProperty(value = "密码（聚合）")
    private String password = "";

    @ApiModelProperty(value = "0禁用 1启用")
    private Integer enable;

    @ApiModelProperty(value = "公共参数1")
    private String strOne = "";

    @ApiModelProperty(value = "公共参数2")
    private String strTwo = "";

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "最后一次操作（1表示最后一次操作）")
    private Integer lastTime = 0;

    @ApiModelProperty(value = "短信标签")
    private String smsLabel;

    @ApiModelProperty(value = "appId")
    private String appId;


    @ApiModelProperty(value = "短信模板")
    private String tid;


    @ApiModelProperty(value = "请求地址")
    private String url;


    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "国际短息appid")
    private String areaAppId;

    @ApiModelProperty(value = "是否开启代理")
    private Boolean isEnableAgency;

    @ApiModelProperty(value = "代理地址")
    private String agencyUrl;

    @ApiModelProperty(value = "代理端口")
    private String agencyPort;


}
