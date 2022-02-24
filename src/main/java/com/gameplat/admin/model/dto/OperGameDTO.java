package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class OperGameDTO implements Serializable {

  private Long id;

  private String gameCode;

  private String gameName;

  private String pcImgUrl;

  private String appImgUrl;

  private String platformCode;

  private String gameType;

  private Integer isH5;

  private Integer isFlash;

  private Integer sort;

  /**
   * 是否开放(0：否；1:是)
   */
  private Integer enable;

  /**
   * 是否热门(0：否；1:是)
   */
  private Integer hot;

  /**
   * 是否外跳(0：否；1:是)
   */
  private Integer isJump;

  /**
   * 是否竖屏(0：否；1:是)
   */
  private Integer isVertical;

}
