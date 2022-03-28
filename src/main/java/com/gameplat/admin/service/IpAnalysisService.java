package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;

public interface IpAnalysisService {

  /** ip分析 */
  IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto);
}
