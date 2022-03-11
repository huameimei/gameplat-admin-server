package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 红包记录
 */
@Data
public class UserRedEnvelopeVO {

  /**
   * 记录id
   */
  private Long  id;

  /**
   * 会员ID
   */
  private Long userId;

  /**
   * 会员名称
   */
  private String userName;

  /**
   * 来源Id 1 活动派发 2 充值红包 3 普通注册红包  4 代理注册红包  5 随机红包
   */
  private String sourceId;

  /**
   * 领取状态  0失效 1未领取 2已领取 3已回收
   */
  private Integer status;

  /**
   * 时间  根据状态判别
   */
  private Date statusTime;

  /**
   * 备注信息
   */
  private String remark;

  /**
   * 红包金额
   */
  private BigDecimal amount;

  /**
   * 打码倍数
   */
  private Double multiple;

  /**
   * 领取方式 1 手动领取  2 自动到账
   */
  private Integer receiveMethod;
}
