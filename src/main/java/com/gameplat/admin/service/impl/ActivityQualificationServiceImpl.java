package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityLobbyConvert;
import com.gameplat.admin.convert.ActivityQualificationConvert;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.ActivityQualificationMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.RandomUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.ActivityInfoEnum;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.common.enums.RechargeStatus;
import com.gameplat.model.entity.activity.ActivityDistribute;
import com.gameplat.model.entity.activity.ActivityLobby;
import com.gameplat.model.entity.activity.ActivityQualification;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import com.gameplat.security.SecurityUserHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ??????????????????
 *
 * @author kenvin
 */
@Slf4j
@Service
public class ActivityQualificationServiceImpl
    extends ServiceImpl<ActivityQualificationMapper, ActivityQualification>
    implements ActivityQualificationService {

  @Autowired private ActivityQualificationConvert activityQualificationConvert;

  @Autowired private ActivityDistributeService activityDistributeService;

  @Autowired private MemberService memberService;

  @Autowired private ActivityLobbyConvert activityLobbyConvert;

  @Autowired private ActivityCommonService activityCommonService;

  @Autowired private ActivityRedPacketService activityRedPacketService;

  @Autowired private RechargeOrderHistoryService rechargeOrderHistoryService;

  @Autowired private ActivityLobbyService activityLobbyService;

  @Autowired private MessageInfoService messageInfoService;

  private static final String EST4_BEGIN = " 12:00:00";

  private static final String EST4_END = " 11:59:59";

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
            activityQualification.getDeleteFlag() != null,
            ActivityQualification::getDeleteFlag,
            activityQualification.getDeleteFlag())
        .eq(
            activityQualification.getStatus() != null,
            ActivityQualification::getStatus,
            activityQualification.getStatus())
        .eq(ActivityQualification::getDeleteFlag, 1)
        .list();
  }

  @Override
  public IPage<ActivityQualificationVO> list(
      PageDTO<ActivityQualification> page, ActivityQualificationQueryDTO dto) {
    LambdaQueryChainWrapper<ActivityQualification> queryChainWrapper = this.lambdaQuery();
    queryChainWrapper
        .eq(
            dto.getActivityId() != null && dto.getActivityId() != 0,
            ActivityQualification::getActivityId,
            dto.getActivityId())
        .like(
            StringUtils.isNotBlank(dto.getUsername()),
            ActivityQualification::getUsername,
            dto.getUsername())
        .eq(dto.getStatus() != null, ActivityQualification::getStatus, dto.getStatus())
        .eq(ActivityQualification::getDeleteFlag, BooleanEnum.YES.value())//?????????????????????????????????
        .eq(
            dto.getQualificationStatus() != null,
            ActivityQualification::getQualificationStatus,
            dto.getQualificationStatus())
        .ge(ObjectUtils.isNotEmpty(dto.getApplyStartTime()), ActivityQualification::getApplyTime, dto.getApplyStartTime())
        .le(ObjectUtils.isNotEmpty(dto.getApplyEndTime()), ActivityQualification::getApplyTime, dto.getApplyEndTime())
        .orderByDesc(ActivityQualification::getCreateTime);
    return queryChainWrapper.page(page).convert(activityQualificationConvert::toVo);
  }

  @Override
  public void add(ActivityQualificationAddDTO dto) {
    Integer type = dto.getType();
    String username = dto.getUsername();
    if (type == null) {
      throw new ServiceException("????????????????????????");
    }
    if (StringUtils.isBlank(username)) {
      throw new ServiceException("?????????????????????");
    }
    List<String> usernameList = new ArrayList<>();
    if (username.contains(",")) {
      usernameList.addAll(Lists.newArrayList(username.split(",")));
    } else {
      usernameList.add(username);
    }
    List<ActivityQualification> manageList = new ArrayList<>();
    for (String username1 : usernameList) {
      MemberInfoVO memberInfo = memberService.getMemberInfo(username1);
      if (StringUtils.isNull(memberInfo)) {
        throw new ServiceException("?????????" + username1 + "????????????,?????????");
      }
      // 1 ????????????  2 ?????????  4 ????????????
      switch (type){
        case 1:
          addActivityLobbyQualification(dto,memberInfo,manageList);
          break;
        case 2:
          addRedPacketQualification(dto,memberInfo,manageList);
          break;
        case 4:
          addActivityTurntableQualification(dto,memberInfo,manageList);
          break;
        default:
          throw new ServiceException("??????????????????");
      }
    }
    if (CollectionUtils.isNotEmpty(manageList)) {
      this.saveBatch(manageList);
    }
  }

  private void addActivityLobbyQualification(ActivityQualificationAddDTO dto,MemberInfoVO memberInfo,List<ActivityQualification> manageList){
    List<ActivityLobbyDTO> activityLobbyList = dto.getActivityLobbyList();
    if (CollectionUtils.isEmpty(activityLobbyList)) {
      throw new ServiceException("??????????????????????????????");
    }

    ActivityQualification qm;
    for (ActivityLobbyDTO activityLobbyDTO : activityLobbyList) {
      List<ActivityLobbyDiscountDTO> lobbyDiscount = activityLobbyDTO.getLobbyDiscount();
      if (activityLobbyDTO.getMultipleHandsel() == 0 && lobbyDiscount.size() > 1) {
        throw new ServiceException("???" + activityLobbyDTO.getTitle() + "??????????????????????????????");
      }
      qm = new ActivityQualification();
      ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyDTO);
      String auditRemark =
              activityCommonService.getAuditRemark(activityLobby, "", "", null, null);
      qm.setAuditRemark(auditRemark);
      qm.setActivityId(activityLobbyDTO.getId());
      qm.setActivityName(activityLobbyDTO.getTitle());
      if(activityLobbyDTO.getActivityType() == null){
        throw new ServiceException("????????????????????????");
      }
      qm.setActivityType(dto.getType());
      qm.setUserId(memberInfo.getId());
      qm.setUsername(memberInfo.getAccount());
      qm.setApplyTime(new Date());
      qm.setStatus(BooleanEnum.YES.value());
      qm.setActivityStartTime(activityLobbyDTO.getStartTime());
      qm.setActivityEndTime(activityLobbyDTO.getEndTime());
      qm.setDeleteFlag(BooleanEnum.YES.value());
      qm.setDrawNum(1);
      qm.setEmployNum(0);
      qm.setQualificationActivityId(IdWorker.getIdStr());
      qm.setQualificationStatus(BooleanEnum.YES.value());
      qm.setStatisItem(activityLobbyDTO.getStatisItem());
      BigDecimal maxMoney = BigDecimal.ZERO;
      BigDecimal withdrawDml = BigDecimal.ZERO;
      //?????????1??????????????????
      if(activityLobby.getRewardCalculateType() == 1){
        maxMoney = lobbyDiscount.stream().map(ActivityLobbyDiscountDTO::getPresenterValue).reduce(BigDecimal::add).get();
        withdrawDml =lobbyDiscount.stream().map(ActivityLobbyDiscountDTO::getWithdrawDml).reduce(BigDecimal::add).get();
        //?????????2?????????????????????
      } else if(activityLobby.getRewardCalculateType() == 2){
        for(ActivityLobbyDiscountDTO item : lobbyDiscount){
          BigDecimal presentMoney = new BigDecimal(item.getTargetValue()).multiply(item.getPresenterValue().divide(new BigDecimal("100")));
          BigDecimal presentDml = presentMoney.multiply(item.getWithdrawDml());
          item.setPresenterValue(presentMoney);
          item.setWithdrawDml(presentDml);
          maxMoney = maxMoney.add(presentMoney);
          withdrawDml = withdrawDml.add(presentDml);
        }
      }
      qm.setMaxMoney(maxMoney);
      qm.setWithdrawDml(withdrawDml);
      qm.setSoleIdentifier(SecureUtil.md5(cn.hutool.core.date.DateUtil.formatDate(new Date())));
      lobbyDiscount.sort(Comparator.comparingLong(ActivityLobbyDiscountDTO::getTargetValue));
      qm.setAwardDetail(JSON.parseArray(JSONObject.toJSONString(lobbyDiscount)).toJSONString());
      qm.setGetWay(activityLobbyDTO.getGetWay());
      manageList.add(qm);
    }
  }

  private void addRedPacketQualification(ActivityQualificationAddDTO dto,MemberInfoVO memberInfo,List<ActivityQualification> manageList){
    //????????????????????????
    List<ActivityLobbyDTO> redPacketConditionList=dto.getMemberRedPacketDTO();
    if(CollectionUtils.isEmpty(redPacketConditionList)){
      throw new ServiceException("??????????????????");
    }
    //??????????????????????????????2?????????????????????
    ActivityLobbyDTO activityLobbyDTO=redPacketConditionList.get(0);

    ActivityQualification qm = new ActivityQualification();
    ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyDTO);
    String auditRemark =activityCommonService.getAuditRemark(activityLobby, "", "", null, null);
    qm.setAuditRemark(auditRemark);
    qm.setActivityId(activityLobbyDTO.getId());
    qm.setActivityName(activityLobbyDTO.getTitle());
    if(activityLobbyDTO.getActivityType() == null){
      throw new ServiceException("????????????????????????");
    }
    qm.setUserId(memberInfo.getId());
    qm.setUsername(memberInfo.getAccount());
    qm.setApplyTime(new Date());
    qm.setStatus(BooleanEnum.YES.value());
    qm.setActivityStartTime(activityLobbyDTO.getStartTime());
    qm.setActivityEndTime(activityLobbyDTO.getEndTime());
    qm.setDeleteFlag(BooleanEnum.YES.value());
    qm.setEmployNum(0);
    qm.setQualificationActivityId(IdWorker.getIdStr());
    qm.setQualificationStatus(BooleanEnum.YES.value());
    qm.setStatisItem(activityLobbyDTO.getStatisItem());
    qm.setGetWay(activityLobbyDTO.getGetWay());
    qm.setSoleIdentifier(RandomUtil.generateOrderCode());

    List<ActivityLobbyDiscountDTO> list=activityLobbyDTO.getRedPacketConditionList();
    if(CollectionUtils.isEmpty(list)){
      throw new ServiceException("???????????????????????????");
    }
    //??????????????????????????????2?????????????????????
    ActivityLobbyDiscountDTO temp=list.get(0);
    qm.setMinMoney(temp.getPresenterValue());
    qm.setMaxMoney(temp.getWithdrawDml());
    qm.setDrawNum(temp.getPresenterDml().intValue());
    qm.setActivityType(ActivityInfoEnum.TypeEnum.RED_ENVELOPE.value());
    manageList.add(qm);
  }

  private void addActivityTurntableQualification(ActivityQualificationAddDTO dto,MemberInfoVO memberInfo,List<ActivityQualification> manageList){
    if(dto==null || CollectionUtils.isEmpty(dto.getMemberRedPacketDTO())){
      throw new ServiceException("??????????????????");
    }
    //??????????????????????????????2?????????????????????
    ActivityLobbyDTO activityLobbyDTO=dto.getMemberRedPacketDTO().get(0);

    ActivityQualification qm = new ActivityQualification();
    ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyDTO);
    String auditRemark =activityCommonService.getAuditRemark(activityLobby, "", "", null, null);
    qm.setAuditRemark(auditRemark);
    qm.setActivityId(activityLobbyDTO.getId());
    qm.setActivityName(activityLobbyDTO.getTitle());
    if(activityLobbyDTO.getActivityType() == null){
      throw new ServiceException("????????????????????????");
    }
    qm.setUserId(memberInfo.getId());
    qm.setUsername(memberInfo.getAccount());
    qm.setApplyTime(new Date());
    qm.setStatus(BooleanEnum.YES.value());
    qm.setActivityStartTime(activityLobbyDTO.getStartTime());
    qm.setActivityEndTime(activityLobbyDTO.getEndTime());
    qm.setDeleteFlag(BooleanEnum.YES.value());
    qm.setEmployNum(0);
    qm.setQualificationActivityId(IdWorker.getIdStr());
    qm.setQualificationStatus(BooleanEnum.YES.value());
    qm.setStatisItem(activityLobbyDTO.getStatisItem());
    qm.setGetWay(activityLobbyDTO.getGetWay());
    qm.setSoleIdentifier(RandomUtil.generateOrderCode());

    List<ActivityLobbyDiscountDTO> list=activityLobbyDTO.getRedPacketConditionList();
    if(CollectionUtils.isEmpty(list)){
      throw new ServiceException("???????????????????????????");
    }
    //??????????????????????????????2?????????????????????
    ActivityLobbyDiscountDTO temp=list.get(0);
    qm.setMinMoney(temp.getPresenterValue());
    qm.setMaxMoney(temp.getWithdrawDml());
    qm.setDrawNum(temp.getPresenterDml().intValue());
    qm.setActivityType(4);
    manageList.add(qm);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void auditStatus(ActivityQualificationAuditStatusDTO dto) {
    List<ActivityQualification> qualificationManageStatusList =
        this.lambdaQuery().in(ActivityQualification::getId, dto.getIdList()).list();
    if (CollectionUtils.isEmpty(qualificationManageStatusList)) {
      throw new ServiceException("???????????????????????????");
    }
    for (ActivityQualification qualification : qualificationManageStatusList) {
      if (qualification.getStatus() == ActivityInfoEnum.QualificationStatus.INVALID.value()) {
        throw new ServiceException("???????????????????????????????????????????????????????????????");
      }
      if (qualification.getStatus() == ActivityInfoEnum.QualificationStatus.AUDITED.value()) {
        throw new ServiceException(
            "????????????????????????" + qualification.getActivityName() + "?????????????????????????????????????????????");
      }
      if (qualification.getQualificationStatus() == BooleanEnum.NO.value()) {
        throw new ServiceException("?????????????????????????????????????????????????????????????????????????????????");
      }
      // ????????????
      qualification.setStatus(ActivityInfoEnum.QualificationStatus.AUDITED.value());
      String remark = "";
      if (ObjectUtils.isNotNull(dto.getAdjustAmount())
        && qualification.getMaxMoney().compareTo(dto.getAdjustAmount()) != 0) {
        if(qualification.getActivityType() != 1){
          throw new ServiceException("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
        }
        qualification.setMaxMoney(dto.getAdjustAmount());
        remark = remark + "????????????????????????" + dto.getAdjustAmount();
      }
      if (ObjectUtils.isNotNull(dto.getAdjustDml())
        && qualification.getWithdrawDml().compareTo(dto.getAdjustDml()) != 0) {
        if(qualification.getActivityType() != 1){
          throw new ServiceException("???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
        }
        qualification.setWithdrawDml(dto.getAdjustDml());
        remark = remark + "???????????????????????????" + dto.getAdjustDml();
      }
      if (ObjectUtils.isNotEmpty(remark)) {
        qualification.setRemark("????????????" + SecurityUserHolder.getUsername() + remark);
      }
      qualification.setAuditPerson(GlobalContextHolder.getContext().getUsername());
      qualification.setAuditTime(new Date());
    }
    // ??????????????????
    if (!this.updateBatchById(qualificationManageStatusList)) {
      throw new ServiceException("???????????????");
    }
    // ?????????????????????????????????
    List<ActivityDistribute> activityDistributeList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(qualificationManageStatusList)) {
      ActivityDistribute ad;
      for (ActivityQualification activityQualification : qualificationManageStatusList) {
        // ??????????????????????????????
        if (activityQualification.getActivityType() == 1) {
          Member member = memberService.getById(activityQualification.getUserId());
          // ??????????????????????????????
          ad = new ActivityDistribute();
          ad.setUserId(Long.getLong(activityQualification.getUserId().toString()));
          ad.setUsername(activityQualification.getUsername());
          ad.setApplyTime(new Date());
          ad.setActivityId(activityQualification.getActivityId());
          ad.setActivityType(1);
          ad.setActivityName(activityQualification.getActivityName());
          ad.setUserId(activityQualification.getUserId());
          ad.setStatus(BooleanEnum.YES.value());
          //                ad.setDisabled(1);
          ad.setDeleteFlag(BooleanEnum.YES.value());
          ad.setQualificationActivityId(activityQualification.getQualificationActivityId());
          ad.setStatisItem(activityQualification.getStatisItem());
          if (ObjectUtils.isNull(dto.getAdjustAmount())) {
            ad.setDiscountsMoney(activityQualification.getMaxMoney());
          } else {
            ad.setDiscountsMoney(dto.getAdjustAmount());
          }
          if (ObjectUtils.isNull(dto.getAdjustDml())) {
            ad.setWithdrawDml(activityQualification.getWithdrawDml());
          } else {
            ad.setWithdrawDml(dto.getAdjustDml());
          }
          ad.setSoleIdentifier(activityQualification.getSoleIdentifier());
          ad.setStatisStartTime(activityQualification.getStatisStartTime());
          ad.setStatisEndTime(activityQualification.getStatisEndTime());
          ad.setGetWay(activityQualification.getGetWay());
          ad.setParentId(member.getParentId());
          ad.setParentName(member.getParentName());
          ad.setAgentPath(member.getSuperPath());
          activityDistributeList.add(ad);
        }
      }
    }
    if (CollectionUtils.isNotEmpty(activityDistributeList)) {
      boolean result = activityDistributeService.saveDistributeBatch(activityDistributeList);
      if (result) {
        log.info("????????????????????????");
      }
    }
  }

  @Override
  public void updateQualificationStatus(ActivityQualificationUpdateStatusDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("??????id????????????");
    }
    ActivityQualification activityQualification = this.getById(dto.getId());
    if (activityQualification == null) {
      throw new ServiceException("????????????????????????");
    }
    if (activityQualification.getStatus() == 2) {
      throw new ServiceException("???????????????????????????????????????????????????");
    }
    if (dto.getQualificationStatus() != null) {
      activityQualification.setQualificationStatus(dto.getQualificationStatus());
    }
    if (!this.updateById(activityQualification)) {
      throw new ServiceException("?????????????????????????????????");
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
      throw new ServiceException("ids????????????");
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
        throw new ServiceException("????????????");
      }
    }
  }

  @Override
  public Map<String, Object> checkQualification(ActivityQualificationCheckDTO dto) {
    MemberInfoVO memberInfo = null;
    Map<String, Object> retMap = new HashMap<>(6);
    try {
      memberInfo = memberService.getMemberInfo(dto.getUsername());
      if (memberInfo == null) {
        throw new ServiceException("???????????????,??????????????????????????????");
      }
      activityCommonService.userDetection(memberInfo, 3);
    } catch (ServiceException e) {
      log.error("step1????????????:{}", e);
      retMap.put("step", 1);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }
    Date countDate = DateUtil.strToDate(dto.getCountDate(), "yyyy-MM-dd HH:mm:ss");
    ActivityLobby activityLobby = null;
    List<ActivityQualification> manageList;
    try {
      // ????????????
      activityLobby = activityCommonService.activityDetection(dto.getActivityId(), countDate, 3);
    } catch (ServiceException e) {
      log.error("step2????????????:{}", e);
      retMap.put("step", 2);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // ???????????????
      activityCommonService.blacklistDetection(activityLobby, memberInfo, 3);
    } catch (ServiceException e) {
      log.error("step3????????????:{}", e);
      retMap.put("step", 3);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // ????????????
      activityCommonService.qualificationDetection(activityLobby, memberInfo, countDate, 3);
    } catch (ServiceException e) {
      log.error("step4????????????:{}", e);
      retMap.put("step", 4);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // ????????????
      manageList =
          activityCommonService.activityRuleDetection(activityLobby, countDate, memberInfo, 3);
    } catch (ServiceException e) {
      log.error("step5????????????:{}", e);
      retMap.put("step", 5);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    retMap.put("step", 0);
    retMap.put("success", true);
    retMap.put("message", "????????????,????????????????????????????????????:" + manageList.get(0).getMaxMoney());
    return retMap;
  }

  @Override
  public void activityRedEnvelopeQualification() {
    try {
      long start = System.currentTimeMillis();
      log.info("?????????????????????????????????");

      //????????????
      cleanRedEnvelopeQualification();

      //?????????????????????ID???????????????
      ActivityRedPacketConfigVO config = activityRedPacketService.getConfig();
      Integer isAllowProxyJoin = config.getIsAllowProxyJoin() != null ? config.getIsAllowProxyJoin(): 0;
      if (config == null) {
        log.info("???????????????????????????,?????????????????????");
      } else {
        //?????????????????????????????????
        Long redenvelopeId = config.getRedenvelopeId();

        if (redenvelopeId == null || redenvelopeId <= 0) {
          log.info("???????????????id??????,redenvelopeId={}", redenvelopeId);
        } else {
          //??????????????????
          ActivityLobbyVO activityLobby = activityLobbyService.getActivityLobbyVOById(redenvelopeId);

          if (activityLobby == null) {
            log.info("????????????????????????,ActivityLobby id={}", redenvelopeId);
          } else {
            //??????????????????ok
            if (checkDoRedEnvelope(activityLobby)) {
              List<Map<String, Object>> rechargeOrderList = getRechargeOrder(isAllowProxyJoin);

              if (CollectionUtils.isEmpty(rechargeOrderList)) {
                log.info("?????????????????????????????????");
              } else {
                Set blackSet = null,userLevelsSet = null;

                //?????????????????????
                String activityBalcklist = config.getActivityBalcklist();
                if (StringUtils.isNotEmpty(activityBalcklist)) {
                  blackSet = Arrays.stream(activityBalcklist.split(",")).collect(Collectors.toSet());
                }

                //?????????????????????????????????????????????
                String userLevels = activityLobby.getGraded();
                if (StringUtils.isNotEmpty(userLevels)) {
                  userLevelsSet = Arrays.stream(userLevels.split(",")).collect(Collectors.toSet());
                }

                //?????????????????????????????????
                List<ActivityQualification> existActivityQualificationList = this.lambdaQuery()
                        .eq(ActivityQualification::getActivityId,activityLobby.getId())
                        .eq(ActivityQualification::getActivityType, ActivityInfoEnum.TypeEnum.RED_ENVELOPE.value())
                        .between(ActivityQualification::getCreateTime,DateUtil.getDateStart(new Date()),DateUtil.getDateEnd(new Date()))
                        .list();

                doRedEnvelopeQualification(blackSet, userLevelsSet, activityLobby, rechargeOrderList, existActivityQualificationList);
              }
            }
          }
        }
      }
      log.info("?????????????????????????????????,?????????{}ms", System.currentTimeMillis() - start);
    } catch (Exception e) {
      log.error("?????????????????????????????????", e);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void refuse(ActivityQualificationRefuseDTO dto) {
    for (Long id : dto.getIdList()) {
      ActivityQualification activityQualification = this.lambdaQuery()
        .eq(ActivityQualification::getId, id)
        .eq(ActivityQualification::getDeleteFlag, 1)
        .one();
      if (ObjectUtils.isNull(activityQualification)) {
        throw new ServiceException("?????????????????????");
      }
      if (activityQualification.getActivityType() != 1) {
        throw new ServiceException("???????????????????????????????????????????????????????????????????????????????????????");
      }
      if (activityQualification.getStatus() == ActivityInfoEnum.QualificationStatus.AUDITED.value()) {
        throw new ServiceException("????????????????????????????????????????????????");
      }

      this.removeById(id);

      // ?????? ???????????????
      MessageInfoAddDTO message = new MessageInfoAddDTO();
      message.setContent(dto.getRefuseReason());
      message.setTitle(activityQualification.getActivityName() + ",?????????????????????");
      message.setPushRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
      message.setLinkAccount(activityQualification.getUsername());
      message.setCategory(PushMessageEnum.MessageCategory.SYS_SEND.getValue());
      message.setPosition(PushMessageEnum.Location.LOCATION_DEF.getValue());
      message.setShowType(PushMessageEnum.MessageShowType.SHOW_DEF.value());
      message.setPopsCount(PushMessageEnum.PopCount.POP_COUNT_DEF.getValue());
      message.setType(PushMessageEnum.MessageType.SYSTEM_INFORMATION.value());
      message.setCreateBy(SecurityUserHolder.getUsername());
      messageInfoService.insertMessage(message);
    }
  }

  /**
   * ???????????????????????????
   * @param activityLobby
   * @return
   */
  private boolean checkDoRedEnvelope(ActivityLobbyVO activityLobby) {
    Date now = new Date();
    if (1!=activityLobby.getStatus()) {
      log.info("??????????????????");
      return false;
    }
    if (!now.after(activityLobby.getStartTime())) {
      log.info("??????????????????,???????????????????????????{}", activityLobby.getStartTime());
      return false;
    }
    if (!now.before(activityLobby.getEndTime())) {
      log.info("???????????????,???????????????????????????{}", activityLobby.getEndTime());
      return false;
    }
    return true;
  }

  /**
   * ?????????????????????????????????
   * @param blackSet
   * @param userLevelsSet
   * @param activityLobby
   * @param list
   * @param existActivityQualificationList
   */
  private void doRedEnvelopeQualification(Set blackSet,Set userLevelsSet,ActivityLobbyVO activityLobby,
                                          List<Map<String, Object>> list,List<ActivityQualification> existActivityQualificationList){
    //????????????????????????????????????????????????
    Map<String, BigDecimal> newMap = new HashMap<>();

    //???????????????id???????????????
    Map<String, Long> nameAndId = new HashMap<>();

    log.info("?????????{}?????????", list==null?0:list.size());
    for (Map<String, Object> map : list) {
      String dayTotalAmount = map.get("dayTotalAmount").toString();
      String account = map.get("account").toString();
      String memberLevel = map.get("memberLevel").toString();
      nameAndId.put(account,new Long(map.get("memberId").toString()));

      //?????????????????????
      if (blackSet != null && blackSet.contains(account)) {
        log.info("??????={}???????????????,??????={}????????????", account, dayTotalAmount);
        continue;
      }

      //??????????????????????????????
      if (userLevelsSet != null && !userLevelsSet.contains(memberLevel)) {
        log.info("??????={}???????????????={}?????????={}????????????????????????", account, memberLevel,dayTotalAmount);
        continue;
      }

      //????????????????????????,???????????????
      if(checkExistActivityQualification(existActivityQualificationList,account)){
        log.info("??????={}??????????????????", account);
        continue;
      }

      //????????????????????????????????????
      if (newMap.containsKey(account)) {
        BigDecimal dayTotalAmountTemp = newMap.get(account);
        newMap.put(account, dayTotalAmountTemp.add(new BigDecimal(dayTotalAmount)));
      } else {
        newMap.put(account, new BigDecimal(dayTotalAmount));
      }
    }

    List<ActivityQualification> needAdd=new ArrayList<>();
    for (Map.Entry<String, BigDecimal> entry : newMap.entrySet()) {
      ActivityLobbyDiscountVO temp=getActivityLobbyDiscountVO(entry.getValue(),activityLobby.getLobbyDiscountList());
      if(temp==null){
        log.info("????????????????????????????????????????????????account={},{}",entry.getKey(),temp);
        continue;
      }
      ActivityQualification qm=new ActivityQualification();
      qm.setAuditRemark("?????????????????????????????????,????????????="+entry.getValue());
      qm.setAuditPerson("system");
      qm.setAuditTime(new Date());
      qm.setActivityId(activityLobby.getId());
      qm.setActivityName(activityLobby.getTitle());
      qm.setActivityType(ActivityInfoEnum.TypeEnum.RED_ENVELOPE.value());
      qm.setUserId(nameAndId.get(entry.getKey()));
      qm.setUsername(entry.getKey());
      qm.setApplyTime(new Date());
      qm.setStatus(ActivityInfoEnum.QualificationStatus.AUDITED.value());
      qm.setActivityStartTime(activityLobby.getStartTime());
      qm.setActivityEndTime(activityLobby.getEndTime());
      //YES=1 ???????????????
      qm.setDeleteFlag(BooleanEnum.YES.value());
//      qm.setQualificationActivityId(IdWorker.getIdStr());
//      qm.setAwardDetail("");
      qm.setSoleIdentifier(RandomUtil.generateOrderCode());
      //YES=1 ????????????
      qm.setQualificationStatus(BooleanEnum.YES.value());
      qm.setStatisItem(activityLobby.getStatisItem());
      qm.setGetWay(ActivityInfoEnum.GetWay.DIRECT_RELEASE.value());
      qm.setDrawNum(temp.getPresenterValue().intValue());
      //?????????????????????????????????????????????
      qm.setWithdrawDml(BigDecimal.ZERO);
      //??????????????????,???????????????0
      qm.setEmployNum(0);
      qm.setMinMoney(temp.getPresenterDml());
      qm.setMaxMoney(temp.getWithdrawDml());
      needAdd.add(qm);
    }

    //??????????????????
    this.saveBatch(needAdd);
  }

  /**
   * ????????????????????????????????????
   * @param money
   * @param lobbyDiscountList
   * @return
   */
  private ActivityLobbyDiscountVO getActivityLobbyDiscountVO(BigDecimal money, List<ActivityLobbyDiscountVO> lobbyDiscountList) {
    if (CollectionUtils.isEmpty(lobbyDiscountList)) {
      return null;
    }
    //money<=0
    if (money == null || money.compareTo(BigDecimal.ZERO) == 0 || money.compareTo(BigDecimal.ZERO) == -1) {
      return null;
    }
    //???????????????????????????
    lobbyDiscountList.sort(Comparator.comparingLong(ActivityLobbyDiscountVO::getTargetValue).reversed());
    for (int i = 0; i < lobbyDiscountList.size(); i++) {
      ActivityLobbyDiscountVO temp = lobbyDiscountList.get(i);
      //targetValue: ????????????
      BigDecimal targetValue = new BigDecimal(temp.getTargetValue());
      //money>=targetValue
      if (money.compareTo(targetValue) == 0 || money.compareTo(targetValue) == 1) {
        return temp;
      }
    }
    return null;
  }

  /**
   * ???????????????????????????????????????????????????????????????
   * 1.????????????????????????12??????????????????12???
   * 2.????????????????????????
   * 3.?????????????????????????????????
   * 4.?????????????????????
   * 5.???????????????????????????
   * @return
   */
  private List<Map<String, Object>> getRechargeOrder(Integer isAllowProxyJoin) {
    QueryWrapper<RechargeOrderHistory> rechargeOrderHistoryQueryWrapper = new QueryWrapper<>();
    rechargeOrderHistoryQueryWrapper.select("IFNULL(sum(amount),0) as dayTotalAmount,member_id as memberId,account,member_level as memberLevel")
            .and(wrapper -> wrapper.eq("member_type", MemberEnums.Type.MEMBER.value()).or().eq(isAllowProxyJoin == 1,"member_type", MemberEnums.Type.AGENT.value()))
            .eq("status", RechargeStatus.SUCCESS.getValue())
            .eq("point_flag", 1)
            .between("audit_time",
                    DateUtil.strToDate(LocalDate.now().plusDays(-1).toString() + EST4_BEGIN, DateUtil.YYYY_MM_DD_HH_MM_SS),
                    DateUtil.strToDate(LocalDate.now().toString() + EST4_END, DateUtil.YYYY_MM_DD_HH_MM_SS))
            .groupBy("member_id,member_level")
            .having("sum(amount)>0");
    return rechargeOrderHistoryService.listMaps(rechargeOrderHistoryQueryWrapper);
  }

  /**
   * ??????45???????????????
   */
  private void cleanRedEnvelopeQualification() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -45);
    Date endTime = DateUtil.getDateEnd(calendar.getTime());
    QueryWrapper<ActivityQualification> delWrapper = new QueryWrapper<>();
    delWrapper.eq("activity_type", ActivityInfoEnum.TypeEnum.RED_ENVELOPE.value());
    delWrapper.le("create_time", endTime);
    this.remove(delWrapper);
  }

  /**
   * ??????????????????????????????????????????
   *
   * @param existActivityQualificationlist
   * @param account
   * @return
   */
  private boolean checkExistActivityQualification(List<ActivityQualification> existActivityQualificationlist, String account) {
    if (CollectionUtils.isEmpty(existActivityQualificationlist)) {
      return false;
    }
    for (ActivityQualification temp : existActivityQualificationlist) {
      if (temp!=null && temp.getUsername().equals(account)) {
        return true;
      }
    }
    return false;
  }

}
