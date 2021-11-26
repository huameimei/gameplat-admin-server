package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Activity;
import com.gameplat.admin.model.dto.ActivityDTO;
import com.gameplat.admin.model.vo.ActivityVO;

/**
 * 活动业务类
 */
public interface ActivityService {


    /**
     * 活动列表分页
     *
     * @param page
     * @param activityDTO
     * @return
     */
    IPage<ActivityVO> list(PageDTO<Activity> page, ActivityDTO activityDTO);
}
