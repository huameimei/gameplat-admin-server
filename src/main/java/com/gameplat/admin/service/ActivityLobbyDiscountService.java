package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.ActivityLobbyDiscount;
import com.gameplat.admin.model.vo.ActivityLobbyDiscountVO;
import java.util.List;

/**
 * 活动大厅业务查询
 */
public interface ActivityLobbyDiscountService {


    /**
     * 通过大厅id查询优惠信息列表
     *
     * @param activityLobbyId
     * @return
     */
    List<ActivityLobbyDiscountVO> listByActivityLobbyId(Long activityLobbyId);

    /**
     * 批量更新
     *
     * @param activityLobbyDiscountList
     */
    void updateBatchLobbyDiscount(List<ActivityLobbyDiscount> activityLobbyDiscountList);

    /**
     * 批量保存
     *
     * @param activityLobbyDiscountList
     */
    boolean saveBatchLobbyDiscount(List<ActivityLobbyDiscount> activityLobbyDiscountList);

    /**
     * 批量删除
     *
     * @param deleteList
     */
    void deleteBatchLobbyDiscount(List<ActivityLobbyDiscount> deleteList);
}
