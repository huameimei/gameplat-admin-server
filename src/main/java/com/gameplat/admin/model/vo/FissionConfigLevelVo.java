package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description : 裂变等级对应分红率
 * @Author : cc
 * @Date : 2022/2/4
 */
@Data
public class FissionConfigLevelVo {
    private Integer level;
    private BigDecimal levelDivideRatio;
}
