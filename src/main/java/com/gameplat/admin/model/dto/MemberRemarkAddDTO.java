package com.gameplat.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

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

  @Size(min = 1, max = 100, message = "最少修改{min}个会员，一次修改不能超过{max}个会员")
  private List<Long> ids;

  @Length(max = 255, message = "备注长度不能超过{max}个字符")
  private String remark;
}
