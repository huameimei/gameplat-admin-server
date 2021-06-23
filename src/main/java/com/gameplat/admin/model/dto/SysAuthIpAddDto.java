package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysAuthIpAddDto  extends BaseEntity {

  /**IP白名单 */
  private String allowIp;

  /**类型*/
  private Integer ipType;

  private String remark;
}