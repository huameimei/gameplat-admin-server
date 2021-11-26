package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.ActivityType;

import java.util.List;

/**
 * 活动类型
 */
public interface ActivityTypeService {

    /**
     * 通过id列表查询活动类型
     *
     * @param activityTypeIdList
     * @return
     */
    List<ActivityType> findByTypeIdList(List<Long> activityTypeIdList);
}
