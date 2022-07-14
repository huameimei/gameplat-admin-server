package com.gameplat.admin.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author bhf @Description 存储文件记录实体层 @Date 2020/5/21 19:04
 */
@Data
public class SysFileManagerVO implements Serializable {

  @Schema(description = "主键")
  private String id;

  @Schema(description = "服务提供商")
  private Integer serviceProvider;

  @Schema(description = "服务提供商")
  private String providerName;

  @Schema(description = "原文件名")
  private String oldFileName;

  @Schema(description = "存储文件名")
  private String storeFileName;

  @Schema(description = "图片地址")
  private String fileUrl;

  @Schema(description = "文件类型")
  private String fileType;

  @Schema(description = "文件大小")
  private String fileSize;

  @Schema(description = "上传状态（0：上传失败 1：上传成功）")
  private Integer status;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JSONField(format = "unixtime")
  private Date createTime;
}
