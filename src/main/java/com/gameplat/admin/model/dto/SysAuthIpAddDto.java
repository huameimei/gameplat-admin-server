package com.gameplat.admin.model.dto;

import lombok.Data;

@Data
public class SysAuthIpAddDto {

  /**IP白名单 */
  private String allowIp;

  /**类型*/
  private Integer ipType;

  private String remark;
}