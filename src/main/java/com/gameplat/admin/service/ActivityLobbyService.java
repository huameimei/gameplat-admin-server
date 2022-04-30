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
   * @param page PageDTO
   * @param dto ActivityLobbyQueryDTO
   * @return IPage
   */
  IPage<ActivityLobbyVO> findActivityLobbyList(
      PageDTO<ActivityLobby> page, ActivityLobbyQueryDTO dto);

  /**
   * 新增活动大厅
   *
   * @param dto ActivityLobbyAddDTO
   */
  void add(ActivityLobbyAddDTO dto);

  /**
   * 更新活动大厅
   *
   * @param dto ActivityLobbyUpdateDTO
   */
  void update(ActivityLobbyUpdateDTO dto);

  /**
   * 删除一个或者多个数据
   *
   * @param ids String
   */
  void remove(String ids);

  /**
   * 根据id删除活动大厅
   *
   * @param ids String
   */
  void deleteActivityLobby(String ids);

  /**
   * @param dto ActivityLobbyUpdateStatusDTO
   */
  void updateStatus(ActivityLobbyUpdateStatusDTO dto);

  /**
   * 查询所有活动大厅列表
   *
   * @return
   */
  List<ActivityLobbyVO> findAllLobbyList();

  /**
   * 查询单个活动信息
   * @param activityLobbyId
   * @return
   */
  ActivityLobbyVO getActivityLobbyVOById(Long activityLobbyId);
}
