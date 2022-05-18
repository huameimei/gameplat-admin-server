package com.gameplat.admin.model.dto;

import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class SpreadUnionDTO implements Serializable {

  @Schema(description = "主键Id")
  private Long id;

  @Schema(description = "联盟名称")
  private String unionName;

  @Schema(description = "代理账号")
  private String agentAccount;

  @Schema(description = "渠道类型")
  private String channel;

  /** 日期 */
  @NotEmpty(message = "开始时间不能为空")
  private String startTime;

  @NotEmpty(message = "结束时间不能为空")
  private String endTime;

  /**
   * 创建人
   */
  private String createBy = SecurityUserHolder.getCredential().getUsername();

  /**
   * 修改人
   */
  private String updateBy = SecurityUserHolder.getCredential().getUsername();

  /**
   * 层级
   */
  private Integer level;
}
