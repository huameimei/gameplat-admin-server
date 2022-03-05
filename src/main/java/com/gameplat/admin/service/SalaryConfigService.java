package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.model.entity.proxy.SalaryConfig;

public interface SalaryConfigService extends IService<SalaryConfig> {
    IPage<SalaryConfigVO> queryPage(PageDTO<SalaryConfig> page, SalaryConfigDTO dto);

    Integer getMaxLevel();

    void add(SalaryConfigDTO dto);

    void edit(SalaryConfigDTO dto);

    void delete(String ids);
}
