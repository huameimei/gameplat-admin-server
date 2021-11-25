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
     * 根据条件查询资格列表
     *
     * @param activityQualification
     * @return
     */
    List<ActivityQualification> findQualificationList(ActivityQualification activityQualification);
}
