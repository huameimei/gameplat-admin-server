package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.DivideDetailQueryDTO;
import com.gameplat.admin.model.vo.DivideDetailVO;
import com.gameplat.model.entity.proxy.DivideDetail;

public interface DivideDetailService extends IService<DivideDetail> {
  /**
   * 分红详情分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<DivideDetailVO> queryPage(PageDTO<DivideDetail> page, DivideDetailQueryDTO dto);
}
