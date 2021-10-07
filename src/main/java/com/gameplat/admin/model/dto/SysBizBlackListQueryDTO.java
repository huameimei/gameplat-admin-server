package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysBizBlackListQueryDTO implements Serializable {
    private Integer userLevel;
    private String account;
    private Integer accountUserLevel;
    private Integer status;
}
