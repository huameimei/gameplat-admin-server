package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @description
 * @date 2022/3/8
 */
@Data
public class LoanVO implements Serializable {

    private IPage<MemberLoanVO> memberLoanVO;

    @ApiModelProperty("欠款金额小计")
    private BigDecimal subtotal;

    @ApiModelProperty("欠款金额总计")
    private BigDecimal total;
}
