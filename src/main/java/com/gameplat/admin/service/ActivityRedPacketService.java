package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ActivityRedPacket;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.model.vo.ActivityRedPacketVO;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;

import java.util.List;

/**
 * 红包雨业务
 */
public interface ActivityRedPacketService extends IService<ActivityRedPacket> {

    /**
     * 查询红包雨列表
     *
     * @param page
     * @param activityRedPacketQueryDTO
     * @return
     */
    IPage<ActivityRedPacketVO> redPacketList(PageDTO<ActivityRedPacket> page, ActivityRedPacketQueryDTO activityRedPacketQueryDTO);

    /**
     * 新增红包雨配置
     *
     * @param activityRedPacketAddDTO
     */
    void add(ActivityRedPacketAddDTO activityRedPacketAddDTO);

    /**
     * 编辑红包雨配置
     *
     * @param activityRedPacketUpdateDTO
     */
    void edit(ActivityRedPacketUpdateDTO activityRedPacketUpdateDTO);

    /**
     * 更新红包雨状态
     *
     * @param packetId
     */
    void updateStatus(Long packetId);

    /**
     * 批量删除
     *
     * @param ids
     */
    void delete(String ids);

    /**
     * 查询优惠列表
     *
     * @param activityRedPacketDiscountDTO
     * @return
     */
    Object discountList(ActivityRedPacketDiscountDTO activityRedPacketDiscountDTO);

    /**
     * 获取红包配置
     *
     * @return
     */
    ActivityRedPacketConfigVO getConfig();

    /**
     * 更新活动配置
     *
     * @param activityRedPacketConfigDTO
     */
    void updateConfig(ActivityRedPacketConfigDTO activityRedPacketConfigDTO);
}
