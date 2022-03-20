package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/3/8
 */
@Data
public class LoanVO implements Serializable {

  private IPage<MemberLoanVO> page;

  private MemberLoanSumVO memberLoanSumVO;
}
