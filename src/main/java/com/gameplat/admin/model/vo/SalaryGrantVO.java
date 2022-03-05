package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryGrantVO {
    private Long id;
    private Long periodsId;
    private Long userId;
    private String account;
    private String userType;
    private Integer agentLevel;
    private Long parentId;
    private String parentName;
    private String superPath;
    private String gameType;
    private BigDecimal rechargeAmount;
    private BigDecimal winAmount;
    private BigDecimal validAmount;
    private Integer validUserNum;
    private BigDecimal salaryAmount;
    private Integer reachStatus;
    private Integer grantStatus;
    private Date createTime;
    private String createBy;
    private String updateBy;
    private Date updateTime;
    private String remark;
}
