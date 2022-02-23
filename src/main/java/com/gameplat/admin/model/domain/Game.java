package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
   * 是否开放(0：否；1:是)
   */
  private Integer enable;

  /**
   * 是否热门(0：否；1:是)
   */
  private Integer hot;

  /**
   * 支持手机端（0：支持，1：不支持）
   */
  private Integer isH5;

  /**
   * 支持电脑端（0:支持，1：不支持）
   */
  private Integer isPc;

  /**
   * 是否外跳(0：否；1:是)
   */
  private Integer isJump;

  /**
   * 是否竖屏(0：否；1:是)
   */
  private Integer isVertical;

  /**
   * 游戏排序
   */
  private Integer sort;

  /**
   * APP图片地址
   */
  private String appImgUrl;

  /**
   * PC图片地址
   */
  private String pcImgUrl;

  /**
   * 更新时间
   */
  private Date updateTime;

  /**
   * 更新者
   */
  private String updateBy;
}
