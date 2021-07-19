package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;

@Data
public class SysAuthIpVO {

  /** IP白名单 */
  private String allowIp;

  /** 类型 */
  private String ipType;

  private Date createTime;

  private String createBy;

  private String remark;
}
