package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysEmailQueryDto implements Serializable {

  /**
   * 标题
   */
  private String title;

  /**
   * 邮箱类型
   */
  private Integer type;

  /**
   * 状态
   */
  private Integer status;

}
