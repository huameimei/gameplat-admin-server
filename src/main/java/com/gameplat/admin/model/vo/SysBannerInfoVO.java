package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;

public class SysBannerInfoVO implements Serializable {
  /** banner类型 1--活动优惠 2--代理加盟 3--虚拟币充值 4--无跳转 5--配置跳转页 6--游戏分类 */
  private Integer bannerType;

  /** pc端图片地址 */
  private String pcPicUrl;

  /** app端图片地址 */
  private String appPicUrl;

  /** 状态（0禁用 1启用） */
  private Integer status;

  private Date createTime;

  private Long createBy;

  private Date updateTime;

  private Long updateBy;
}
