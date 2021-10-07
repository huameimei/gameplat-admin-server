package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class TpMerchantAddDTO extends BaseEntity {

    private String name;

    private String tpInterfaceCode;

    private Integer status;

    private Long rechargeTimes;

    private Long rechargeAmount;

    private String parameters;

    private String payTypes;
}
