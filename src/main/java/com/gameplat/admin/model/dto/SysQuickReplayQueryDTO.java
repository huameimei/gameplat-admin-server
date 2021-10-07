package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysQuickReplayQueryDTO implements Serializable {

    /**
     * 回复信息
     */
    private String message;
}
