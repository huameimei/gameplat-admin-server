package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysBizBlacklistUpdateDTO implements Serializable {
    private Long id;
    private Integer targetType;
    private String target;
    private String types;
    private String remark;
    private Integer status;
}
