package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityInfoVO;

import java.util.List;

/**
 * 活动业务类
 *
 * @author admin
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

  /**
   * 列表查询
   *
   * @param page
   * @param activityInfoQueryDTO
   * @return
   */
  IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoQueryDTO activityInfoQueryDTO);

  /**
   * 查询活动详情
   *
   * @param id
   * @return
   */
  ActivityInfoVO detail(Long id);

  /**
   * 新增活动
   *
   * @param activityInfoAddDTO
   * @param country
   * @return
   */
  void add(ActivityInfoAddDTO activityInfoAddDTO, String country);

  /**
   * 检查活动是否满足条件
   *
   * @param activityLobbyId
   * @param id
   */
  void checkActivityLobbyId(Long activityLobbyId, Long id);

  /**
   * 保存活动信息
   *
   * @param activityInfo
   * @return
   */
  boolean saveActivityInfo(ActivityInfo activityInfo);

  /**
   * 查询关联规则的活动
   *
   * @return
   */
  List<ActivityInfoVO> getAllActivity();

  /**
   * 通过条件查询列表数据
   *
   * @param activityInfo
   * @return
   */
  List<ActivityInfo> list(ActivityInfo activityInfo);

  /**
   * 通过活动类型查询活动列表
   *
   * @param id
   * @return
   */
  List<ActivityInfo> findByTypeId(Long id);

  /**
   * 修改活动
   *
   * @param activityInfoUpdateDTO
   * @param country
   */
  void update(ActivityInfoUpdateDTO activityInfoUpdateDTO, String country);

  /**
   * 删除活动
   *
   * @param ids
   */
  void delete(String ids);

  /**
   * 更新活动排序
   *
   * @param activityInfoUpdateSortDTO
   */
  void updateSort(ActivityInfoUpdateSortDTO activityInfoUpdateSortDTO);
}
