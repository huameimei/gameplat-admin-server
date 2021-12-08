package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 快捷回复配置DTO
 *
 * @author three
 */
@Data
public class QuickReplyDTO {

  /** 编号 */
  @NotNull(groups = Groups.UPDATE.class, message = "编号不能为空")
  private Long id;

  /** 回复信息 */
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "恢复内容不能为空")
  private String message;

  /** 消息类型 */
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "消息类型不能为空")
  private String messageType;

  /** 回复信息模糊模糊匹配 */
  private Integer messageFuzzy;
}
