package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class GameTypeVO implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 游戏大类编码
   */
  private String gameTypeCode;

  /**
   * 游戏大类名称
   */
  private String gameTypeName;

  /**
   * 状态 1.开启 0.关闭
   */
  private Integer status;


  /**
   * 更新者
   */
  private String updateBy;

  /**
   * 更新时间
   */
  private Date updateTime;


}
