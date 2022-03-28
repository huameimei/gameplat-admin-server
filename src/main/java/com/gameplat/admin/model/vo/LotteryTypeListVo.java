package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/** 彩系彩种列表 */
@Data
public class LotteryTypeListVo {

  @ApiModelProperty(value = "彩系编码", required = true)
  private String groupCode;

  @ApiModelProperty(value = "彩系名称", required = true)
  private String groupName;

  @ApiModelProperty(value = "彩种列表", required = true)
  private List<LotteryCodeVo> data;
}
