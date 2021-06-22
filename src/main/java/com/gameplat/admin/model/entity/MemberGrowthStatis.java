package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * 会员成长值汇总
 * @author Lenovo
 */
@Data
public class MemberGrowthStatis extends BaseEntity {

  /**
   * 会员id
   */
  private Long memberId;

  /**
   * 会员账号
   */
  private String memberName;

  /**
   * 会员当前等级
   */
  private Integer level;

  /**
   * 会员当前的成长值
   */
  private Integer growth;

  /**
   * 充值累计成长值
   */
  private Integer rechargeGrowth;

  /**
   * 签到累计成长值
   */
  private Integer signGrowth;

  /**
   * 打码量累计成长值
   */
  private Integer dmlGrowth;


  /**
   * 后台修改累计成长值
   */
  private Integer backGrowth;

  /**
   * 完善资料累计成长值
   */
  private Integer infoGrowth;

  /**
   * 绑定银行卡累计成长值
   */
  private  Integer bindGrowth;

  /**
   * 未达到保底累计扣除成长值
   */
  private Integer demoteGrowth;

  /**
   * 创建人
   */
  private String createBy;


  /**
   * 修改人
   */
  private String updateBy;

  /**
   * 备注
   */
  private String remark;

}

