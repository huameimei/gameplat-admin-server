package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统发版信息
 */
@Data
public class VersionControlDTO {

    private Long id;

    private String title;

    private String version;

    private Integer forceUpdate;

    private String content;

    private String type;

    private Integer state;

    private String androidUrl;

    private Integer androidUrlType;

    private String iosEnterpriseSing;

    private String iosSuperSing;

    private String iosDescribeUrl;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;



}
