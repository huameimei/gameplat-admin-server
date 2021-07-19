package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("sys_auth_ip")
public class SysAuthIp {

  private Long id;
  /** IP白名单 */
  private String allowIp;

  /** 类型 0-后端， 1-前端 多个逗号隔开 */
  private String ipType;

  private Date createTime;

  private String createBy;

  private String remark;
}
