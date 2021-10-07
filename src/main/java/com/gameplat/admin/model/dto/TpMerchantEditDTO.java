package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class TpMerchantEditDTO extends BaseEntity {

    private String name;

    private Integer status;

    private Long rechargeTimes;

    private Long rechargeAmount;

    private String parameters;

    private String payTypes;
}
