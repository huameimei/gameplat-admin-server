package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lily
 * @description 成长值变动游戏分类名称
 * @date 2021/11/23
 */
@Data
public class RecordKindName {

  @Schema(description = "键")
  private String key;

  @Schema(description = "值")
  private String value;

  @Schema(description = "en-Us")
  private String enUs;

  @Schema(description = "in-ID")
  private String inID;

  @Schema(description = "th-TH")
  private String thTH;

  @Schema(description = "th-TH")
  private String viVN;

  @Schema(description = "zh-CN")
  private String zhCN;
}
