package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 批量修改会员备注
 *
 * @author robben
 */
@Data
public class MemberRemarkAddDTO implements Serializable {

  @NotNull(message = "会员ID不能为空")
  @Max(value = 500, message = "一次修改不能超过500个会员")
  private List<Long> ids;

  @Length(max = 255, message = "备注长度不能超过255个字符")
  private String remark;
}
