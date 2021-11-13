package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 菜单信息DTO
 * @author three
 */
@Data
public class MenuDTO {

  /**
   * 标题
   */
  private String title;

  /**
   * 是否隐藏（0显示1隐藏）
   */
  private Integer visible;

}
