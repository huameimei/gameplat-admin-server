package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.dto.ActivityDistributeQueryDTO;
import com.gameplat.admin.model.vo.ActivityDistributeStatisticsVO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;

import java.util.List;

/**
 * 活动分发管理
 *
 * @author kenvin
 */
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
  boolean saveDistributeBatch(List<ActivityDistribute> activityDistributeList);

  /**
   * 分页查询
   *
   * @param page
   * @param activityDistributeQueryDTO
   * @return
   */
  PageExt<IPage<ActivityDistributeVO>, ActivityDistributeStatisticsVO> list(
      PageDTO<ActivityDistribute> page, ActivityDistributeQueryDTO activityDistributeQueryDTO);

  /**
   * 更新分发状态
   *
   * @param ids
   */
  void updateStatus(String ids);

  /**
   * 删除活动分发
   *
   * @param ids
   */
  void remove(String ids);

  /**
   * 更新删除状态
   *
   * @param ids
   * @return
   */
  boolean updateDeleteStatus(String ids);
}
