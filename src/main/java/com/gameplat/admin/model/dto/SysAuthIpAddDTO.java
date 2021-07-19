package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysAuthIpAddDTO extends BaseEntity {

  /** IP白名单 */
  private String allowIp;

  /** 类型 */
  private String ipType;

  private String remark;
}
