package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityLobbyConvert;
import com.gameplat.admin.convert.ActivityQualificationConvert;
import com.gameplat.admin.mapper.ActivityQualificationMapper;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.ActivityCommonService;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.RandomUtil;
import com.gameplat.base.common.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 活动资格管理
 *
 * @author kenvin
 */
@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class)
public class ActivityQualificationServiceImpl
    extends ServiceImpl<ActivityQualificationMapper, ActivityQualification>
    implements ActivityQualificationService {

  @Autowired private ActivityQualificationConvert activityQualificationConvert;

  @Autowired private ActivityDistributeService activityDistributeService;

  @Autowired private MemberService memberService;

  @Autowired private ActivityLobbyConvert activityLobbyConvert;

  @Autowired private ActivityCommonService activityCommonService;

  @Override
  public List<ActivityQualification> findQualificationList(
      ActivityQualification activityQualification) {
    return this.lambdaQuery()
        .eq(
            activityQualification.getActivityId() != null
                && activityQualification.getActivityId() != 0,
            ActivityQualification::getActivityId,
            activityQualification.getActivityId())
        .eq(
            activityQualification.getDeleteFlag() != null
                && activityQualification.getDeleteFlag() != 0,
            ActivityQualification::getDeleteFlag,
            activityQualification.getDeleteFlag())
        .eq(
            activityQualification.getStatus() != null && activityQualification.getStatus() != 0,
            ActivityQualification::getStatus,
            activityQualification.getStatus())
        .eq(ActivityQualification::getDeleteFlag, 1) // 是否逻辑删除
        .list();
  }

  @Override
  public IPage<ActivityQualificationVO> list(
      PageDTO<ActivityQualification> page,
      ActivityQualificationQueryDTO activityQualificationQueryDTO) {
    LambdaQueryChainWrapper<ActivityQualification> queryChainWrapper = this.lambdaQuery();
    queryChainWrapper
        .eq(
            activityQualificationQueryDTO.getActivityId() != null
                && activityQualificationQueryDTO.getActivityId() != 0,
            ActivityQualification::getActivityId,
            activityQualificationQueryDTO.getActivityId())
        .like(
            StringUtils.isNotBlank(activityQualificationQueryDTO.getUsername()),
            ActivityQualification::getUsername,
            activityQualificationQueryDTO.getUsername())
        .eq(
            activityQualificationQueryDTO.getStatus() != null,
            ActivityQualification::getStatus,
            activityQualificationQueryDTO.getStatus())
        .eq(
            activityQualificationQueryDTO.getQualificationStatus() != null,
            ActivityQualification::getQualificationStatus,
            activityQualificationQueryDTO.getQualificationStatus());
    return queryChainWrapper.page(page).convert(activityQualificationConvert::toVo);
  }

  @Override
  public void add(ActivityQualificationAddDTO activityQualificationAddDTO) {
    Integer type = activityQualificationAddDTO.getType();
    String username = activityQualificationAddDTO.getUsername();
    if (type == null) {
      throw new ServiceException("活动类型不能为空");
    }
    if (StringUtils.isBlank(username)) {
      throw new ServiceException("用户名不能为空");
    }
    List<String> usernameList = new ArrayList<>();
    if (username.contains(",")) {
      usernameList.addAll(Lists.newArrayList(username.split(",")));
    } else {
      usernameList.add(username);
    }

    for (String username1 : usernameList) {
      MemberInfoVO memberInfo = memberService.getMemberInfo(username1);
      if (StringUtils.isNull(memberInfo)) {
        throw new ServiceException("用户【" + username1 + "】不存在,请检查");
      }
      // 1 活动大厅  2 红包雨
      if (type == 1) {
        List<ActivityLobbyDTO> activityLobbyList =
            activityQualificationAddDTO.getActivityLobbyList();
        if (CollectionUtils.isEmpty(activityLobbyList)) {
          throw new ServiceException("活动大厅数据不能为空");
        }
        List<ActivityQualification> manageList = new ArrayList<>();
        ActivityQualification qm;
        for (ActivityLobbyDTO activityLobbyDTO : activityLobbyList) {
          List<ActivityLobbyDiscountDTO> lobbyDiscount = activityLobbyDTO.getLobbyDiscountList();
          if (activityLobbyDTO.getMultipleHandsel() == 0 && lobbyDiscount.size() > 1) {
            throw new ServiceException("【" + activityLobbyDTO.getTitle() + "】，没有开启多重彩金");
          }
          qm = new ActivityQualification();
          ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyDTO);
          String auditRemark =
              activityCommonService.getAuditRemark(activityLobby, "", "", null, null);
          qm.setAuditRemark(auditRemark);
          qm.setActivityId(activityLobbyDTO.getId());
          qm.setActivityName(activityLobbyDTO.getTitle());
          qm.setActivityType(activityLobbyDTO.getType());
          qm.setUserId(memberInfo.getId());
          qm.setUsername(username1);
          qm.setApplyTime(new Date());
          qm.setStatus(1);
          qm.setActivityStartTime(activityLobbyDTO.getStartTime());
          qm.setActivityEndTime(activityLobbyDTO.getEndTime());
          qm.setDeleteFlag(1);
          qm.setDrawNum(1);
          qm.setEmployNum(0);
          qm.setQualificationActivityId(IdWorker.getIdStr());
          qm.setQualificationStatus(1);
          qm.setStatisItem(activityLobbyDTO.getStatisItem());
          qm.setMaxMoney(
              lobbyDiscount.stream().mapToInt(ActivityLobbyDiscountDTO::getPresenterValue).sum());
          qm.setWithdrawDml(
              lobbyDiscount.stream().mapToInt(ActivityLobbyDiscountDTO::getWithdrawDml).sum());
          qm.setSoleIdentifier(RandomUtil.generateOrderCode());
          lobbyDiscount.sort(Comparator.comparingInt(ActivityLobbyDiscountDTO::getTargetValue));
          qm.setAwardDetail(JSON.parseArray(JSONObject.toJSONString(lobbyDiscount)).toJSONString());
          qm.setGetWay(activityLobbyDTO.getGetWay());
          manageList.add(qm);
        }
        if (CollectionUtils.isNotEmpty(manageList)) {
          this.saveBatch(manageList);
        }
      }
    }
  }

  @Override
  public void auditStatus(ActivityQualificationAuditStatusDTO activityQualificationAuditStatusDTO) {
    List<ActivityQualification> qualificationManageStatusList =
        this.lambdaQuery()
            .in(ActivityQualification::getId, activityQualificationAuditStatusDTO.getIdList())
            .list();
    for (ActivityQualification qualification : qualificationManageStatusList) {
      if (qualification.getStatus() == 0) {
        throw new ServiceException("您选择的数据有无效数据，无效数据不能审核！");
      }
      if (qualification.getStatus() == 2) {
        throw new ServiceException(
            "您选择的数据有【" + qualification.getActivityName() + "】已审核的数据，请勿重复审核！");
      }
      if (qualification.getQualificationStatus() == 0) {
        throw new ServiceException("您选择的数据有资格状态被禁用的数据，禁用状态不能审核！");
      }
      // 更新数据
      qualification.setStatus(2);
      qualification.setAuditPerson(GlobalContextHolder.getContext().getUsername());
      qualification.setAuditTime(new Date());
    }
    // 批量更新数据
    if (!this.updateBatchById(qualificationManageStatusList)) {
      throw new ServiceException("审核失败！");
    }
    // 审核成功，添加派发记录
    List<Long> distributeIds = activityQualificationAuditStatusDTO.getIdList();
    List<ActivityQualification> activityQualificationList =
        this.lambdaQuery().in(ActivityQualification::getActivityId, distributeIds).list();

    List<ActivityDistribute> activityDistributeList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(activityQualificationList)) {
      ActivityDistribute ad;
      for (ActivityQualification activityQualification : activityQualificationList) {
        if (activityQualification.getActivityType() != 2) {
          // 插入活动大厅派发记录
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
      boolean result = activityDistributeService.saveDistributeBatch(activityDistributeList);
      if (result) {
        log.info("插入活动派发成功");
      }
    }
  }

  @Override
  public void updateQualificationStatus(
      ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO) {
    if (activityQualificationUpdateStatusDTO.getId() == null
        || activityQualificationUpdateStatusDTO.getId() == 0) {
      throw new ServiceException("资格id不能为空");
    }
    ActivityQualification activityQualification =
        this.getById(activityQualificationUpdateStatusDTO.getId());
    if (activityQualification == null) {
      throw new ServiceException("该活动资格不存在");
    }
    if (activityQualification.getStatus() == 2) {
      throw new ServiceException("审核通过的记录不能修改活动资格状态");
    }
    if (activityQualificationUpdateStatusDTO.getQualificationStatus() != null) {
      activityQualification.setQualificationStatus(
          activityQualificationUpdateStatusDTO.getQualificationStatus());
    }
    if (!this.updateById(activityQualification)) {
      throw new ServiceException("修改活动资格状态失败！");
    }
  }

  @Override
  public void updateQualificationStatus(ActivityQualification activityQualification) {
    LambdaUpdateWrapper<ActivityQualification> update = Wrappers.lambdaUpdate();
    update.set(ActivityQualification::getEmployNum, activityQualification.getEmployNum());
    update.set(ActivityQualification::getEmployTime, activityQualification.getEmployTime());
    update.eq(
        ActivityQualification::getQualificationActivityId,
        activityQualification.getQualificationActivityId());
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
    List<ActivityQualification> qualificationList =
        this.lambdaQuery().in(ActivityQualification::getId, idList).list();
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

  @Override
  public Map<String, Object> checkQualification(
      ActivityQualificationCheckDTO activityQualificationCheckDTO) {
    MemberInfoVO memberInfo = null;
    Map<String, Object> retMap = new HashMap<>(3);
    try {
      memberInfo = memberService.getMemberInfo(activityQualificationCheckDTO.getUsername());
      if (memberInfo == null) {
        throw new ServiceException("账号不存在,请输入真实有效的账号");
      }
      activityCommonService.userDetection(memberInfo, 3);
    } catch (ServiceException e) {
      retMap.put("step", 1);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }
    Date countDate =
        DateUtil.strToDate(activityQualificationCheckDTO.getCountDate(), "yyyy-MM-dd HH:mm:ss");
    ActivityLobby activityLobby = null;
    List<ActivityQualification> manageList;
    try {
      // 活动检测
      activityLobby =
          activityCommonService.activityDetection(
              activityQualificationCheckDTO.getActivityId(), countDate, 3);
    } catch (ServiceException e) {
      retMap.put("step", 2);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // 黑名单检测
      activityCommonService.blacklistDetection(activityLobby, memberInfo, 3);
    } catch (ServiceException e) {
      retMap.put("step", 3);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // 资格检测
      activityCommonService.qualificationDetection(activityLobby, memberInfo, countDate, 3);
    } catch (ServiceException e) {
      retMap.put("step", 4);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // 规则检测
      manageList =
          activityCommonService.activityRuleDetection(activityLobby, countDate, memberInfo, 3);
    } catch (ServiceException e) {
      retMap.put("step", 5);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    retMap.put("step", 0);
    retMap.put("success", true);
    retMap.put("message", "检测完毕,目前会员可领取的优惠金额:" + manageList.get(0).getMaxMoney());
    return retMap;
  }
}
