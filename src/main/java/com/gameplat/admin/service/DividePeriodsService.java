package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.proxy.DividePeriods;
import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.dto.DividePeriodsQueryDTO;
import com.gameplat.admin.model.vo.DividePeriodsVO;

public interface DividePeriodsService extends IService<DividePeriods> {
    IPage<DividePeriodsVO> queryPage(PageDTO<DividePeriods> page, DividePeriodsQueryDTO dto);

    void add(DividePeriodsDTO dto);

    void edit(DividePeriodsDTO dto);

    void delete(String ids);

    void settle(DividePeriodsDTO dto);
}
