package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.model.entity.activity.ActivityQualification;

import java.util.List;
import java.util.Map;

/**
 * 参加活动资格查询
 *
 * @author kenvin <br>
 */
public interface ActivityQualificationService extends IService<ActivityQualification> {

  /**
   * 根据条件查询资格列表
   *
   * @param entity ActivityQualification
   * @return List
   */
  List<ActivityQualification> findQualificationList(ActivityQualification entity);

  /**
   * 查询活动资格列表
   *
   * @param page PageDTO
   * @param dto ActivityQualificationQueryDTO
   * @return IPage
   */
  IPage<ActivityQualificationVO> list(
      PageDTO<ActivityQualification> page, ActivityQualificationQueryDTO dto);

  /**
   * 新增活动资格
   *
   * @param dto ActivityQualificationAddDTO
   */
  void add(ActivityQualificationAddDTO dto);

  /**
   * 审核活动资格
   *
   * @param dto ActivityQualificationAuditStatusDTO
   */
  void auditStatus(ActivityQualificationAuditStatusDTO dto);

  /**
   * 更新状态
   *
   * @param dto ActivityQualificationUpdateStatusDTO
   */
  void updateQualificationStatus(ActivityQualificationUpdateStatusDTO dto);

  /**
   * 更新活动资格状态
   *
   * @param activityQualification ActivityQualification
   */
  void updateQualificationStatus(ActivityQualification activityQualification);

  /**
   * 删除活动资格
   *
   * @param ids String
   */
  void delete(String ids);

  /**
   * 资格检测
   *
   * @param dto ActivityQualificationCheckDTO
   * @return Map
   */
  Map<String, Object> checkQualification(ActivityQualificationCheckDTO dto);

  /**
   * 生成当天红包雨资格
   */
  void activityRedEnvelopeQualification();

  void refuse(ActivityQualificationRefuseDTO dto);
}
