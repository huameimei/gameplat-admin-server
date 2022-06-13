package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** 实时支付排行榜初始化搜索条件参数 @Author zak @Date 2022/01/18 19:41:05 */
@Data
public class PayLeaderboardSearch {
  @Schema(description = "三方支付接口列表[{interfaceName:xxx,interfaceCode:xxx}]")
  private List<Map<String, String>> interfaceList;

  @Schema(description = "支付类型列表[{payTypeName:xxx,payTypeCode:xxx}]")
  private List<Map<String, String>> payTypeList;
}
