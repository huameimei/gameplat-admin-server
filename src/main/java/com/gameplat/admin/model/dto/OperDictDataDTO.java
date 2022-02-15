package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典数据操作DTO
 *
 * @author three
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperDictDataDTO implements Serializable {

  @NotNull(groups = Groups.UPDATE.class, message = "字典ID不能为空")
  private Long id;

  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "字典类型不能为空")
  private String dictType;

  private String dictName;

  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "字典标签不能为空")
  private String dictLabel;

  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "字典值不能为空")
  private String dictValue;

  @NotNull(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "字典排序不能为空")
  private Integer dictSort;

  private Integer status;

  private String remark;

  private Integer isVisible;

  private Character isDefault;
}
