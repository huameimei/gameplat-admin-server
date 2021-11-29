package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityDistributeMapper;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityDistributeServiceImpl
        extends ServiceImpl<ActivityDistributeMapper, ActivityDistribute>
        implements ActivityDistributeService {


    @Override
    public List<ActivityDistribute> findActivityDistributeList(ActivityDistribute activityDistribute) {
        return this.lambdaQuery()
                .eq(activityDistribute.getActivityId() != null && activityDistribute.getActivityId() != 0
                        , ActivityDistribute::getActivityId, activityDistribute.getActivityId())
                .eq(activityDistribute.getDeleteFlag() != null && activityDistribute.getDeleteFlag() != 0
                        , ActivityDistribute::getDeleteFlag, activityDistribute.getDeleteFlag())
                .eq(activityDistribute.getStatus() != null && activityDistribute.getStatus() != 0
                        , ActivityDistribute::getStatus, activityDistribute.getStatus())
                .list();
    }

    @Override
    public void deleteByLobbyIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArr) {
            idList.add(Long.parseLong(idStr));
        }
        this.removeByIds(idList);
    }

    @Override
    public boolean saveBatch(List<ActivityDistribute> activityDistributeList) {
        return this.saveBatch(activityDistributeList);
    }
}
