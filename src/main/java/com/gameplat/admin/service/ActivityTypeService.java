package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;

import java.util.List;

/**
 * 活动类型
 *
 * @author kenvin
 */
public interface ActivityTypeService extends IService<ActivityType> {

  /**
   * 通过id列表查询活动类型
   *
   * @param activityTypeIdList
   * @return
   */
  List<ActivityType> findByTypeIdList(List<Long> activityTypeIdList);

  /**
   * 分页查询活动类型
   *
   * @param page
   * @param activityTypeQueryDTO
   * @return
   */
  IPage<ActivityTypeVO> list(PageDTO<ActivityType> page, ActivityTypeQueryDTO activityTypeQueryDTO);

  /**
   * 新增活动类型
   *
   * @param activityTypeAddDTO
   */
  void add(ActivityTypeAddDTO activityTypeAddDTO);

  /**
   * 更新
   *
   * @param activityTypeUpdateDTO
   */
  void update(ActivityTypeUpdateDTO activityTypeUpdateDTO);

  /**
   * 删除
   *
   * @param ids
   */
  void remove(String ids);

  /**
   * 查询所有的活动列表
   *
   * @param language
   * @return
   */
  List<ActivityTypeVO> listAll(String language);
}
