package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.model.entity.proxy.RebateReportExtend;
import org.apache.ibatis.annotations.Param;

public interface RebateExtendMapper extends BaseMapper<RebateReportExtend> {

  IPage<RebateReportExtend> queryPage(
      PageDTO<RebateReportExtend> page, @Param("reportId") Long reportId);

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
