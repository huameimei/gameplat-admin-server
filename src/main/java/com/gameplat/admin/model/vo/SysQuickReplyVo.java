package com.gameplat.admin.model.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysQuickReplyVo implements Serializable {

  private Long id;
  /**
   * 回复信息
   */
  private String  message;

  /**
   * 回复信息类型 1-入款 2-出款
   */
  private Integer messageType;

  /**
   *状态 关闭0、开启1
   */
  private Integer messageStatus;

}
