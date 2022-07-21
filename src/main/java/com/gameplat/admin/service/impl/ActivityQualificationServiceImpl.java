package com.gameplat.admin.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动资格管理
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
        .eq(ActivityQualification::getDeleteFlag, BooleanEnum.YES.value())//添加是否删除的查询标识
        .eq(
            dto.getQualificationStatus() != null,
            ActivityQualification::getQualificationStatus,
            dto.getQualificationStatus())
        .orderByDesc(ActivityQualification::getCreateTime);
    return queryChainWrapper.page(page).convert(activityQualificationConvert::toVo);
  }

  @Override
  public void add(ActivityQualificationAddDTO dto) {
    Integer type = dto.getType();
    String username = dto.getUsername();
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
    List<ActivityQualification> manageList = new ArrayList<>();
    for (String username1 : usernameList) {
      MemberInfoVO memberInfo = memberService.getMemberInfo(username1);
      if (StringUtils.isNull(memberInfo)) {
        throw new ServiceException("用户【" + username1 + "】不存在,请检查");
      }
      // 1 活动大厅  2 红包雨  4 转盘活动
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
          throw new ServiceException("活动类型不对");
      }
    }
    if (CollectionUtils.isNotEmpty(manageList)) {
      this.saveBatch(manageList);
    }
  }

  private void addActivityLobbyQualification(ActivityQualificationAddDTO dto,MemberInfoVO memberInfo,List<ActivityQualification> manageList){
    List<ActivityLobbyDTO> activityLobbyList = dto.getActivityLobbyList();
    if (CollectionUtils.isEmpty(activityLobbyList)) {
      throw new ServiceException("活动大厅数据不能为空");
    }

    ActivityQualification qm;
    for (ActivityLobbyDTO activityLobbyDTO : activityLobbyList) {
      List<ActivityLobbyDiscountDTO> lobbyDiscount = activityLobbyDTO.getLobbyDiscount();
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
      if(activityLobbyDTO.getActivityType() == null){
        throw new ServiceException("活动类型不能为空");
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
      //如果是1则是固定金额
      if(activityLobby.getRewardCalculateType() == 1){
        maxMoney = lobbyDiscount.stream().map(ActivityLobbyDiscountDTO::getPresenterValue).reduce(BigDecimal::add).get();
        withdrawDml =lobbyDiscount.stream().map(ActivityLobbyDiscountDTO::getWithdrawDml).reduce(BigDecimal::add).get();
        //如果是2则是百分比金额
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
      qm.setSoleIdentifier(RandomUtil.generateOrderCode());
      lobbyDiscount.sort(Comparator.comparingLong(ActivityLobbyDiscountDTO::getTargetValue));
      qm.setAwardDetail(JSON.parseArray(JSONObject.toJSONString(lobbyDiscount)).toJSONString());
      qm.setGetWay(activityLobbyDTO.getGetWay());
      manageList.add(qm);
    }
  }

  private void addRedPacketQualification(ActivityQualificationAddDTO dto,MemberInfoVO memberInfo,List<ActivityQualification> manageList){
    //如果是红包雨活动
    List<ActivityLobbyDTO> redPacketConditionList=dto.getMemberRedPacketDTO();
    if(CollectionUtils.isEmpty(redPacketConditionList)){
      throw new ServiceException("活动数据不对");
    }
    //只能选择一个，如果传2个，就选第一个
    ActivityLobbyDTO activityLobbyDTO=redPacketConditionList.get(0);

    ActivityQualification qm = new ActivityQualification();
    ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyDTO);
    String auditRemark =activityCommonService.getAuditRemark(activityLobby, "", "", null, null);
    qm.setAuditRemark(auditRemark);
    qm.setActivityId(activityLobbyDTO.getId());
    qm.setActivityName(activityLobbyDTO.getTitle());
    if(activityLobbyDTO.getActivityType() == null){
      throw new ServiceException("活动类型不能为空");
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
      throw new ServiceException("请选择一个优惠区间");
    }
    //只能选择一个，如果传2个，就选第一个
    ActivityLobbyDiscountDTO temp=list.get(0);
    qm.setMinMoney(temp.getPresenterValue());
    qm.setMaxMoney(temp.getWithdrawDml());
    qm.setDrawNum(temp.getPresenterDml().intValue());
    qm.setActivityType(ActivityInfoEnum.TypeEnum.RED_ENVELOPE.value());
    manageList.add(qm);
  }

  private void addActivityTurntableQualification(ActivityQualificationAddDTO dto,MemberInfoVO memberInfo,List<ActivityQualification> manageList){
    if(dto==null || CollectionUtils.isEmpty(dto.getMemberRedPacketDTO())){
      throw new ServiceException("活动数据不对");
    }
    //只能选择一个，如果传2个，就选第一个
    ActivityLobbyDTO activityLobbyDTO=dto.getMemberRedPacketDTO().get(0);

    ActivityQualification qm = new ActivityQualification();
    ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyDTO);
    String auditRemark =activityCommonService.getAuditRemark(activityLobby, "", "", null, null);
    qm.setAuditRemark(auditRemark);
    qm.setActivityId(activityLobbyDTO.getId());
    qm.setActivityName(activityLobbyDTO.getTitle());
    if(activityLobbyDTO.getActivityType() == null){
      throw new ServiceException("活动类型不能为空");
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
      throw new ServiceException("请选择一个优惠区间");
    }
    //只能选择一个，如果传2个，就选第一个
    ActivityLobbyDiscountDTO temp=list.get(0);
    qm.setMinMoney(temp.getPresenterValue());
    qm.setMaxMoney(temp.getWithdrawDml());
    qm.setDrawNum(temp.getPresenterDml().intValue());
    qm.setActivityType(4);
    manageList.add(qm);
  }

  @Override
  public void auditStatus(ActivityQualificationAuditStatusDTO dto) {
    List<ActivityQualification> qualificationManageStatusList =
        this.lambdaQuery().in(ActivityQualification::getId, dto.getIdList()).list();
    if (CollectionUtils.isEmpty(qualificationManageStatusList)) {
      throw new ServiceException("您审核资格不存在！");
    }
    for (ActivityQualification qualification : qualificationManageStatusList) {
      if (qualification.getStatus() == ActivityInfoEnum.QualificationStatus.INVALID.value()) {
        throw new ServiceException("您选择的数据有无效数据，无效数据不能审核！");
      }
      if (qualification.getStatus() == ActivityInfoEnum.QualificationStatus.AUDITED.value()) {
        throw new ServiceException(
            "您选择的数据有【" + qualification.getActivityName() + "】已审核的数据，请勿重复审核！");
      }
      if (qualification.getQualificationStatus() == BooleanEnum.NO.value()) {
        throw new ServiceException("您选择的数据有资格状态被禁用的数据，禁用状态不能审核！");
      }
      // 更新数据
      qualification.setStatus(ActivityInfoEnum.QualificationStatus.AUDITED.value());
      qualification.setAuditPerson(GlobalContextHolder.getContext().getUsername());
      qualification.setAuditTime(new Date());
    }
    // 批量更新数据
    if (!this.updateBatchById(qualificationManageStatusList)) {
      throw new ServiceException("审核失败！");
    }
    // 审核成功，添加派发记录
    List<ActivityDistribute> activityDistributeList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(qualificationManageStatusList)) {
      ActivityDistribute ad;
      for (ActivityQualification activityQualification : qualificationManageStatusList) {
        // 大厅活动添加派发记录
        if (activityQualification.getActivityType() == 1) {
          // 插入活动大厅派发记录
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
  public void updateQualificationStatus(ActivityQualificationUpdateStatusDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("资格id不能为空");
    }
    ActivityQualification activityQualification = this.getById(dto.getId());
    if (activityQualification == null) {
      throw new ServiceException("该活动资格不存在");
    }
    if (activityQualification.getStatus() == 2) {
      throw new ServiceException("审核通过的记录不能修改活动资格状态");
    }
    if (dto.getQualificationStatus() != null) {
      activityQualification.setQualificationStatus(dto.getQualificationStatus());
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
  public Map<String, Object> checkQualification(ActivityQualificationCheckDTO dto) {
    MemberInfoVO memberInfo = null;
    Map<String, Object> retMap = new HashMap<>(6);
    try {
      memberInfo = memberService.getMemberInfo(dto.getUsername());
      if (memberInfo == null) {
        throw new ServiceException("账号不存在,请输入真实有效的账号");
      }
      activityCommonService.userDetection(memberInfo, 3);
    } catch (ServiceException e) {
      log.error("step1出现异常:{}", e);
      retMap.put("step", 1);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }
    Date countDate = DateUtil.strToDate(dto.getCountDate(), "yyyy-MM-dd HH:mm:ss");
    ActivityLobby activityLobby = null;
    List<ActivityQualification> manageList;
    try {
      // 活动检测
      activityLobby = activityCommonService.activityDetection(dto.getActivityId(), countDate, 3);
    } catch (ServiceException e) {
      log.error("step2出现异常:{}", e);
      retMap.put("step", 2);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // 黑名单检测
      activityCommonService.blacklistDetection(activityLobby, memberInfo, 3);
    } catch (ServiceException e) {
      log.error("step3出现异常:{}", e);
      retMap.put("step", 3);
      retMap.put("success", false);
      retMap.put("message", e.getMessage());
      return retMap;
    }

    try {
      // 资格检测
      activityCommonService.qualificationDetection(activityLobby, memberInfo, countDate, 3);
    } catch (ServiceException e) {
      log.error("step4出现异常:{}", e);
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
      log.error("step5出现异常:{}", e);
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

  @Override
  public void activityRedEnvelopeQualification() {
    try {
      long start = System.currentTimeMillis();
      log.info("生成当天红包雨资格开始");

      //清理资格
      cleanRedEnvelopeQualification();

      //判断红包雨活动ID是否有配置
      ActivityRedPacketConfigVO config = activityRedPacketService.getConfig();

      if (config == null) {
        log.info("红包雨配置信息为空,需要在后台配置");
      } else {
        //判断红包雨活动配置规则
        Long redenvelopeId = config.getRedenvelopeId();

        if (redenvelopeId == null || redenvelopeId <= 0) {
          log.info("红包雨活动id异常,redenvelopeId={}", redenvelopeId);
        } else {
          //查询活动信息
          ActivityLobbyVO activityLobby = activityLobbyService.getActivityLobbyVOById(redenvelopeId);

          if (activityLobby == null) {
            log.info("不存在红包雨活动,ActivityLobby id={}", redenvelopeId);
          } else {
            //检查活动是否ok
            if (checkDoRedEnvelope(activityLobby)) {
              List<Map<String, Object>> rechargeOrderList = getRechargeOrder();

              if (CollectionUtils.isEmpty(rechargeOrderList)) {
                log.info("没有满足条件的充值记录");
              } else {
                Set blackSet = null,userLevelsSet = null;

                //获取活动黑名单
                String activityBalcklist = config.getActivityBalcklist();
                if (StringUtils.isNotEmpty(activityBalcklist)) {
                  blackSet = Arrays.stream(activityBalcklist.split(",")).collect(Collectors.toSet());
                }

                //获取允许参与活动的所有会员层级
                String userLevels = activityLobby.getGraded();
                if (StringUtils.isNotEmpty(userLevels)) {
                  userLevelsSet = Arrays.stream(userLevels.split(",")).collect(Collectors.toSet());
                }

                //查询所有已经产生的资格
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
      log.info("生成当天红包雨资格结束,消耗：{}ms", System.currentTimeMillis() - start);
    } catch (Exception e) {
      log.error("生成当天红包雨资格异常", e);
    }
  }

  /**
   * 检查红包雨活动信息
   * @param activityLobby
   * @return
   */
  private boolean checkDoRedEnvelope(ActivityLobbyVO activityLobby) {
    Date now = new Date();
    if (1!=activityLobby.getStatus()) {
      log.info("活动没有启用");
      return false;
    }
    if (!now.after(activityLobby.getStartTime())) {
      log.info("活动尚未开始,配置的开始时间为：{}", activityLobby.getStartTime());
      return false;
    }
    if (!now.before(activityLobby.getEndTime())) {
      log.info("活动已过期,配置的结束时间为：{}", activityLobby.getEndTime());
      return false;
    }
    return true;
  }

  /**
   * 处理红包雨活动产生资格
   * @param blackSet
   * @param userLevelsSet
   * @param activityLobby
   * @param list
   * @param existActivityQualificationList
   */
  private void doRedEnvelopeQualification(Set blackSet,Set userLevelsSet,ActivityLobbyVO activityLobby,
                                          List<Map<String, Object>> list,List<ActivityQualification> existActivityQualificationList){
    //存放需要生成资格的会员和充值信息
    Map<String, BigDecimal> newMap = new HashMap<>();

    //账号和会员id之间的映射
    Map<String, Long> nameAndId = new HashMap<>();

    log.info("一共有{}人充值", list==null?0:list.size());
    for (Map<String, Object> map : list) {
      String dayTotalAmount = map.get("dayTotalAmount").toString();
      String account = map.get("account").toString();
      String memberLevel = map.get("memberLevel").toString();
      nameAndId.put(account,new Long(map.get("memberId").toString()));

      //过滤活动黑名单
      if (blackSet != null && blackSet.contains(account)) {
        log.info("账号={}在黑名单内,充值={}不给资格", account, dayTotalAmount);
        continue;
      }

      //过滤不能层级内的充值
      if (userLevelsSet != null && !userLevelsSet.contains(memberLevel)) {
        log.info("账号={}在充值等级={}内充值={}不能参与资格统计", account, memberLevel,dayTotalAmount);
        continue;
      }

      //如果已经产生资格,则不再生成
      if(checkExistActivityQualification(existActivityQualificationList,account)){
        log.info("账号={}已经生成资格", account);
        continue;
      }

      //合并同一个账号的充值金额
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
        log.info("根据优惠规则没有找到对应配置信息account={},{}",entry.getKey(),temp);
        continue;
      }
      ActivityQualification qm=new ActivityQualification();
      qm.setAuditRemark("红包雨活动资格自动审批,充值总额="+entry.getValue());
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
      //YES=1 代表未删除
      qm.setDeleteFlag(BooleanEnum.YES.value());
//      qm.setQualificationActivityId(IdWorker.getIdStr());
//      qm.setAwardDetail("");
      qm.setSoleIdentifier(RandomUtil.generateOrderCode());
      //YES=1 代表启用
      qm.setQualificationStatus(BooleanEnum.YES.value());
      qm.setStatisItem(activityLobby.getStatisItem());
      qm.setGetWay(ActivityInfoEnum.GetWay.DIRECT_RELEASE.value());
      qm.setDrawNum(temp.getPresenterValue().intValue());
      //打码量是在抽奖时计算，这里不算
      qm.setWithdrawDml(BigDecimal.ZERO);
      //新增抽奖资格,使用次数为0
      qm.setEmployNum(0);
      qm.setMinMoney(temp.getPresenterDml());
      qm.setMaxMoney(temp.getWithdrawDml());
      needAdd.add(qm);
    }

    //批量插入资格
    this.saveBatch(needAdd);
  }

  /**
   * 根据充值金额获取优惠区间
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
    for (int i = 0; i < lobbyDiscountList.size(); i++) {
      ActivityLobbyDiscountVO temp = lobbyDiscountList.get(i);
      //targetValue: 充值金额
      BigDecimal targetValue = new BigDecimal(temp.getTargetValue());
      //money>=targetValue
      if (money.compareTo(targetValue) == 0 || money.compareTo(targetValue) == 1) {
        return temp;
      }
      //如果充值金额第一条都不满足则返回第一条的抽奖次数
      if (i == 0 && money.compareTo(targetValue) == 1) {
        return temp;
      }
    }

    return null;
  }

  /**
   * 获取充值订单，判断会员是否有满足红包雨资格
   * 1.查询时间段为昨天12点到当天时间12点
   * 2.已入款状态为成功
   * 3.只查正式会员的充值记录
   * 4.是计算积分标识
   * 5.时间以审核时间为准
   * @return
   */
  private List<Map<String, Object>> getRechargeOrder() {
    QueryWrapper<RechargeOrderHistory> rechargeOrderHistoryQueryWrapper = new QueryWrapper<>();
    rechargeOrderHistoryQueryWrapper.select("IFNULL(sum(amount),0) as dayTotalAmount,member_id as memberId,account,member_level as memberLevel")
            .eq("member_type", MemberEnums.Type.MEMBER.value())
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
   * 清理45天前的资格
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
   * 判断某个账号是否已经生成资格
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
