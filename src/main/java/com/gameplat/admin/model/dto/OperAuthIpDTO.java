package com.gameplat.admin.model.dto;

import com.gameplat.base.common.validator.Patterns;
import com.gameplat.common.group.Groups;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * ip白名单操作类
 *
 * @author three
 */
@Data
public class OperAuthIpDTO {

  /** 序号 */
  @NotNull(message = "不能为空", groups = Groups.UPDATE.class)
  private Long id;

  /** ip类型 */
  @NotEmpty(
      message = "IP类型不能为空",
      groups = {Groups.INSERT.class, Groups.UPDATE.class})
  private String ipType;

  /** ip */
  @NotEmpty(
      message = "IP地址不能为空",
      groups = {Groups.INSERT.class, Groups.UPDATE.class})
  @Pattern(
      regexp = Patterns.REGEX_IP_ADDR,
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "IP地址格式不正确")
  private String ip;

  private String remark;
}
