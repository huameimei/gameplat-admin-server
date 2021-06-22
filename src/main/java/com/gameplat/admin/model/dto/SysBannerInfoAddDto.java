package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * Banner信息
 */
@Data
public class SysBannerInfoAddDto extends BaseEntity {

  /**
   * banner类型
   * 1--活动优惠
   * 2--代理加盟
   * 3--虚拟币充值
   * 4--无跳转
   * 5--配置跳转页
   * 6--游戏分类
   */
  private Integer bannerType;


  /**
   * 子类型名称
   */
  private String childName;


  /**
   * Banner子类型
   */
  private String childType;


  /**
   * pc端图片地址
   */
  private String pcPicUrl;


  /**
   * app端图片地址
   */
  private String appPicUrl;

  /**
   * 状态（0禁用 1启用）
   */
  private Integer status;

  /**
   * 语种
   */
  private String  language;

}
