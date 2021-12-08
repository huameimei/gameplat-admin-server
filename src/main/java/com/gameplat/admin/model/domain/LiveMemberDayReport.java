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
 * 真人日报表
 */
@Data
@TableName("live_member_day_report")
public class LiveMemberDayReport implements Serializable {


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
   * 游戏平台
   */
  private String gameCode;

  /**
   * 游戏子类型
   */
  private String gameKind;

  private String firstKind;

  /**
   * 年月日
   */
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date statTime;

   /**
   * 添加时间
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 投注金额
   */
  private BigDecimal betAmount;

  /**
   * 有效投注额
   */
  private BigDecimal validAmount;

  /**
   * 水钱
   */
  private BigDecimal revenue;

  /**
   * 中奖金额
   */
  private Double winAmount;

  /**
   * 下注笔数
   */
  private Long betCount;
  /**
   * 中奖数
   */
  private Long winCount;
}
