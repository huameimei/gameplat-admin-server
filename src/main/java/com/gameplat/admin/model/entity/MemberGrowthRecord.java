package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * 成长值记录
 * @author Lenovo
 */
@Data
public class MemberGrowthRecord extends BaseEntity {

  /** 会员id */
  private Long memberId;

  /** 会员账号 */
  private String memberName;

  /** 变动游戏分类编码 */
  private String kindCode;

  /** 变动游戏分类名称 */
  private String kindName;

  /** 类型：0:充值 1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡 */
  private Integer type;

  /** 变动前的等级 */
  private Integer oldLevel;

  /** 变动后的等级 */
  private Integer currentLevel;

  /** 变动倍数 */
  private Double changeMult;

  /** 变动的成长值 */
  private Integer changeGrowth;

  /** 变动前的成长值 */
  private Integer oldGrowth;

  /** 变动后的成长值 */
  private Integer currentGrowth;

  /** 备注 */
  private String remark;
}
