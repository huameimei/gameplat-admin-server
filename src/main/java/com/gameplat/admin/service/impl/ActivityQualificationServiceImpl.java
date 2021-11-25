package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityQualificationDao;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.service.ActivityQualificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityQualificationServiceImpl extends
        ServiceImpl<ActivityQualificationDao, ActivityQualification>
        implements ActivityQualificationService {

    @Override
    public List<ActivityQualification> findQualificationList(ActivityQualification activityQualification) {
        return this.lambdaQuery()
                .eq(activityQualification.getActivityId() != null && activityQualification.getActivityId() != 0
                        , ActivityQualification::getActivityId, activityQualification.getActivityId())
                .eq(activityQualification.getDeleteFlag() != null && activityQualification.getDeleteFlag() != 0
                        , ActivityQualification::getDeleteFlag, activityQualification.getDeleteFlag())
                .eq(activityQualification.getStatus() != null && activityQualification.getStatus() != 0
                        , ActivityQualification::getStatus, activityQualification.getStatus())
                .list();
    }

}
