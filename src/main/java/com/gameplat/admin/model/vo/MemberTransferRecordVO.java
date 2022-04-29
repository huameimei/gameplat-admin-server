package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MemberTransferRecordVO implements Serializable {

  private Long id;

  private String serialNo;

  private Integer type;

  private Integer status;

  private String content;

  private Date createTime;

  private String createBy;

  private Date updateTime;

  private String updateBy;
}
