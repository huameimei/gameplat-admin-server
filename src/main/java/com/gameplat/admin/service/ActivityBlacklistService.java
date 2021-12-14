package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ActivityBlacklist;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistDTO;
import com.gameplat.admin.model.vo.ActivityBlacklistVO;

/**
 * 活动黑名单业务处理
 * @author kenvin
 */
public interface ActivityBlacklistService extends IService<ActivityBlacklist> {
    /**
     * 活动黑名单列表
     *
     * @param page
     * @param activityBlacklistDTO
     * @return
     */
    IPage<ActivityBlacklistVO> list(PageDTO<ActivityBlacklist> page, ActivityBlacklistDTO activityBlacklistDTO);

    /**
     * 新增活动黑名单
     *
     * @param activityBlacklistAddDTO
     */
    boolean add(ActivityBlacklistAddDTO activityBlacklistAddDTO);

    /**
     * 删除活动黑名单
     *
     * @param ids
     */
    void remove(String ids);
}
