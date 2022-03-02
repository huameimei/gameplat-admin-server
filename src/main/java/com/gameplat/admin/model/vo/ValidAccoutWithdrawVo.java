package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 提现限制说明 每笔充值都要对应个打码量,只有会员有效打码量达到才可以提现 条件：首先必须先达到最近一笔的打码量,在次总打码量必须达到,两个条件必须满足 */
@Data
public class ValidAccoutWithdrawVo implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;
  /** 会员ID */
  private Long memberId;
  /** 会员账号 */
  private String account;
  /** 打码量要求 */
  private BigDecimal dmlClaim;
  /** 开始时间 */
  @Field(
      type = FieldType.Date,
      format = DateFormat.custom,
      pattern = "yyyy-MM-dd HH:mm:ss||epoch_second")
  private String createTime;

  /** 结束时间 */
  @Field(
      type = FieldType.Date,
      format = DateFormat.custom,
      pattern = "yyyy-MM-dd HH:mm:ss||epoch_second")
  private String endTime;

  /** 游戏编码 */
  private String gameKind;

  /** 统计时间 */
  private Date statTime;

  /** 平台编码 */
  private String platformCode;

  /** 充值金额 */
  private BigDecimal rechMoney;

  /** 游戏名 */
  private String gameName;

  /** 投注内容 */
  private String betContext;

  /** 总投注金额 */
  private BigDecimal vaildAmount;
}
