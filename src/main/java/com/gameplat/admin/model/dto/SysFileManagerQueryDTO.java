package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author bhf
 * @Description 存储文件记录实体层
 * @Date 2020/5/21 19:04
 **/
@Data
public class SysFileManagerQueryDTO implements Serializable {

  @Schema(description = "原文件名")
  private String oldFileName;

  @Schema(description = "存储文件名")
  private String storeFileName;

  @Schema(description = "文件类型")
  private String fileType;

  @Schema(description = "上传者")
  private String createBy;

  @Schema(description = "开始时间")
  private String startTime;

  @Schema(description = "结束时间")
  private String endTime;
}
