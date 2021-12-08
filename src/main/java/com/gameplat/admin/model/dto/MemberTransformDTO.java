package com.gameplat.admin.model.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 批量转代理
 *
 * @author robben
 */
@Data
public class MemberTransformDTO implements Serializable {

  @NotEmpty(message = "缺少流水号")
  private String serialNo;

  @NotEmpty(message = "目标代理账号不能为空")
  private String agentAccount;

  @NotNull(message = "会员ID不能为空")
  private Long id;

  /** 不包含本身 */
  private Boolean excludeSelf;
}
