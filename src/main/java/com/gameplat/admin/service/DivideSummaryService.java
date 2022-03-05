package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.proxy.DivideSummary;
import com.gameplat.admin.model.dto.DivideSummaryQueryDTO;
import com.gameplat.admin.model.vo.DivideSummaryVO;

public interface DivideSummaryService extends IService<DivideSummary> {
    IPage<DivideSummaryVO> queryPage(PageDTO<DivideSummary> page, DivideSummaryQueryDTO dto);

    Integer getMaxLevel(DivideSummaryQueryDTO dto);
}
