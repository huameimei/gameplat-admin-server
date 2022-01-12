package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsAreaQueryDTO implements Serializable {

    /**
     * 编码
     */
    private String code;
    /**
     * 国家/地区
     */
    private String name;

    /**
     * 状态 0 禁用 1 启用
     */
    private String status;
}
