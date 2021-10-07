package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysBizBlackListQueryDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistAddDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistUpdateDTO;
import com.gameplat.admin.model.entity.SysBizBlacklist;
import com.gameplat.admin.model.vo.SysBizBlacklistVO;

/**
 * 业务黑名单
 */
public interface SysBizBlacklistService extends IService<SysBizBlacklist> {

    IPage<SysBizBlacklistVO> queryPage(
            Page<SysBizBlacklist> sysBizBlacklist, SysBizBlackListQueryDTO queryDTO);

    void save(SysBizBlacklistAddDTO sysBizBlacklistAddDTO);

    void delete(Long id);

    void update(SysBizBlacklistUpdateDTO sysBizBlacklistUpdateDTO);
}
