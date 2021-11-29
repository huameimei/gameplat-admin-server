package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.ActivityDistribute;

import java.util.List;

public interface ActivityDistributeService {

    /**
     * 按条件查询
     *
     * @param activityDistribute
     * @return
     */
    List<ActivityDistribute> findActivityDistributeList(ActivityDistribute activityDistribute);

    /**
     * 根据id进行删除
     *
     * @param ids
     */
    void deleteByLobbyIds(String ids);

    /**
     * 批量保存
     *
     * @param activityDistributeList
     * @return
     */
    boolean saveBatch(List<ActivityDistribute> activityDistributeList);
}
