package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class PayAccountQueryDTO extends BaseEntity {

    private String account;

    private String payType;

    private List<String> userLevelList;

    private Integer status;
}
