package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description : 会员成长值变动dto @Author : lily @Date : 2021/12/08
 */
@Data
public class MemberGrowthChangeDto {
  @Schema(required = true, description = "会员id")
  private Long userId;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "此次变动成长值")
  private Long changeGrowth;

  @Schema(required = true, description = "类型：0:充值  1: 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
  private Integer type;

  @Schema(description = "变动原因")
  private String remark;

  @Schema(description = "变动游戏分类")
  private String kindCode;

  @Schema(description = "变动游戏分类名称")
  private String kindName;

  @Schema(description = "变动倍数")
  private BigDecimal changeMult;
}
