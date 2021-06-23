package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysQuickReplayQueryDto implements Serializable {

  /**
   * 回复信息
   */
  private String message;

}
