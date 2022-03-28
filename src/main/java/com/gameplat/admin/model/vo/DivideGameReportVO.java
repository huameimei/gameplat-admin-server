package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DivideGameReportVO {
  /** 主键 */
  private Long id;

  /** 账号编号 */
  private Long memberId;

  /** 账号 */
  private String account;

  /** 用户真实姓名 */
  private String realName;

  /** 上级ID */
  private Long superId;

  /** 上级名称 */
  private String superAccount;

  /** 代理路径 */
  private String userPaths;

  /** 用户类型 */
  private String userType;

  private Integer agentLevel;

  /** 游戏平台 */
  private String platformCode;

  /** 游戏子类型 */
  private String gameKind;

  private String gameKindName;

  /** 游戏大类 */
  private String gameType;

  private String gameTypeName;

  /** 投注金额 */
  private BigDecimal betAmount;

  /** 有效投注额 */
  private BigDecimal validAmount;

  /** 中奖金额 */
  private BigDecimal winAmount;

  /** 下注笔数 */
  private Long betCount;
  /** 中奖数 */
  private Long winCount;

  /** 统计时间(年月日) */
  @JsonFormat(pattern = "yyyy-MM-dd")
  private String statTime;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 更新时间 */
  private Date updateTime;
}
