package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 作者 zl
 * @version 创建时间：2017年11月12日 上午22:57:51 日志实体
 */
@Data
@TableName("sys_log_login")
public class SysLogLogin implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String userName;

  private String module;

  private String doTime;

  private String method;

  private String path;

  private String params;

  private String userAgent;

  private String deviceType;

  private String domain;

  private Boolean flag;

  private String logType;

  private String ipAddress;

  private String ipDesc;

  @TableField("`desc`")
  private String desc;

  private String remark;

  private Date createTime;
}
