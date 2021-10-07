package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysSmsQueryDTO implements Serializable {

    private String phone;

    private Integer smsType;

    private Integer status;
}
