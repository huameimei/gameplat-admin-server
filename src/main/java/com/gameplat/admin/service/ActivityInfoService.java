package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoQueryDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateSortDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.model.entity.activity.ActivityInfo;

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
   * @param page PageDTO
   * @param dto ActivityInfoQueryDTO
   * @return IPage
   */
  IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoQueryDTO dto);

  /**
   * 查询活动详情
   *
   * @param id Long
   * @return ActivityInfoVO
   */
  ActivityInfoVO detail(Long id);

  /**
   * 新增活动
   *
   * @param dto ActivityInfoAddDTO
   */
  void add(ActivityInfoAddDTO dto);

  /**
   * 检查活动是否满足条件
   *
   * @param activityLobbyId Long
   * @param id Long
   */
  void checkActivityLobbyId(Long activityLobbyId, Long id);

  /**
   * 保存活动信息
   *
   * @param activityInfo ActivityInfo
   * @return boolean
   */
  boolean saveActivityInfo(ActivityInfo activityInfo);

  /**
   * 查询关联规则的活动
   *
   * @return List
   */
  List<ActivityInfoVO> getAllActivity();

  /**
   * 通过条件查询列表数据
   *
   * @param activityInfo ActivityInfo
   * @return List
   */
  List<ActivityInfo> list(ActivityInfo activityInfo);

  /**
   * 通过活动类型查询活动列表
   *
   * @param id Long
   * @return List
   */
  List<ActivityInfo> findByTypeId(Long id);

  /**
   * 修改活动
   *
   * @param dto ActivityInfoUpdateDTO
   */
  void update(ActivityInfoUpdateDTO dto);

  /**
   * 删除活动
   *
   * @param ids String
   */
  void delete(String ids);

  /**
   * 更新活动排序
   *
   * @param activityInfoUpdateSortDTO
   */
  void updateSort(ActivityInfoUpdateSortDTO activityInfoUpdateSortDTO);

  /**
   * 查询未绑定的活动大厅
   *
   * @param isBind
   * @return
   */
  List<ActivityLobbyVO> findUnboundLobbyList(boolean isBind);

}
