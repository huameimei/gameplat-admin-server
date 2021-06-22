package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SysDictDataVo implements Serializable {
  private Long Id;
  private String dictName;
  private String dictLabel;
  private String dictValue;
  private Integer dictSort;
  private String cssClass;
  private String listClass;
  private Integer isVisible;
  private Integer isDefault;
  private Integer status;
  private String createBy;
  private Date createTime;
  private String updateBy;
  private Date updateTime;
  private String remark;
}
