package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityQualificationConvert;
import com.gameplat.admin.mapper.ActivityQualificationMapper;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.common.context.GlobalContextHolder;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ActivityQualificationServiceImpl extends
        ServiceImpl<ActivityQualificationMapper, ActivityQualification>
        implements ActivityQualificationService {

    @Autowired
    private ActivityQualificationConvert activityQualificationConvert;

    @Autowired
    private ActivityDistributeService activityDistributeService;


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
            throw new ServiceException("更新失败");
        }
    }

    @Override
    public void updateStatus(ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO) {
        List<ActivityQualification> qualificationManageStatusList =
                this.lambdaQuery().in(ActivityQualification::getActivityId
                        , activityQualificationUpdateStatusDTO.getQualificationIds()).list();
        for (ActivityQualification qualification : qualificationManageStatusList) {
            if (qualification.getStatus() == 0) {
                throw new ServiceException("您选择的数据有无效数据，无效数据不能审核！");
            }
            if (qualification.getStatus() == 2) {
                throw new ServiceException("您选择的数据有已审核的数据，请勿重复审核！");
            }
            if (qualification.getQualificationStatus() == 0) {
                throw new ServiceException("您选择的数据有资格状态被禁用的数据，禁用状态不能审核！");
            }
            //更新数据
            qualification.setStatus(2);
            qualification.setAuditPerson(GlobalContextHolder.getContext().getUsername());
            qualification.setAuditTime(new Date());
        }
        //批量更新数据
        if (!this.updateBatchById(qualificationManageStatusList)) {
            throw new ServiceException("修改失败！");
        }

        //审核成功，添加派发记录
        List<Long> distributeIds = activityQualificationUpdateStatusDTO.getQualificationIds();
        List<ActivityQualification> activityQualificationList = this.lambdaQuery().in(ActivityQualification::getActivityId, distributeIds).list();

        List<ActivityDistribute> activityDistributeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(activityQualificationList)) {
            ActivityDistribute ad;
            for (ActivityQualification activityQualification : activityQualificationList) {
                if (activityQualification.getActivityType() != 2) {
                    //插入活动大厅派发记录
                    ad = new ActivityDistribute();
                    ad.setUserId(Long.getLong(activityQualification.getUserId().toString()));
                    ad.setUsername(activityQualification.getUsername());
                    ad.setApplyTime(new Date());
                    ad.setActivityId(activityQualification.getActivityId());
                    ad.setActivityType(1);
                    ad.setActivityName(activityQualification.getActivityName());
                    ad.setUserId(activityQualification.getUserId());
                    ad.setStatus(1);
//                ad.setDisabled(1);
                    ad.setDeleteFlag(1);
                    ad.setDiscountsMoney(NumberUtil.toBigDecimal(activityQualification.getMaxMoney()));
                    ad.setQualificationActivityId(activityQualification.getQualificationActivityId());
                    ad.setStatisItem(activityQualification.getStatisItem());
                    ad.setWithdrawDml(activityQualification.getWithdrawDml());
                    ad.setSoleIdentifier(activityQualification.getSoleIdentifier());
                    ad.setStatisStartTime(activityQualification.getStatisStartTime());
                    ad.setStatisEndTime(activityQualification.getStatisEndTime());
                    ad.setGetWay(activityQualification.getGetWay());
                    activityDistributeList.add(ad);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(activityDistributeList)) {
            boolean result = activityDistributeService.saveBatch(activityDistributeList);
            if (result) {
                log.info("插入活动派发成功");
            }
        }
    }

    @Override
    public void updateQualificationStatus(ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO) {
        if (activityQualificationUpdateStatusDTO.getId() == null || activityQualificationUpdateStatusDTO.getId() == 0) {
            throw new ServiceException("资格id不能为空");
        }
        if (activityQualificationUpdateStatusDTO.getStatus() == 2) {
            throw new ServiceException("审核通过的记录不能修改资格状态");
        }
        ActivityQualification activityQualification = activityQualificationConvert.toEntity(activityQualificationUpdateStatusDTO);
        if (!this.updateById(activityQualification)) {
            throw new ServiceException("修改失败！");
        }
    }

    @Override
    public void updateQualificationStatus(ActivityQualification activityQualification) {
        LambdaUpdateWrapper<ActivityQualification> update = Wrappers.lambdaUpdate();
        update.set(ActivityQualification::getEmployNum, activityQualification.getEmployNum());
        update.set(ActivityQualification::getEmployTime, activityQualification.getEmployTime());
        update.eq(ActivityQualification::getQualificationActivityId, activityQualification.getQualificationActivityId());
        this.update(update);
    }

    @Override
    public void delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArr) {
            idList.add(Long.parseLong(idStr));
        }
        List<ActivityQualification> qualificationList = this.lambdaQuery().in(ActivityQualification::getId).list();
        if (CollectionUtils.isNotEmpty(qualificationList)) {
            for (ActivityQualification activityQualification : qualificationList) {
                activityQualification.setDeleteFlag(0);
            }
            boolean result = this.updateBatchById(qualificationList);
            if (!result) {
                throw new ServiceException("删除失败");
            }
        }
    }


}
