package com.gameplat.admin.model.vo;

import com.gameplat.model.entity.setting.SysSetting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author martin
 * @description
 * @date 2022/03/10
 */
@Data
public class SysSettingVO extends SysSetting {

  /** 游戏参数 */
  private String gameParameters;

  /** 主题 */
  private String theme;

  /** kindID */
  private String kindId;

  /** 游戏code */
  private String code;

  private String country;

  @Schema(description = "维护状态 0正常 1维护中")
  private Integer status;

  private String zhCn;

  private String enUs;

  private String thTh;
  private String viVn;
  private String inId;
}
