package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 系统域名
 * @date 2022/2/16
 */
@Data
@TableName("tenant_domain")
public class TenantDomain implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "租户code")
    private String tenant;

    @ApiModelProperty(value = "域名类型")
    private String domainType;

    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "是否cdn加速")
    private Integer isCdn;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否启用(0/1)")
    private Integer display;

    @ApiModelProperty(value = "是否测试域名 0 否  1 是")
    private Integer isTest;

    @ApiModelProperty(value = "是否socket域名 0 否  1 是")
    private Integer isSocket;

    @ApiModelProperty(value = "解析地址")
    private String analysisAddress;

    @ApiModelProperty(value = "是否已解析 0 否  1 是")
    private Integer isAnalysis;

    @ApiModelProperty(value = "是否ssl认证 0 否  1 是")
    private Integer isSsl;

    @ApiModelProperty(value = "域名状态")
    private String domainStatus;

    @ApiModelProperty(value = "是否返回给前端 0 否  1 是")
    private Integer isClient;

    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "是否自身提供域名 0 否  1 是")
    private Integer isOwn;

    @ApiModelProperty(value = "'逻辑删除1是删除，0是存在 ")
    private Integer isDelete;
}
