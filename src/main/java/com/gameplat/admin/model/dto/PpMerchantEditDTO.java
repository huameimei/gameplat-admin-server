package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class PpMerchantEditDTO extends BaseEntity {

    private String name;

    private String ppInterfaceCode;

    private Integer status;

    private Long proxyTimes;

    private Long proxyAmount;

    private String parameters;

    private Integer sort;

    private String merLimits;

    private Long maxLimitCash; // 最大金额限制

    private Long minLimitCash; // 最小金额限制

    private String userLever; // 用户层级
}
