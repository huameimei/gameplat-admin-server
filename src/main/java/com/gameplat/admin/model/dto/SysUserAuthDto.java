package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kb @Date 2022/3/12 17:14 @Version 1.0
 */
@Data
public class SysUserAuthDto implements Serializable {

  @Schema(description = "主键")
  private Integer authId;

  @Schema(description = "身份认证中心url")
  private String idcardAuthUrl;

  @Schema(description = "身份认证中心key")
  private String idcardAuthKey;

  @Schema(description = "银行认证中心url")
  private String bankAuthUrl;

  @Schema(description = "银行认证中心key")
  private String bankAuthKey;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private Date updateTime;
}
