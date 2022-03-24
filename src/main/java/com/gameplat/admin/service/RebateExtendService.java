package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.proxy.RebateReportExtend;

public interface RebateExtendService extends IService<RebateReportExtend> {
  IPage<RebateReportExtend> queryPage(PageDTO<RebateReportExtend> page, Long reportId);

  /**
   * 新增佣金调整记录
   *
   * @param reportExtendPO
   * @return
   */
  int addReportExtend(RebateReportExtend reportExtendPO);

  /**
   * 编辑佣金调整记录
   *
   * @param reportExtendPO
   * @return
   */
  int editReportExtend(RebateReportExtend reportExtendPO);

  /**
   * 删除佣金调整记录
   *
   * @param reportExtendPO
   * @return
   */
  int removeReportExtend(RebateReportExtend reportExtendPO);
}
