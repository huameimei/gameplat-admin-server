package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailDTO implements Serializable {

    private String title;
    private Integer status;
    private Integer emailType;
    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;
}
