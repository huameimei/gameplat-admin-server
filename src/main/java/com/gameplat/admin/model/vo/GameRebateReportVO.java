package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class GameRebateReportVO implements Serializable {
  private Long id;

  /**
   * 账号编号
   */
  private Long memberId;

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
   * 游戏类型
   */
  private String platformCode;

  /**
   * 游戏子类型
   */
  private String gameKind;

  private String firstKind;

  /**
   * 添加时间
   */
  private Date createTime;

  /**
   * 返水金额
   */
  private BigDecimal rebateMoney;

  /**
   * 实际返水金额
   */
  private BigDecimal realRebateMoney;

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

  private Integer status;

  private String remark;

  private BigDecimal validAmount;

  private String statTime;

  private String userLevel;
}
