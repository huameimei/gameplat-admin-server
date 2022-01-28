package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 批量修改会员备注
 *
 * @author robben
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRemarkAddDTO implements Serializable {

  @NotNull(message = "会员ID不能为空")
  private Long id;

  @Max(value = 500, message = "一次修改不能超过500个会员")
  private List<Long> ids;

  @Length(max = 255, message = "备注长度不能超过255个字符")
  private String remark;
}
