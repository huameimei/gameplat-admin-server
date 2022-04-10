package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.model.entity.proxy.SalaryPeriods;

/** @Description : 工资期数 @Author : cc @Date : 2022/4/2 */
public interface SalaryPeriodsService extends IService<SalaryPeriods> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<SalaryPeriodsVO> queryPage(PageDTO<SalaryPeriods> page, SalaryPeriodsDTO dto);

  /**
   * 添加
   *
   * @param dto
   */
  void add(SalaryPeriodsDTO dto);

  /**
   * 编辑
   *
   * @param dto
   */
  void edit(SalaryPeriodsDTO dto);

  /**
   * 删除
   *
   * @param ids
   */
  void delete(String ids);

  /**
   * 结算
   *
   * @param id
   */
  void settle(Long id);

  /**
   * 派发
   *
   * @param id
   */
  void grant(Long id);

  /**
   * 回收
   *
   * @param id
   */
  void recycle(Long id);
}
