package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.model.entity.proxy.DividePeriods;

/** @Description : 层层代分红期数 @Author : cc @Date : 2022/4/2 */
public interface DividePeriodsService extends IService<DividePeriods> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<DividePeriodsVO> queryPage(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto);

  /**
   * 添加
   *
   * @param dto
   */
  void add(DividePeriodsDTO dto);

  /**
   * 编辑
   *
   * @param dto
   */
  void edit(DividePeriodsDTO dto);

  /**
   * 删除
   *
   * @param ids
   */
  void delete(String ids);

  /**
   * 结算
   *
   * @param dto
   */
  void settle(DividePeriodsDTO dto);

  /**
   * 派发
   *
   * @param dto
   */
  void grant(DividePeriodsDTO dto);

  /**
   * 回收
   *
   * @param dto
   */
  void recycle(DividePeriodsDTO dto);
}
