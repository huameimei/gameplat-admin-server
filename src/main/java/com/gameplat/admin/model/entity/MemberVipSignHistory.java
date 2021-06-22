package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import java.util.Date;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class MemberVipSignHistory extends BaseEntity {

  /** 签到人id */
  private Long memberId;

  /** 签到人名称 */
  private String memberName;

  /** 签到时间 */
  private Date signTime;

  /** 签到ip */
  private String signIp;

  /** 创建人 */
  private String createBy;

  /** 修改人 */
  private String updateBy;

  /** 备注 */
  private String remark;
}
