package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典类型操作DTO
 *
 * @author three
 */
@Data
public class OperDictTypeDTO implements Serializable {

  @NotNull(groups = Groups.UPDATE.class, message = "ID不能为空")
  private Long dictId;

  /** 字典名称 */
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "字典名称不能为空")
  private String dictName;

  /** 字典类型 */
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "字典类型不能为空")
  private String dictType;

  /** 状态（1正常 0停用） */
  private String status;

  /** 排序 */
  private Integer orderNum;

  private String remark;
}
