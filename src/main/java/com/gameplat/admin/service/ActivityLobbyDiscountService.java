package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.domain.ActivityLobbyDiscount;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.model.vo.ActivityLobbyDiscountVO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;

import java.util.List;

/**
 * 活动大厅业务查询
 */
public interface ActivityLobbyDiscountService {


    /**
     * 批量保存活动大厅打折数据
     *
     * @param activityLobbyDiscounts
     * @return
     */
    int saveBatch(List<ActivityLobbyDiscount> activityLobbyDiscounts);

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
    void saveBatchLobbyDiscount(List<ActivityLobbyDiscount> activityLobbyDiscountList);

    /**
     * 批量删除
     *
     * @param deleteList
     */
    void deleteBatchLobbyDiscount(List<ActivityLobbyDiscount> deleteList);
}
