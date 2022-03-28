package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.model.entity.proxy.SalaryPeriods;

public interface SalaryPeriodsService extends IService<SalaryPeriods> {
  IPage<SalaryPeriodsVO> queryPage(PageDTO<SalaryPeriods> page, SalaryPeriodsDTO dto);

  void add(SalaryPeriodsDTO dto);

  void edit(SalaryPeriodsDTO dto);

  void delete(String ids);

  void settle(Long id);

  void grant(Long id);

  void recycle(Long id);
}
