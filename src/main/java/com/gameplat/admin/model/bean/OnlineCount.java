package com.gameplat.admin.model.bean;

import lombok.Data;

/**
 * 在线设备合计
 *
 * @author three
 */
@Data
public class OnlineCount {

  private int iosCount;

  private int androidCount;

  /** win用户合计 */
  private int windowsCount;

  /** H5 */
  private int h5Count;

  /** 会员合计 */
  private int memberCount;

  /** 试玩会员合计 */
  private int testUserCount;

  /** 推广会员合计 */
  private int promotionCount;

  /** 告警会员合计 */
  private int warningCount;

  /** 其他 */
  private int otherCount;
}
