package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityQualificationConvert;
import com.gameplat.admin.mapper.ActivityQualificationMapper;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityQualificationServiceImpl extends
        ServiceImpl<ActivityQualificationMapper, ActivityQualification>
        implements ActivityQualificationService {

    @Autowired
    private ActivityQualificationConvert activityQualificationConvert;

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

    @Override
    public IPage<ActivityQualificationVO> list(PageDTO<ActivityQualification> page, ActivityQualificationDTO activityQualificationDTO) {
        LambdaQueryChainWrapper<ActivityQualification> queryChainWrapper = this.lambdaQuery();
        queryChainWrapper.like(StringUtils.isNotBlank(activityQualificationDTO.getActivityName())
                , ActivityQualification::getActivityName, activityQualificationDTO.getActivityName())
                .eq(activityQualificationDTO.getActivityType() != null
                        , ActivityQualification::getActivityType, activityQualificationDTO.getActivityType())
                .eq(activityQualificationDTO.getActivityId() != null && activityQualificationDTO.getActivityId() != 0
                        , ActivityQualification::getActivityId, activityQualificationDTO.getActivityId())
                .eq(activityQualificationDTO.getUserId() != null && activityQualificationDTO.getUserId() != 0
                        , ActivityQualification::getUserId, activityQualificationDTO.getUserId())
                .like(StringUtils.isNotBlank(activityQualificationDTO.getUsername())
                        , ActivityQualification::getUsername, activityQualificationDTO.getUsername())
                .ge(StringUtils.isNotBlank(activityQualificationDTO.getApplyStartTime())
                        , ActivityQualification::getApplyTime, activityQualificationDTO.getApplyStartTime())
                .le(StringUtils.isNotBlank(activityQualificationDTO.getApplyEndTime())
                        , ActivityQualification::getApplyTime, activityQualificationDTO.getApplyEndTime())
                .eq(StringUtils.isNotBlank(activityQualificationDTO.getAuditPerson())
                        , ActivityQualification::getAuditPerson, activityQualificationDTO.getAuditPerson())
                .eq(activityQualificationDTO.getAuditTime() != null, ActivityQualification::getAuditTime, activityQualificationDTO.getAuditTime())
                .like(StringUtils.isNotBlank(activityQualificationDTO.getAuditRemark())
                        , ActivityQualification::getAuditRemark, activityQualificationDTO.getAuditRemark())
                .eq(activityQualificationDTO.getStatus() != null, ActivityQualification::getStatus, activityQualificationDTO.getStatus())
                .ge(activityQualificationDTO.getActivityStartTime() != null
                        , ActivityQualification::getActivityStartTime, activityQualificationDTO.getActivityStartTime())
                .eq(activityQualificationDTO.getActivityEndTime() != null
                        , ActivityQualification::getActivityEndTime, activityQualificationDTO.getActivityEndTime())
                .lt(activityQualificationDTO.getAbortTime() != null
                        , ActivityQualification::getActivityStartTime, activityQualificationDTO.getAbortTime())
                .eq(activityQualificationDTO.getQualificationStatus() != null
                        , ActivityQualification::getQualificationStatus, activityQualificationDTO.getQualificationStatus())
                .eq(activityQualificationDTO.getDrawNum() != null
                        , ActivityQualification::getDrawNum, activityQualificationDTO.getDrawNum())
                .eq(activityQualificationDTO.getDeleteFlag() != null
                        , ActivityQualification::getDeleteFlag, activityQualificationDTO.getDeleteFlag())
                .eq(activityQualificationDTO.getStatisItem() != null && activityQualificationDTO.getStatisItem() != 0
                        , ActivityQualification::getStatisItem, activityQualificationDTO.getStatisItem())
        ;
        return queryChainWrapper.page(page).convert(activityQualificationConvert::toVo);
    }

    @Override
    public void add(ActivityQualificationAddDTO activityQualificationAddDTO) {
        ActivityQualification activityQualification = activityQualificationConvert.toEntity(activityQualificationAddDTO);
        if (!this.save(activityQualification)) {
            throw new ServiceException("保存失败");
        }
    }

    @Override
    public void update(ActivityQualificationUpdateDTO activityQualificationUpdateDTO) {
        ActivityQualification activityQualification = activityQualificationConvert.toEntity(activityQualificationUpdateDTO);
        if (!this.save(activityQualification)) {
            throw new ServiceException("保存失败");
        }
    }

}
