package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import com.gameplat.common.model.dto.BaseDTO;
import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.security.acl.Group;

/**
 * 字典数据操作DTO
 *
 * @author three
 */
@Data
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

  private Integer isDefault;
}
