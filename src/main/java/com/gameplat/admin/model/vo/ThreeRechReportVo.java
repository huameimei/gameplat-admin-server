package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/7 0:34 @Version 1.0
 */
@Data
public class ThreeRechReportVo implements Serializable {

  @Schema(description = "第三方接口编码")
  private String tpInterfaceCode;

  @Schema(description = "第三方接口名称")
  private String tpInterfaceName;

  @Schema(description = "充值金额")
  private BigDecimal amount;
}
