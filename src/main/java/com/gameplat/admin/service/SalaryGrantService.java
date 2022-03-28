package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.vo.SalaryGrantVO;
import com.gameplat.model.entity.proxy.SalaryGrant;

import java.math.BigDecimal;

public interface SalaryGrantService extends IService<SalaryGrant> {
  IPage<SalaryGrantVO> queryPage(PageDTO<SalaryGrant> page, SalaryGrantDTO dto);

  void change(Long id, BigDecimal salaryAmount);
}
