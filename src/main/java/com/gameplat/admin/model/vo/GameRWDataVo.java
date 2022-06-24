package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/6/22 21:30
 * @Version 1.0
 */
@Data
public class GameRWDataVo implements Serializable {

  /**
   * 账号
   */
  private String account;


  /**
   * 真实姓名
   */
  private String realName;

  /**
   * 会员层级
   */
  private String levelName;

  /**
   * 上级代理
   */
  private String parentName;

  /**
   * 会员等级（代理下的会员等级）
   */
  private String agentLevel;


  /**
   * 账号状态
   */
  private int state;

  /**
   * 返点等级
   */
  private BigDecimal rebate;


  /**
   * 充值金额
   */
  private BigDecimal amount;


  /**
   * 注册时间
   */
  private String regTime;


  /**
   * 是否限制投注
   */
  private Integer limitLotFlag;

}
