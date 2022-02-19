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
@TableName("version_control")
public class VersionControl {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "强制更新 0 否 1是")
    private Integer forceUpdate;

    @ApiModelProperty(value = "更新内容")
    private String content;

    @ApiModelProperty(value = "版本类型 0(安卓) 1(IOS)")
    private Integer type;

    @ApiModelProperty(value = "状态 0 失效 1 有效")
    private Integer state;

    @ApiModelProperty(value = "安卓包下载地址")
    private String androidUrl;

    @ApiModelProperty(value = "1 内链  2 外链")
    private Integer androidUrlType;

    @ApiModelProperty(value = "ios企业签")
    private String iosEnterpriseSing;

    @ApiModelProperty(value = "ios超级签")
    private String iosSuperSing;

    @ApiModelProperty(value = "ios描述文件")
    private String iosDescribeUrl;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
