package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author bhf
 * @Description 存储文件记录实体层
 * @Date 2020/5/21 19:04
 **/
@Data
public class SysFileManagerQueryDTO implements Serializable {

  @ApiModelProperty(value = "原文件名")
  private String oldFileName;

  @ApiModelProperty(value = "存储文件名")
  private String storeFileName;

  @ApiModelProperty(value = "文件类型")
  private String fileType;

  @ApiModelProperty(value = "上传者")
  private String createBy;

  @ApiModelProperty(value = "开始时间")
  private String startTime;

  @ApiModelProperty(value = "结束时间")
  private String endTime;

}
