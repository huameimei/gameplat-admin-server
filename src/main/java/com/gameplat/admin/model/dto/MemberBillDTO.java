package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lily
 * @description 查询现金流水入参
 * @date 2021/12/2
 */
@Data
public class MemberBillDTO implements Serializable {

  @Schema(description = "账号")
  private String account;

  @Schema(description = "代理账号")
  private String superPath;

  @Schema(description = "订单号，关联其他业务订单号")
  private String orderNo;

  @Schema(description = "账变类型：TranTypes中值")
  private List<Integer> tranTypes;

  @Schema(description = "添加开始时间")
  private String beginTime;

  @Schema(description = "添加结束时间")
  private String endTime;
}
