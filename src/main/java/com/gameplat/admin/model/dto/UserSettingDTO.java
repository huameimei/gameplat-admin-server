package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户个性DTO
 *
 * @author three
 */
@Data
public class UserSettingDTO {

  /** 用户页 */
  private String indexUrl;

  /** 用户默认页 */
  @NotNull(message = "缺少用户默认页参数")
  private Integer defaultPageSize;

  /** 充值订单排序 */
  @NotNull(message = "缺少充值订单排序参数")
  private Integer receiptOrder;

  /** 提现订单排序 */
  @NotNull(message = "缺少提现订单排序参数")
  private Integer withdrawOrder;

  /** 金额千分符 */
  @NotNull(message = "缺少金额千分符参数")
  private Integer thousandsSeparator;

  /** 金额精度 */
  @NotNull(message = "缺少金额精度参数")
  private Integer fractionCount;

  /** 切换导航时自动打开菜单 */
  @NotNull(message = "缺少切换导航时自动打开菜单参数")
  private Integer openDefaultNavMenu;

  /** 用户黑名单 */
  private String specialMemberWarn;

  /** 用户主题色 */
  private String themeColor;

  /** 开启 Tags-View */
  private Boolean tagsView;

  /** 侧边栏 Logo */
  private Boolean sidebarLogo;

  /** 时区 */
  private String timezone;

  /** 侧边栏伸缩 */
  private Boolean sidebarShow;
}
