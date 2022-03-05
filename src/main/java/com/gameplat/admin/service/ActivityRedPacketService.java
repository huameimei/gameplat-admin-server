package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.model.vo.ActivityRedPacketVO;
import com.gameplat.admin.model.vo.ActivityTurntablePrizeConfigVO;
import com.gameplat.model.entity.activity.ActivityRedPacket;

import java.util.List;

/** 红包雨业务 */
public interface ActivityRedPacketService extends IService<ActivityRedPacket> {

  /**
   * 查询红包雨列表
   *
   * @param page
   * @param dto
   * @return
   */
  IPage<ActivityRedPacketVO> redPacketList(
      PageDTO<ActivityRedPacket> page, ActivityRedPacketQueryDTO dto);

  /**
   * 新增红包雨配置
   *
   * @param activityRedPacketAddDTO
   */
  void add(ActivityRedPacketAddDTO activityRedPacketAddDTO);

  /**
   * 编辑红包雨配置
   *
   * @param dto
   */
  void edit(ActivityRedPacketUpdateDTO dto);

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
   * @param dto
   * @return
   */
  Object discountList(ActivityRedPacketDiscountDTO dto);

  /**
   * 获取红包配置
   *
   * @return
   */
  ActivityRedPacketConfigVO getConfig();

  /**
   * 更新活动配置
   *
   * @param dto
   */
  void updateConfig(ActivityRedPacketConfigDTO dto);

  /**
   * 获取转盘奖品配置
   *
   * @return
   */
  List<ActivityTurntablePrizeConfigVO> getTurntablePrizeConfig();

  /**
   * 更新转盘奖品配置
   *
   * @param e ActivityTurntablePrizeConfigDTO
   */
  void updateTurntablePrizeConfig(ActivityTurntablePrizeConfigDTO e);
}
