package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/** 彩系彩种列表 */
@Data
public class LotteryTypeListVo {

  @Schema(description = "彩系编码", required = true)
  private String groupCode;

  @Schema(description = "彩系名称", required = true)
  private String groupName;

  @Schema(description = "彩种列表", required = true)
  private List<LotteryCodeVo> data;
}
