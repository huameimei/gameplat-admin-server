package com.gameplat.admin.model.doc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChatConfig {

    @ApiModelProperty(value = "聊天室浮窗图片")
    private String app_float_image;

    @ApiModelProperty(value = "聊天室浮窗名称")
    private String app_float_name;

    @ApiModelProperty(value = "聊天室浮窗链接")
    private String app_float_link;

    @ApiModelProperty(value = "聊天室禁言提示语")
    private String app_gag_title;

    @ApiModelProperty(value = "抢红包是否显示金额 1是0否")
    private Boolean showHbMoney;

    @ApiModelProperty(value = "抢红包是否显示记录 1是0否")
    private Boolean showHbHistory;

    @ApiModelProperty(value = "聊天室端开关 1是0否")
    private Boolean showAttention;
}
