package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class LiveTransferRecordVO implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 订单编号
   */
  private String orderNo;

  /**
   * 会员ID
   */
  private Long memberId;

  /**
   * 会员账号
   */
  private String account;


  /**
   * 转账金额
   */
  private BigDecimal amount;

  /**
   * 转账类型：1转入，2转出
   */
  private Integer transferType;


  /**
   * 1平台转出成功真人转入失败,
   * 2真人转出成功平台转入失败，
   * 3受理成功,
   * 4取消
   */
  private Integer status;


  /**
   * 真人平台编码
   */
  private String liveCode;


  /**
   * 备注
   */
  private String remark;

  /**
   * 余额
   */
  private BigDecimal balance;

  /**
   * 类型:0:手动转换  1:自动转换
   */
  private Integer transferStatus;


  private String createBy;

  private Date createTime;

}
