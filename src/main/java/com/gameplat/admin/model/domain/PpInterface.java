package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pp_interface")
public class PpInterface {

  @TableId(type = IdType.AUTO)
  public Long id;

  private String name;

  private String code;

  private String version;

  private String charset;

  private Integer dispatchType;

  private String dispatchUrl;

  private String dispatchMethod;

  private Integer status;

  private String parameters;

  private Integer orderQueryEnabled;

  private String orderQueryVersion;

  private String orderQueryUrl;

  private String orderQueryMethod;

  private String limtInfo;

  private Integer asynNotifyStatus;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
}
