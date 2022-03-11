package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏导航栏VO
 */
@Data
public class GameBarVO implements Serializable {


  /**
   * 数据主键
   */
  @TableId(type = IdType.AUTO)
  private Long id;


  /**
   * 导航code
   */
  private String code;

  /**
   * 导航名称
   */
  private String name;

  /**
   * 0 顶级导航
   * 1 一级游戏
   * 2 二级游戏
   */
  private Integer gameType;

  /**
   * 一级游戏Code
   */
  private String gameOneCode;

  /**
   * 二级游戏Code
   */
  private String gameTwoCode;

  /**
   * 排序
   */
  private Integer sort;

  /**
   * PC 图片
   */
  private String pcImg;

  /**
   * 游戏logo
   */
  private String gameLogo;

  /**
   * 是否适配多场景
   */
  private Integer isScenes;

  /**
   * 游戏相关图片配置
   */
  private String gameImgConfig;

  /**
   * 备注信息
   */
  private String remark;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 更新时间
   */
  private Date updateTime;

  /**
   * 更新人
   */
  private String updateBy;
}
