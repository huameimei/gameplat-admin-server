package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.vo.SalaryGrantVO;
import com.gameplat.model.entity.proxy.SalaryGrant;

import java.math.BigDecimal;

/** @Description : 工资派发 @Author : cc @Date : 2022/4/2 */
public interface SalaryGrantService extends IService<SalaryGrant> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<SalaryGrantVO> queryPage(PageDTO<SalaryGrant> page, SalaryGrantDTO dto);

  /**
   * 工资金额调整
   *
   * @param id
   * @param salaryAmount
   */
  void change(Long id, BigDecimal salaryAmount);
}
