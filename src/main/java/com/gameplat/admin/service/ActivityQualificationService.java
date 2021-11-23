package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 参加活动资格查询
 *
 * @author lyq
 * @Description 业务实现层
 * @date 2020-08-20 11:32:32
 */
public interface ActivityQualificationService {

    /**
     * 查询活动资格列表
     *
     * @param activityQualificationDTO
     * @return
     */
    List<ActivityQualification> findActivityQualificationList(ActivityQualificationDTO activityQualificationDTO);

    /**
     * 保存活动资格
     *
     * @param activityQualification
     */
    void saveActivityQualification(ActivityQualification activityQualification);

    /**
     * 更新活动资格
     *
     * @param activityQualification
     */
    void updateActivityQualification(ActivityQualification activityQualification);

    /**
     * 批量删除活动资格
     *
     * @param qualificationIds
     */
    void deleteBatchActivityQualification(List<Long> qualificationIds);

    /**
     * 检查是否有参加活动的资格
     *
     * @param qualificationIds
     */
    void chick(List<Long> qualificationIds);

    /**
     * 通过资格审核
     *
     * @param activityQualification
     */
    void passAuditActivityQualification(ActivityQualification activityQualification);

    /**
     * 更新活动资格状态
     *
     * @param activityQualification
     */
    void updateQualificationStatus(ActivityQualification activityQualification);

    /**
     * 更新删除状态
     *
     * @param activityQualification
     */
    void updateDeleteStatus(ActivityQualification activityQualification);
}
