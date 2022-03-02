package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyQueryDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.model.entity.activity.ActivityLobby;

import java.util.List;

/**
 * 活动大厅业务查询
 *
 * @author kenvin
 */
public interface ActivityLobbyService extends IService<ActivityLobby> {

  /**
   * 查询活动大厅列表
   *
   * @param page
   * @param activityLobbyQueryDTO
   * @return
   */
  IPage<ActivityLobbyVO> findActivityLobbyList(
      PageDTO<ActivityLobby> page, ActivityLobbyQueryDTO activityLobbyQueryDTO);

  /**
   * 新增活动大厅
   *
   * @param activityLobbyAddDTO
   */
  void add(ActivityLobbyAddDTO activityLobbyAddDTO);

  /**
   * 更新活动大厅
   *
   * @param activityLobbyUpdateDTO
   */
  void update(ActivityLobbyUpdateDTO activityLobbyUpdateDTO);

  /**
   * 删除一个或者多个数据
   *
   * @param ids
   */
  void remove(String ids);

  /**
   * 根据id删除活动大厅
   *
   * @param ids
   */
  void deleteActivityLobby(String ids);

  /**
   * @param activityLobbyUpdateStatusDTO
   */
  void updateStatus(ActivityLobbyUpdateStatusDTO activityLobbyUpdateStatusDTO);

  /**
   * 查询未绑定的大厅活动列表
   *
   * @return
   */
  List<ActivityLobbyVO> findUnboundLobbyList();
}
