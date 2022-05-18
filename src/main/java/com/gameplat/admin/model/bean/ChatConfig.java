package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChatConfig {

  @Schema(description = "聊天室浮窗图片")
  private String app_float_image;

  @Schema(description = "聊天室浮窗名称")
  private String app_float_name;

  @Schema(description = "聊天室浮窗链接")
  private String app_float_link;

  @Schema(description = "聊天室禁言提示语")
  private String app_gag_title;

  @Schema(description = "抢红包是否显示金额 1是0否")
  private Boolean showHbMoney;

  @Schema(description = "抢红包是否显示记录 1是0否")
  private Boolean showHbHistory;

  @Schema(description = "聊天室端开关 1是0否")
  private Boolean showAttention;
}
