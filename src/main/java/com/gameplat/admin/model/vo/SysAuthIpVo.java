package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;

@Data
public class SysAuthIpVo {

  /**IP白名单 */
  private String allowIp;

  /**类型*/
  private Integer ipType;

  private Date createTime;

  private String createBy;

  private String remark;
}
