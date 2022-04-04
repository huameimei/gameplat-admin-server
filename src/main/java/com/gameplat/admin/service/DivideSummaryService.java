package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.DivideSummaryQueryDTO;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import com.gameplat.model.entity.proxy.DivideSummary;

/** @Description : 分红汇总 @Author : cc @Date : 2022/4/2 */
public interface DivideSummaryService extends IService<DivideSummary> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<DivideSummaryVO> queryPage(PageDTO<DivideSummary> page, DivideSummaryQueryDTO dto);

  /**
   * 获取某期数最大的等级
   *
   * @param dto
   * @return
   */
  Integer getMaxLevel(DivideSummaryQueryDTO dto);
}
