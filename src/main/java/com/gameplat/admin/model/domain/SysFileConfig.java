package com.gameplat.admin.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysFileConfig implements Serializable {

    @ApiModelProperty(value = "服务提供商（1.本地 2.minio 3.七牛云 4.阿里云 5.腾讯云）")
    private Integer serviceProvider;

    @ApiModelProperty(value = "服务提供商")
    private String diskPath;

    @ApiModelProperty(value = "文件预览接口")
    private String preview;

    @ApiModelProperty(value = "access_key")
    private String accessKey;

    @ApiModelProperty(value = "secret_key")
    private String secretKey;

    @ApiModelProperty(value = "bucket空间")
    private String bucket;

    @ApiModelProperty(value = "endpoint域名")
    private String endpoint;

    @ApiModelProperty(value = "zone存储区域（所属地域）")
    private Integer zone;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "备用字段1")
    private String paramOne;

    @ApiModelProperty(value = "备用字段2")
    private String paramTwo;

    @ApiModelProperty(value = "0禁用 1启用")
    private Integer enable;

    @ApiModelProperty(value = "请求类型")
    private String http;

    @ApiModelProperty(value = "bucket存储区域")
    private String bucketRegion;

    @ApiModelProperty(value = "最后一次操作（1表示最后一次操作）")
    private Integer lastTime;

    @ApiModelProperty(value = "文件是否鉴黄 0禁用 1启用")
    private Integer fileIsYellow;

    @ApiModelProperty(value = "鉴黄类型 图片、视频、音频、文字")
    private String fileYellowType;

    @ApiModelProperty(value = "官网地址")
    private String url;

    @ApiModelProperty(value = "是否开启cdn")
    private Integer cdnOpen;

    @ApiModelProperty(value = "cdn地址")
    private String cdnUrl;

    @ApiModelProperty(value = "tracker服务器节点，支持多个")
    private List<String> trackerList;

    @ApiModelProperty(value = "socket连接超时时长")
    private Integer soTimeout;

    @ApiModelProperty(value = "连接tracker服务器超时时长")
    private Integer connectTimeout;

    @ApiModelProperty(value = "缩略图高度")
    private Integer height;

    @ApiModelProperty(value = "缩略图宽度")
    private Integer width;
}
