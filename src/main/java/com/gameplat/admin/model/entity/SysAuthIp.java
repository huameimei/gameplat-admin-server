package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("sys_auth_ip")
public class SysAuthIp {

  private Long id;
  /**IP白名单 */
  private String allowIp;

  /**类型*/
  private Integer ipType;

  private Date  createTime;

  private String createBy;

  private String remark;
}
