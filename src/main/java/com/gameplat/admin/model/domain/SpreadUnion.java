package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@TableName("spread_union")
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "联运对象", description = "活动表")
public class SpreadUnion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "联盟名称")
    private String unionName;

    @ApiModelProperty(value = "代理账号")
    private Integer agentAccount;

    @ApiModelProperty(value = "渠道类型")
    private Integer channel;

    @ApiModelProperty(value = "联盟包id")
    private Integer unionPackageId;

    @ApiModelProperty(value = "联盟包名称")
    private Date unionPackageName;

    @ApiModelProperty(value = "联运平台")
    private Date unionPlatform;

    @ApiModelProperty(value = "推广域名")
    private Integer promotionDomain;

    @ApiModelProperty(value = "联运专用IOS包")
    private String iosDownloadUrl;

    @ApiModelProperty(value = "联运专用安卓包")
    private Date appDownloadUrl;

    @ApiModelProperty(value = "联运状态")
    private String unionStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;


}
