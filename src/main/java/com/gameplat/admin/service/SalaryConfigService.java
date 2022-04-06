package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.model.entity.proxy.SalaryConfig;

/** @Description : 工资配置 @Author : cc @Date : 2022/4/2 */
public interface SalaryConfigService extends IService<SalaryConfig> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<SalaryConfigVO> queryPage(PageDTO<SalaryConfig> page, SalaryConfigDTO dto);

  /**
   * 获取最大代理层级
   *
   * @return
   */
  Integer getMaxLevel();

  /**
   * 添加
   *
   * @param dto
   */
  void add(SalaryConfigDTO dto);

  /**
   * 编辑
   *
   * @param dto
   */
  void edit(SalaryConfigDTO dto);

  /**
   * 删除
   *
   * @param ids
   */
  void delete(String ids);
}
