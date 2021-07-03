package com.gameplat.admin.enums;

public enum DictDataEnum {
  defaultUserLevel(DictTypeEnum.SYSTEM_CONFIG.getValue(), "default_user_level", "会员默认层级"),
  defaultJsonFileDir(DictTypeEnum.SYSTEM_CONFIG.getValue(), "default_json", "默认字典文件保存目录"),
  tokenExpiredTime(DictTypeEnum.SYSTEM_CONFIG.getValue(), "token_expired_time", "Token有效期时长");

  private String type;
  private String label;
  private String remark;

  DictDataEnum(String type, String label, String remark) {
    this(type, label);
    this.remark = remark;
  }

  DictDataEnum(String type, String label) {
    this.type = type;
    this.label = label;
  }

  public String getType() {
    return type;
  }

  public String getLabel() {
    return label;
  }

  public String getRemark() {
    return remark;
  }
}
