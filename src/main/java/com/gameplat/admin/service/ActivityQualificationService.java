package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityQualificationVO;

import java.util.List;
import java.util.Map;


/**
 * 参加活动资格查询
 *
 * @author kenvin
 * @Description 业务实现层
 */
public interface ActivityQualificationService extends IService<ActivityQualification> {

    /**
     * 根据条件查询资格列表
     *
     * @param activityQualification
     * @return
     */
    List<ActivityQualification> findQualificationList(ActivityQualification activityQualification);

    /**
     * 查询活动资格列表
     *
     * @param page
     * @param activityQualificationQueryDTO
     * @return
     */
    IPage<ActivityQualificationVO> list(PageDTO<ActivityQualification> page, ActivityQualificationQueryDTO activityQualificationQueryDTO);

    /**
     * 新增活动资格
     *
     * @param activityQualificationAddDTO
     */
    void add(ActivityQualificationAddDTO activityQualificationAddDTO);

    /**
     * 审核活动资格
     *
     * @param activityQualificationAuditStatusDTO
     */
    void auditStatus(ActivityQualificationAuditStatusDTO activityQualificationAuditStatusDTO);

    /**
     * 更新状态
     *
     * @param activityQualificationUpdateStatusDTO
     */
    void updateQualificationStatus(ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO);

    /**
     * 更新活动资格状态
     *
     * @param activityQualification
     */
    void updateQualificationStatus(ActivityQualification activityQualification);

    /**
     * 删除活动资格
     *
     * @param ids
     */
    void delete(String ids);

    /**
     * 资格检测
     *
     * @param activityQualificationCheckDTO
     */
    Map<String, Object> checkQualification(ActivityQualificationCheckDTO activityQualificationCheckDTO);
}
