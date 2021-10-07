package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class PpMerchantVO extends Model<PpMerchantVO> {

    private Long id;

    private String name;

    private String ppInterfaceCode;

    private String parameters;

    private Integer status;

    private Long proxyTimes;

    private Long proxyAmount;

    private String interfaceName;

    private Integer sort;

    private String merLimits;

    private Long maxLimitCash; // 最大金额限制

    private Long minLimitCash; // 最小金额限制

    private String userLever; // 用户层级

    private PpInterfaceVO ppInterfaceVO;
}
