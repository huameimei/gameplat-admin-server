package com.gameplat.admin.model.entity;

import java.util.Date;
import lombok.Data;

@Data
public class YuBaoInterest {

  /**
   * 主键
   */
  private Long id;

  /**
   * 会员账号
   */
  private String userAccount;

  /**
   * 用户真实姓名
   */
  private String fullName;

  /**
   * 收益日期
   */
  private Date date;


  /**
   * 余额宝金额
   */
  private Long yubaoMoney;

  /**
   * 收益率
   */
  private String   interestRate;

  /**
   * 收益金额
   */
  private  Long interestMoney;

  /**
   * 结算时间
   */
  private Date settleTime;


}
