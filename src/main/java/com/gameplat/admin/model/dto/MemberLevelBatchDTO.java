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
public class MemberLevelBatchDTO implements Serializable {

  @Min(value = 1, message = "至少选择一个会员！")
  List<Long> ids;

  @Length(max = 255, message = "请选择您要转移的层级")
  private Integer level;

}
