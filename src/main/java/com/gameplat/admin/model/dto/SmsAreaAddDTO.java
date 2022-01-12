package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class SmsAreaAddDTO implements Serializable {

    /**
     * 编码
     */
    @NotEmpty(message = "区号编码不能为空")
    private String code;
    /**
     * 国家/地区
     */
    @NotEmpty(message = "国家名称不能为空")
    private String name;

    /**
     * 状态 0 禁用 1 启用
     */
    @NotEmpty(message = "状态不能为空")
    private String status;
}
