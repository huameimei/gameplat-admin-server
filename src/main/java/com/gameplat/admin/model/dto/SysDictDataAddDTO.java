package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysDictDataAddDTO extends BaseEntity {

    private String dictType;

    private String dictName;

    private String dictLabel;

    private String dictValue;

    private Integer dictSort;

    private String remark;

    private Integer isVisible;

    private Integer isDefault;
}
