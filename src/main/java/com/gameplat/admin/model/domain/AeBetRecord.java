package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/** AE注单记录 */
@Data
@TableName("game_bet_record_ae")
public class AeBetRecord {

  @TableId(type = IdType.AUTO)
  private Long id;

  /** 唯一编码 */
  private String billNo;

  /** 用户名 */
  private String account;

  /** 输赢金额 */
  private String winAmount;

  /** 投注金额 */
  private String betAmount;

  /** 有效投注额 */
  private String validAmount;

  /** 租户编码 */
  private String tenant;

  /** 局号 */
  private String roundId;

  /** 币种 */
  private String currency;

  /** 游戏编码 */
  private String gameCode;

  /** 平台编码 */
  private String platformCode;

  /** 游戏分类 */
  private String gameKind;

  /** 投注内容 */
  private String betContent;

  /** 投注时间 */
  private Date betTime;

  /** 下注北京时间 */
  private Date cstBetTime;

  /** 报表统计时间(美东) */
  private Date statTime;

  /** 结算时间 */
  private Date settleTime;

  /** 下注美东时间 */
  private Date amesTime;

  /** 返水结算时间(美东) */
  private Date rebateTime;

  /** 添加时间 */
  private Date createTime;

  /** 更新时间 */
  private Date updateTime;
}
