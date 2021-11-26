package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityTypeMapper;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.service.ActivityTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityTypeServiceImpl extends ServiceImpl<ActivityTypeMapper, ActivityType>
        implements ActivityTypeService {


    @Override
    public List<ActivityType> findByTypeIdList(List<Long> activityTypeIdList) {
        if (CollectionUtils.isEmpty(activityTypeIdList)) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(ActivityType::getId, activityTypeIdList).list();
    }
}
