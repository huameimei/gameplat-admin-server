package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/6/20 13:48
 * @Version 1.0
 */
@Data
public class YuBaoReportDataVo implements Serializable {


  /**
   * 余额宝收益
   */
  private BigDecimal yuBaoIncome;


  //private
}
