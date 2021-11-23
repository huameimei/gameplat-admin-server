package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.gameplat.admin.mapper.ActivityDistributeDao;
import com.gameplat.admin.mapper.ActivityQualificationDao;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.BeanUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityQualificationServiceImpl implements ActivityQualificationService {

    @Autowired
    private ActivityQualificationDao activityQualificationDao;

    @Autowired
    private ActivityDistributeDao activityDistributeDao;

    @Autowired
    private UserActivityFeignService userActivityFeignService;

    @Override
    public List<ActivityQualification> findActivityQualificationList(ActivityQualificationDTO activityQualificationDTO) {
        return activityQualificationDao.findActivityQualificationList(activityQualificationDTO);
    }

    @Override
    public void saveActivityQualification(ActivityQualification activityQualification) {
        int i = activityQualificationDao.saveQualificationManage(activityQualification);
        if (i != 1) {
            throw new ServiceException("添加失败！");
        }
    }

    @Override
    public void updateActivityQualification(ActivityQualification qualificationManage) {
        int i = activityQualificationDao.updateQualificationManage(qualificationManage);
        if (i != 1) {
            throw new ServiceException("修改失败！");
        }
    }

    @Override
    public void deleteBatchActivityQualification(List<Long> qualificationIds) {
        activityQualificationDao.deleteBatchQualificationManage(qualificationIds);
    }


    @Override
    public void chick(List<Long> qualificationIds) {
        //获取所有用户
        List<UserStatusVO> usersStatus = userActivityFeignService.findUserStatusList(Lists.newArrayList());
        List<UserStatusVO1> usersStatusList = new ArrayList<>();
        for (UserStatusVO vo : usersStatus) {
            UserStatusVO1 vo1 = new UserStatusVO1();
            BeanUtils.copyBeanProp(vo1, vo);
            usersStatusList.add(vo1);
        }
        Map<Long, Integer> userIdMap = usersStatusList.stream().collect(Collectors.toMap(UserStatusVO1::getUserId, UserStatusVO1::getStatus));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passAuditActivityQualification(ActivityQualification qualificationManage) {
        List<ActivityQualification> qualificationManageStatusList = activityQualificationDao.findQualificationManageStatusList(qualificationManage.getQualificationIds());
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
        }
        qualificationManage.setStatus(2);
        qualificationManage.setAuditPerson(SysUserUtil.getUserName());
        int i = activityQualificationDao.passAuditQualificationManage(qualificationManage);
        if (i < 1) {
            throw new ServiceException("修改失败！");
        }
        //审核成功，添加派发记录
        List<Long> distributeIds = qualificationManage.getQualificationIds();
        //查出所有审核成功的资格详细信息
        List<ActivityQualification> list = activityQualificationDao.findQualificationManageStatusList(distributeIds);
//        Set<Long> ids = list.stream().map(QualificationManage::getUserId).collect(Collectors.toSet());
//        List<UserRankVO1> userRankList = userClient.findUserRankList(Lists.newArrayList(ids));
//        Map<Long, List<UserRankVO1>> userRankMap = userRankList.stream().collect(Collectors.groupingBy(UserRankVO1::getUserId));

        List<ActivityDistribute> lists = new ArrayList<>();
        ActivityDistribute ad;
        for (ActivityQualification manage : list) {
            //插入运营红包雨派发记录
            if (manage.getActivityType() == 2) {
                //抽奖次数
//                Integer drawNum = manage.getDrawNum();
//                for (int j = 0; j < drawNum; j++) {
//                    //最大值和最小值之间的随机数
//                    Double win = (Double) (manage.getMinMoney() + Math.random() * (manage.getMaxMoney() - manage.getMinMoney() + 1));
//                    ActivityDistribute activityDistribute = new ActivityDistribute();
//                    activityDistribute.setDistributeId(idGenerator.nextId());
//                    activityDistribute.setActivityName(manage.getActivityName());
//                    activityDistribute.setActivityType(2);
//                    activityDistribute.setActivityId(manage.getActivityId());
//                    activityDistribute.setUserId(Long.valueOf(manage.getUserId()));
//                    activityDistribute.setUsername(manage.getUsername());
//                    activityDistribute.setApplyTime(new Date());
//                    activityDistribute.setDiscountsMoney(new BigDecimal(win));
//                    activityDistribute.setStatus(1);
//                    //activityDistribute.setTargetMoney(redPacketCondition.getTopUpMoney());
//                    activityDistribute.setDisabled(1);
//                    activityDistribute.setDeleteFlag(1);
//                    activityDistribute.setCreateBy("system");
//                    activityDistribute.setCreateTime(new Date());
//                    List<UserRankVO1> userRankVOS = userRankMap.get(manage.getUserId());
//                    if (userRankVOS==null) {
//                        activityDistribute.setMemberPayLevel(0);
//                    }else {
//                        activityDistribute.setMemberPayLevel(Integer.valueOf(userRankVOS.get(0).getRank()));
//                    }
//                    activityDistribute.setQualificationActivityId(manage.getQualificationId().toString());
//                    activityDistribute.setSoleIdentifier(RandomUtil.generateNumber(6));
//                    lists.add(activityDistribute);
//                }
            } else {
                //插入活动大厅派发记录
                ad = new ActivityDistribute();
                ad.setUserId(Long.getLong(manage.getUserId().toString()));
                ad.setUsername(manage.getUsername());
                ad.setCreateBy(SysUserUtil.getUserName());
                ad.setApplyTime(new Date());
                ad.setActivityId(manage.getActivityId());
                ad.setActivityType(1);
                ad.setActivityName(manage.getActivityName());
                ad.setUserId(manage.getUserId());
                ad.setStatus(1);
//                ad.setDisabled(1);
                ad.setDeleteFlag(1);
                ad.setDiscountsMoney(NumberUtil.toBigDecimal(manage.getMaxMoney()));
                ad.setQualificationActivityId(manage.getQualificationActivityId());
                ad.setStatisItem(manage.getStatisItem());
                ad.setWithdrawDml(manage.getWithdrawDml());
                ad.setSoleIdentifier(manage.getSoleIdentifier());
                ad.setStatisStartTime(manage.getStatisStartTime());
                ad.setStatisEndTime(manage.getStatisEndTime());
                ad.setGetWay(manage.getGetWay());
                lists.add(ad);
            }
        }
        int k = activityDistributeDao.batchInsertActivityDistribute(lists);
        if (k < 1) {
            log.info("插入活动派发成功");
            throw new ServiceException("插入活动派发失败");
        }
    }

    @Override
    public void updateQualificationStatus(ActivityQualification activityQualification) {
        if (activityQualification.getStatus() == 2) {
            throw new ServiceException("审核通过的记录不能修改资格状态！");
        }
        int i = activityQualificationDao.updateQualificationManageStatus(activityQualification);
        if (i < 1) {
            throw new ServiceException("修改失败！");
        }
    }

    @Override
    public void updateDeleteStatus(ActivityQualification activityQualification) {
        activityQualification.setDeleteFlag(0);
        int i = activityQualificationDao.updateQualificationManageStatus(activityQualification);
        if (i < 1) {
            throw new ServiceException("删除失败！");
        }
    }
}
