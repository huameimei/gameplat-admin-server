package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("game")
public class Game implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 游戏编码
   */
  private String gameCode;

  /**
   * 游戏中文名
   */
  private String chineseName;

  /**
   * 游戏英文名
   */
  private String englishName;

  /**
   * 图片名称
   */
  private String imageName;

  /**
   * 平台编码
   */
  private String platformCode;

  /**
   * 游戏类型
   */
  private String gameType;

  /**
   * 支持手机端（0：支持，1：不支持）
   */
  private Integer isH5;

  /**
   * 支持电脑端（0:支持，1：不支持）
   */
  private Integer isFlash;

  /**
   * 手机端图片名称
   */
  private String h5ImageName;


  /**
   * 游戏排序
   */
  private Integer sort;
}
