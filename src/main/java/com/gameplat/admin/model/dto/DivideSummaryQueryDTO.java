package com.gameplat.admin.model.dto;

import lombok.Data;

@Data
public class DivideSummaryQueryDTO {
    private Long id;
    private Long periodsId;
    private Long userId;
    private String account;
    private Integer agentLevel;
    private Long parentId;
    private String parentName;
    private Integer status;
    private String startTime;
    private String endTime;
}
