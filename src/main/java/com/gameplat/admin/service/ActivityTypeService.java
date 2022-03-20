package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.model.entity.activity.ActivityType;

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
   * @param activityTypeIdList List
   * @return List
   */
  List<ActivityType> findByTypeIdList(List<Long> activityTypeIdList);

  /**
   * 分页查询活动类型
   *
   * @param page PageDTO
   * @param dto ActivityTypeQueryDTO
   * @return IPage
   */
  IPage<ActivityTypeVO> list(PageDTO<ActivityType> page, ActivityTypeQueryDTO dto);

  /**
   * 新增活动类型
   *
   * @param dto ActivityTypeAddDTO
   */
  void add(ActivityTypeAddDTO dto);

  /**
   * 更新
   *
   * @param dto ActivityTypeUpdateDTO
   */
  void update(ActivityTypeUpdateDTO dto);

  /**
   * 删除
   *
   * @param ids String
   */
  void remove(String ids);

  /**
   * 查询所有的活动列表
   *
   * @return List
   */
  List<ActivityTypeVO> listAll();
}
