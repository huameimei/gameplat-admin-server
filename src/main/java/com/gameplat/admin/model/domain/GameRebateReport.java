package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 真人游戏返水报表
 */
@Data
@TableName("game_rebate_report")
public class GameRebateReport implements Serializable {

  /**
   * 主键
   */
  @TableId(type = IdType.AUTO)
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

  private String gameType;

  /**
   * 添加时间
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date beginDate;

  /**
   * 期号截止时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  private Integer status;

  private String remark;

  private BigDecimal validAmount;

  private String statTime;

  @TableField(exist = false)
  private String userLevel;
}
