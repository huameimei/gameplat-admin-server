package com.gameplat.admin.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author bhf @Description 存储文件记录实体层 @Date 2020/5/21 19:04
 */
@Data
public class SysFileManagerVO implements Serializable {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "服务提供商")
  private Integer serviceProvider;

  @ApiModelProperty(value = "服务提供商")
  private String providerName;

  @ApiModelProperty(value = "原文件名")
  private String oldFileName;

  @ApiModelProperty(value = "存储文件名")
  private String storeFileName;

  @ApiModelProperty(value = "图片地址")
  private String fileUrl;

  @ApiModelProperty(value = "文件类型")
  private String fileType;

  @ApiModelProperty(value = "文件大小")
  private String fileSize;

  @ApiModelProperty(value = "上传状态（0：上传失败 1：上传成功）")
  private Integer status;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  @JSONField(format = "unixtime")
  private Date createTime;
}
