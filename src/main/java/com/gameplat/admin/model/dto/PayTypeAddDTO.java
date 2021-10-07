package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class PayTypeAddDTO extends BaseEntity {

    private String name;

    private String code;

    private String bankFlag;

    private Integer transferEnabled;

    private Integer onlinePayEnabled;

    private Integer isSysCode;
}
