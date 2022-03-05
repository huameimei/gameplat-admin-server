package com.gameplat.admin.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 批量清理会员联系方式
 *
 * @author robben
 */
@Data
public class MemberContactCleanDTO implements Serializable {

  @Min(value = 1, message = "至少包含一种联系方式")
  List<String> fields;
  @Length(max = 255, message = "开始时间不能为空")
  private String startTime;
  @NotEmpty(message = "结束时间不能为空")
  private String endTime;
}
