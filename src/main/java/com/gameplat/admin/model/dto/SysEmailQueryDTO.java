package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysEmailQueryDTO implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 邮箱类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;
}
