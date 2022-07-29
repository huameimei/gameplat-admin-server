package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 游戏日报表 */
@Data
public class GameBetDailyReportVO implements Serializable {

  /** 主键 */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 账号编号 */
  private Long memberId;

  /** 账号 */
  @Excel(name = "会员账号", width = 20, orderNum = "2")
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

  /** 游戏平台 */
  private String platformCode;

  /** 游戏子类型 */
  private String gameKind;

  @Excel(name = "游戏平台", width = 20, orderNum = "3")
  private String gameKindName;

  /** 游戏大类 */
  private String gameType;

  /** 投注金额 */
  @Excel(name = "投注金额", width = 20, orderNum = "4")
  private BigDecimal betAmount;

  /** 有效投注额 */
  @Excel(name = "有效投注金额", width = 20, orderNum = "5")
  private BigDecimal validAmount;

  /** 中奖金额 */
  @Excel(name = "中奖金额", width = 20, orderNum = "6")
  private BigDecimal winAmount;

  /** 下注笔数 */
  private Long betCount;
  /** 中奖数 */
  private Long winCount;

  /** 统计时间(年月日) */
  @JsonFormat(pattern = "yyyy-MM-dd")
  @Excel(name = "统计日期", width = 20, orderNum = "1")
  private String statTime;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 更新时间 */
  private Date updateTime;
}
