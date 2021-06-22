package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class MemberVipSignStatis extends BaseEntity {

  /**
   * 会员ID
   */
  private String memberId;

  /**
   * 会员名称
   */
  private String memberName;

  /**
   * 总签到数
   */
  private String signCount;

  /**
   * 七天连续签到次数
   */
  private  Integer continueWeekSign;


  /**
   * 十五天连续签到次数
   */
  private Integer continueHalfMonthSign;


  /**
   * 三十天连续签到次数
   */
  private Integer continueMonthSign;

}
