package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 游戏导航栏VO
 */
@Data
public class GameBarNewVO implements Serializable  {


  /**
   * 主键Id
   */
  private Long id;

  /**
   * 父级Id
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String parentCode;

  /**
   * 导航Code
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String barCode;

  /**
   * 导航名称
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String name;

  /**
   * 导航logo
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String barLogo;

  /**
   *   导航展示端  0 web  1 安卓，IOS  2 h5  3全部
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String clientType;

  /**
   *   导航状态 0 关闭  1 开启"
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer state;

  /**
   * 排序
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer sort;

  /**
   *   0 导航  1 一级游戏  2 二级游戏
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer barType;

  /**
   *   相关动作 0 直接渲染  1 跳一级界面  2 跳二级界面
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer actionType;

  /**
   * 是否适配多场景 1 是  0 否
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer isScenes;

  /**
   *   绑定的游戏大类Code
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String gameType;

  /**
   * 一级游戏Code
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String gameOneCode;

  /**
   * 二级游戏Code
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String gameTwoCode;

  /**
   *   游戏相关配置
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String gameImgConfig;

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Date createTime;

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Date updateTime;

  /**
   * 更新人
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String updateBy;

  /**
   * 游戏集合
   */
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private List<GameBarNewVO> childrenList = new ArrayList<>();
}
