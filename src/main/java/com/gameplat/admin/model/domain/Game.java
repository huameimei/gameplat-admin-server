package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

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
   * 玩游戏编码
   */
  private String playCode;

  /**
   * 游戏名
   */
  private String gameName;

  /**
   * 平台编码
   */
  private String platformCode;

  /**
   * 游戏大类
   */
  private String gameType;

  /**
   * 游戏类别
   */
  private String gameKind;

  /**
   * 支持手机端（0：支持，1：不支持）
   */
  private Integer isH5;

  /**
   * 支持电脑端（0:支持，1：不支持）
   */
  private Integer isPc;

  /**
   * 游戏排序
   */
  private Integer sort;

  /**
   * 图片地址
   */
  private String imgUrl;

  /**
   * 更新时间
   */
  private Date updateTime;

  /**
   * 更新者
   */
  private String updateBy;
}
