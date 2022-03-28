package com.gameplat.admin.enums;

/**
 * 会员福利领取记录
 *
 * @author admin
 */
public class MemberWealRewordEnums {

  /** 福利状态 */
  public enum MemberWealRewordStatus {
    PENDING_AUDIT(0, "待审核"),
    UNACCALIMED(1, "未领取"),
    COMPLETED(2, "已完成"),
    EXPIRED(3, "已失效"),
    ;

    private int value;
    private String desc;

    MemberWealRewordStatus(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return this.value;
    }

    public String getDesc(int value) {
      for (MemberWealRewordStatus status : values()) {
        if (status.value == value) {
          return status.desc;
        }
      }
      return null;
    }
  }
}
