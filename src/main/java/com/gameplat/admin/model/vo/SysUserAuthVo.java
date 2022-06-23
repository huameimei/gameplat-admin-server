package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kb @Date 2022/3/12 17:14 @Version 1.0
 */
@Data
public class SysUserAuthVo implements Serializable {

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
}
