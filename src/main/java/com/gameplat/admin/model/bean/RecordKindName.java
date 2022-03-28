package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lily
 * @description 成长值变动游戏分类名称
 * @date 2021/11/23
 */
@Data
public class RecordKindName {

  @ApiModelProperty("键")
  private String key;

  @ApiModelProperty("值")
  private String value;

  @ApiModelProperty("en-Us")
  private String enUs;

  @ApiModelProperty("in-ID")
  private String inID;

  @ApiModelProperty("th-TH")
  private String thTH;

  @ApiModelProperty("th-TH")
  private String viVN;

  @ApiModelProperty("zh-CN")
  private String zhCN;
}
