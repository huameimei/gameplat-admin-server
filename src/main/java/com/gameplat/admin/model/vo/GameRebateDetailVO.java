package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class GameRebateDetailVO implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 账号编号
   */
  private Long userId;

  /**
   * 账号
   */
  private String account;

  /**
   * 用户真实姓名
   */
  private String realName;

  /**
   * 代理路径
   */
  private String userPaths;

  /**
   * 添加时间
   */
  private Date createTime;

  /**
   * 返水金额
   */
  private Double rebateMoney;

  /**
   * 实际返水金额
   */
  private Double realRebateMoney;

  /**
   * 配置期号主键
   */
  private Long periodId;

  /**
   * 配置期号名称
   */
  private String periodName;

  /**
   * 期号起始时间
   */
  private Date beginDate;

  /**
   * 期号截止时间
   */

  private Date endDate;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 备注
   */
  private String remark;


  private Double validAmount;

  private String statTime;
}
