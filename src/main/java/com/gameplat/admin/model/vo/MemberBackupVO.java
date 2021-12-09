package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class MemberBackupVO implements Serializable {

  private Long id;

  private String serialNo;

  private Integer type;

  private String content;

  private Date createTime;

  private String createBy;

  private Date updateTime;

  private String updateBy;
}
