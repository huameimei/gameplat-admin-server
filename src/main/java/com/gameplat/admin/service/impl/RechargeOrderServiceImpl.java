package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.component.RechargeOrderQueryCondition;
import com.gameplat.admin.constant.BuildInDiscountType;
import com.gameplat.admin.constant.RechargeMode;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.RechargeOrderConvert;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.feign.MessageFeignClient;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderMapper;
import com.gameplat.admin.model.bean.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.constant.SocketEnum;
import com.gameplat.common.enums.*;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.common.util.CNYUtils;
import com.gameplat.message.model.MessagePayload;
import com.gameplat.model.entity.DiscountType;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberGrowthConfig;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.pay.PayAccount;
import com.gameplat.model.entity.pay.TpMerchant;
import com.gameplat.model.entity.pay.TpPayChannel;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import com.gameplat.model.entity.spread.SpreadUnion;
import com.gameplat.model.entity.sys.SysDictData;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.gameplat.common.enums.DictDataEnum.MAX_DISCOUNT_MONEY;
import static com.gameplat.common.enums.DictDataEnum.MAX_RECHARGE_MONEY;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RechargeOrderServiceImpl extends ServiceImpl<RechargeOrderMapper, RechargeOrder>
    implements RechargeOrderService {

  @Autowired private RechargeOrderConvert rechargeOrderConvert;

  @Autowired(required = false)
  private RechargeOrderMapper rechargeOrderMapper;

  @Autowired(required = false)
  private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private MemberService memberService;

  @Autowired private SysUserService sysUserService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private PayAccountService payAccountService;

  @Autowired private TpMerchantService tpMerchantService;

  @Autowired private TpPayChannelService tpPayChannelService;

  @Autowired private ConfigService configService;

  @Autowired private BizBlacklistFacade bizBlacklistFacade;

  @Autowired private DiscountTypeService discountTypeService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private ValidWithdrawService validWithdrawService;

  @Autowired private MemberRwReportService memberRwReportService;

  @Autowired private MemberGrowthConfigService memberGrowthConfigService;

  @Autowired @Lazy private MemberGrowthStatisService memberGrowthStatisService;

  @Autowired(required = false)
  private MessageMapper messageMapper;

  @Autowired private MessageDistributeService messageDistributeService;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private RechargeOrderQueryCondition rechargeCondition;

  @Autowired(required = false)
  private MessageFeignClient client;

  @Autowired
  private DistributedLocker distributedLocker;

  @Override
  public PageExt<RechargeOrderVO, SummaryVO> findPage(
      Page<RechargeOrder> page, RechargeOrderQueryDTO dto) {

    IPage<RechargeOrderVO> data = rechargeOrderMapper.findPage(page, rechargeCondition.builderQueryWrapper(dto));
    // ??????????????????????????????????????????????????????????????????
    SummaryVO summaryVO = amountSum(dto);
    return new PageExt<>(data, summaryVO);
  }

  @Override
  public void updateDiscount(
      Long id, Integer discountType, BigDecimal discountAmount, BigDecimal discountDml) {
    Integer discountRechargeFlag = 1;
    if (null == discountType) {
      discountAmount = BigDecimal.ZERO;
      discountDml = BigDecimal.ZERO;
      discountRechargeFlag = null;
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    RechargeOrder rechargeOrder = this.getById(id);
    rechargeOrder.setAuditorAccount(userCredential.getUsername());
    rechargeOrder.setAuditTime(new Date());
    rechargeOrder.setDiscountType(discountType);
    rechargeOrder.setDiscountAmount(discountAmount);
    rechargeOrder.setDiscountDml(discountDml);
    rechargeOrder.setTotalAmount(rechargeOrder.getPayAmount().add(discountAmount));
    rechargeOrder.setDiscountRechargeFlag(discountRechargeFlag);
    this.updateById(rechargeOrder);
  }

  @Override
  public void updateRemarks(Long id, String auditRemarks) {
    if (null == auditRemarks) {
      throw new ServiceException("????????????????????????!");
    }
    UserCredential userCredential = SecurityUserHolder.getCredential();
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getAuditRemarks, auditRemarks)
        .set(RechargeOrder::getAuditorAccount, userCredential.getUsername())
        .set(RechargeOrder::getAuditTime, new Date())
        .eq(RechargeOrder::getId, id);
    this.update(new RechargeOrder(), update);
  }

  @Override
  public void handle(Long id) {
    this.updateStatus(id, RechargeStatus.UNHANDLED.getValue(), RechargeStatus.HANDLED.getValue());
  }

  @Override
  public void unHandle(Long id) {
    RechargeOrder rechargeOrder = this.getById(id);
    // ???????????????????????????????????????????????????
    crossAccountCheck(rechargeOrder);
    // ????????????????????????
    this.updateStatus(id, RechargeStatus.HANDLED.getValue(), RechargeStatus.UNHANDLED.getValue());
  }

  @Override
  public void accept(Long id, String auditRemarks, boolean flag) {
    RechargeOrder rechargeOrder = this.getById(id);
    String lock_key = String.format(CachedKeys.MEMBER_FINANCE, rechargeOrder.getAccount());
    RLock lock = distributedLocker.lock(lock_key);
    try {

      // ???????????????????????????????????????  ???????????????
      if (!flag) {
        String maxAmount = configService.getValue(MAX_RECHARGE_MONEY);
        if (rechargeOrder.getAmount().compareTo(new BigDecimal(maxAmount)) > 0) {
          throw new ServiceException("??????????????????????????????????????????????????????????????????????????????????????????????????????" + maxAmount);
        }

        String maxDiscount = configService.getValue(MAX_DISCOUNT_MONEY);
        if (rechargeOrder.getDiscountAmount() != null) {
          if (rechargeOrder.getDiscountAmount().compareTo(new BigDecimal(maxDiscount)) > 0) {
            throw new ServiceException("????????????????????????????????????????????????????????????" + maxDiscount);
          }
        }
      }
      // ??????????????????
      verifyRechargeOrderForAuditing(rechargeOrder);
      // ???????????????????????????????????????????????????
      crossAccountCheck(rechargeOrder);
      // ??????????????????
      if (flag) {
        rechargeProcess(rechargeOrder);
      }
      // ????????????????????????
      Member member = memberService.getById(rechargeOrder.getMemberId());
      MemberInfo memberInfo = memberInfoService.getById(rechargeOrder.getMemberId());
      verifyUser(member, memberInfo, rechargeOrder.getMode() == RechargeMode.TRANSFER.getValue());

      // ???????????????????????????????????????
      SysUser sysUser = sysUserService.getByUsername(SecurityUserHolder.getUsername());
      AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
      if (null != sysUser && StringUtils.equals(UserTypes.SUBUSER.value(), sysUser.getUserType())) {
        checkZzhRechargeAmountAudit(
                sysUser.getUserName(),
                adminLimitInfo,
                rechargeOrder.getMode(),
                rechargeOrder.getTotalAmount());
      }

      // ????????????????????????
      recalculateDiscountAmount(rechargeOrder, memberInfo.getTotalRechTimes() > 0);

      // ??????????????????
      rechargeOrder.setAuditorAccount(sysUser.getUserName());
      rechargeOrder.setAuditTime(new Date());
      if (null != auditRemarks) {
        rechargeOrder.setAuditRemarks(auditRemarks);
      }
      rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
      updateRechargeOrder(rechargeOrder);
      // ??????????????????
      // ?????? ???????????? ??????
      if (!UserTypes.PROMOTION.value().equals(member.getUserType())) {
        memberRwReportService.addRecharge(member, memberInfo.getTotalRechTimes(), rechargeOrder);
      }

      if (rechargeOrder.getDmlFlag() == TrueFalse.TRUE.getValue()) {
        // ???????????????
        validWithdrawService.addRechargeOrder(rechargeOrder);
      }
      // ??????????????????
      addUserBill(rechargeOrder, member, memberInfo.getBalance(), sysUser.getUserName());
      // ????????????????????????
      updateRechargeMoney(rechargeOrder, member.getUserType());

      // ????????????????????????
      memberInfoService.updateBalanceWithRecharge(
              memberInfo.getMemberId(),
              rechargeOrder.getPayAmount(),
              rechargeOrder.getTotalAmount(),
              rechargeOrder.getPointFlag());
      // ??????????????????????????????
      if (TrueFalse.TRUE.getValue() != rechargeOrder.getPointFlag()) {
        log.info(
                "???????????????????????? account={}???orderNo={},pointFlag={}",
                rechargeOrder.getAccount(),
                rechargeOrder.getOrderNo(),
                rechargeOrder.getPointFlag());
        return;
      }

      // ???????????????
      MemberGrowthConfig memberGrowthConfig = memberGrowthConfigService.getOneConfig();
      if (memberGrowthConfig.getIsEnableVip() == TrueFalse.FALSE.getValue()) {
        log.info(
                "?????????????????????????????????-?????????VIP????????????: account={}???orderNo={},pointFlag={}",
                rechargeOrder.getAccount(),
                rechargeOrder.getOrderNo(),
                rechargeOrder.getPointFlag());
        return;
      }
      if (memberGrowthConfig.getIsEnableRecharge() == TrueFalse.FALSE.getValue()) {
        log.info(
                "?????????????????????????????????-???????????????????????????????????? account={}???orderNo={},pointFlag={}",
                rechargeOrder.getAccount(),
                rechargeOrder.getOrderNo(),
                rechargeOrder.getPointFlag());
        return;
      }
      MemberGrowthChangeDto memberGrowthChangeDto = new MemberGrowthChangeDto();
      memberGrowthChangeDto.setUserId(rechargeOrder.getMemberId());
      memberGrowthChangeDto.setUserName(rechargeOrder.getAccount());
      memberGrowthChangeDto.setType(GrowthChangeEnum.recharge.getCode());
      memberGrowthChangeDto.setChangeGrowth(
              memberGrowthConfig.getRechageRate().multiply(rechargeOrder.getAmount()).longValue());
      memberGrowthChangeDto.setRemark("???????????????????????????");

      memberGrowthStatisService.changeGrowth(memberGrowthChangeDto);
    } finally {
      distributedLocker.unlock(lock);
    }

    // ???????????? ?????? ??????  mode   ?????? ????????????
    if (ObjectUtil.equals(RechargeMode.TRANSFER.getValue(), rechargeOrder.getMode())
        || ObjectUtil.equals(RechargeMode.ONLINE_PAY.getValue(), rechargeOrder.getMode())) {
      this.addMessageInfo(rechargeOrder, RechargeStatus.SUCCESS.getValue());
      MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
      this.sendMessage(
          rechargeOrder.getAccount(), SocketEnum.SOCKET_RECHARGE_SUCCESS, limit.getRechargeTip());
    }
  }

  @Override
  public void cancel(Long id) throws ServiceException {
    RechargeOrder rechargeOrder = this.getById(id);
    // ??????????????????
    verifyRechargeOrderForAuditing(rechargeOrder);
    // ???????????????????????????????????????????????????
    crossAccountCheck(rechargeOrder);
    // ??????????????????
    rechargeOrder.setAuditorAccount(SecurityUserHolder.getUsername());
    rechargeOrder.setAuditTime(new Date());
    rechargeOrder.setStatus(RechargeStatus.CANCELLED.getValue());
    updateRechargeOrder(rechargeOrder);
    // ????????????
    this.addMessageInfo(rechargeOrder, RechargeStatus.CANCELLED.getValue());
    // ??????????????? socket
    this.sendMessage(
        rechargeOrder.getAccount(),
        SocketEnum.SOCKET_RECHARGE_FAIL,
        SocketEnum.SEND_RECHARGE_FAIL_MESSAGE);
  }

  @Override
  public void updateStatus(Long id, Integer curStatus, Integer newStatus) {
    String auditorAccount = SecurityUserHolder.getUsername();
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getStatus, newStatus)
        .set(RechargeOrder::getAcceptAccount, auditorAccount)
        .set(RechargeOrder::getAcceptTime, new Date())
        .set(RechargeOrder::getAuditorAccount, auditorAccount)
        .set(RechargeOrder::getAuditTime, new Date())
        .eq(RechargeOrder::getId, id)
        .eq(RechargeOrder::getStatus, curStatus);

    if (!this.update(new RechargeOrder(), update)) {
      String msg = String.format("?????????????????????????????????????????????[%s]???????????????.", id);
      log.error(msg);
      throw new ServiceException(msg);
    }
  }

  @Override
  public void manual(ManualRechargeOrderBo manualRechargeOrderBo, UserEquipment userEquipment) {
    RechargeOrder rechargeOrder = buildManualRechargeOrder(manualRechargeOrderBo);
    // ?????????????????????
    fillClientInfo(rechargeOrder, userEquipment);
    this.save(rechargeOrder);

    if (manualRechargeOrderBo.isSkipAuditing()) {
      String auditRemarks = StringUtils.isBlank(manualRechargeOrderBo.getAuditRemarks()) ? "????????????" : manualRechargeOrderBo.getAuditRemarks();
      accept(rechargeOrder.getId(), auditRemarks, false);
    }
  }

  @Override
  public List<ActivityStatisticItem> findRechargeDateList(Map<String, Object> map) {
    List<ActivityStatisticItem> rechargeDateList = rechargeOrderMapper.findRechargeDateList(map);
    if (CollectionUtils.isNotEmpty(rechargeDateList)) {
      // ????????????????????????String??????List<Date>R
      for (ActivityStatisticItem rechargeDate : rechargeDateList) {
        if (StringUtils.isNotEmpty(rechargeDate.getRechargeTimes())) {
          List<String> dateList = Arrays.asList(rechargeDate.getRechargeTimes().split(","));
          List<String> list = dateList.stream().distinct().sorted().collect(Collectors.toList());
          List<Date> dates = new ArrayList<>();
          for (String date : list) {
            dates.add(DateUtil.strToDate(date, DateUtil.YYYY_MM_DD));
          }
          rechargeDate.setRechargeTimeList(dates);
        }
      }
    }
    return rechargeDateList;
  }

  @Override
  public List<ActivityStatisticItem> findAllFirstRechargeAmount(Map<String, Object> map) {
    String startTime = (String) map.get("startTime");
    String endTime = (String) map.get("endTime");
    // ??????????????????????????????????????????????????????????????????
    map.remove("startTime");
    map.remove("endTime");
    List<ActivityStatisticItem> firstRechargeOrderList =
        rechargeOrderMapper.findFirstRechargeOrderList(map);
    if (CollectionUtils.isNotEmpty(firstRechargeOrderList)) {
      for (ActivityStatisticItem firstRechargeOrder : firstRechargeOrderList) {
        // ????????????????????????????????????????????????????????????
        if (cn.hutool.core.date.DateUtil.isIn(
            firstRechargeOrder.getRechargeTime(),
            cn.hutool.core.date.DateUtil.parseDate(startTime),
            cn.hutool.core.date.DateUtil.parseDate(endTime))) {
          firstRechargeOrder.setIsNewUser(1);
        } else {
          firstRechargeOrder.setIsNewUser(2);
        }
      }
      return firstRechargeOrderList;
    }
    return null;
  }

  @Override
  public List<ActivityStatisticItem> findAllTwoRechargeAmount(Map<String, Object> map) {
    String startTime = (String) map.get("startTime");
    String endTime = (String) map.get("endTime");
    // ??????????????????????????????????????????????????????????????????
    map.remove("startTime");
    map.remove("endTime");
    List<ActivityStatisticItem> twoRechargeOrderList =
            rechargeOrderMapper.findTwoRechargeAmount(map);
    if (CollectionUtils.isNotEmpty(twoRechargeOrderList)) {
      for (ActivityStatisticItem twoRechargeOrder : twoRechargeOrderList) {
        // ????????????????????????????????????????????????????????????
        if (cn.hutool.core.date.DateUtil.isIn(
                twoRechargeOrder.getRechargeTime(),
                cn.hutool.core.date.DateUtil.parseDate(startTime),
                cn.hutool.core.date.DateUtil.parseDate(endTime))) {
          twoRechargeOrder.setIsNewUser(1);
        } else {
          twoRechargeOrder.setIsNewUser(2);
        }
      }
      return twoRechargeOrderList;
    }
    return null;
  }

  /** ???????????????????????????????????????????????????????????????????????????????????????????????? */
  @Override
  public MemberActivationVO getRechargeInfoByNameAndUpdateTime(
      MemberActivationDTO memberActivationDTO) {
    return rechargeOrderMapper.getRechargeInfoByNameAndUpdateTime(memberActivationDTO);
  }

  private void addMessageInfo(RechargeOrder rechargeOrder, Integer state) {
    if (this.verifyMessage() == com.gameplat.common.enums.TrueFalse.FALSE.getValue()) {
      return;
    }
    Message messageInfo = new Message();
    messageInfo.setTitle(title(1, state));
    messageInfo.setContent(
        context(state, rechargeOrder.getCreateTime(), rechargeOrder.getAmount(), 1));
    messageInfo.setCategory(4);
    messageInfo.setPosition(1);
    messageInfo.setShowType(0);
    messageInfo.setPopsCount(0);
    messageInfo.setPushRange(2);
    messageInfo.setLinkAccount(rechargeOrder.getAccount());
    messageInfo.setSort(0);
    messageInfo.setType(1);
    messageInfo.setLanguage("zh-CN");
    messageInfo.setStatus(1);
    messageInfo.setImmediateFlag(0);
    messageInfo.setRemarks("????????????");
    messageInfo.setCreateBy("system");
    messageInfo.setCreateTime(new Date());
    messageMapper.insert(messageInfo);

//    MessageDistribute messageDistribute = new MessageDistribute();
//    messageDistribute.setMessageId(messageInfo.getId());
//    messageDistribute.setUserId(rechargeOrder.getMemberId());
//    messageDistribute.setUserAccount(messageInfo.getLinkAccount());
//    messageDistribute.setRechargeLevel(Convert.toInt(rechargeOrder.getMemberLevel()));
//    messageDistribute.setVipLevel(
//        memberInfoService
//            .lambdaQuery()
//            .eq(MemberInfo::getMemberId, rechargeOrder.getMemberId())
//            .one()
//            .getVipLevel());
//    messageDistribute.setReadStatus(NumberConstant.ZERO);
//    messageDistribute.setCreateBy("system");
//    messageDistributeService.save(messageDistribute);
  }

  private void sendMessage(String account, String channel, String message) {
    MessagePayload messagePayload = MessagePayload.builder().channel(channel).title(message).build();
    log.info("????????????=============>????????????Socket??????,????????????{}", messagePayload);
    client.sendToUser(account, messagePayload);
  }

  private int verifyMessage() {
    SysDictData sysDictData =
        sysDictDataService.getDictData(
            DictTypeEnum.SYSTEM_PARAMETER_CONFIG.getValue(),
            DictDataEnum.RECHARGE_PUSH_MSG.getLabel());
    return ObjectUtil.isNull(sysDictData) ? 0 : Convert.toInt(sysDictData.getDictValue());
  }

  /**
   * @param state ??????
   * @param date ??????
   * @param money ??????
   * @param mode 1 ?????? 2 ??????
   * @return String
   */
  private String context(Integer state, Date date, BigDecimal money, int mode) {
    String context = "";
    String dateStr = DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD_HH_MM_SS);

    if (ObjectUtil.equals(1, mode)) {
      if (ObjectUtil.equals(3, state)) {
        context = "??????" + dateStr + "?????????" + money.setScale(2, RoundingMode.HALF_UP) + "??????????????????";
      } else {
        context = "??????" + dateStr + "?????????" + money.setScale(2, RoundingMode.HALF_UP) + "????????????";
      }
    }

    return context;
  }

  /**
   * @param mode 1 ?????? 2 ??????
   * @return String
   */
  private String title(int mode, int state) {
    String title = "";
    if (ObjectUtil.equals(1, mode)) {
      if (ObjectUtil.equals(state, 3)) {
        title = "????????????";
      } else {
        title = "????????????";
      }
    }
    return title;
  }

  private void verifyRechargeOrderForAuditing(RechargeOrder rechargeOrder) {
    if (rechargeOrder == null) {
      throw new ServiceException("????????????????????????");
    }
    if (rechargeOrder.getAmount().compareTo(BigDecimal.ZERO) < 0
        || rechargeOrder.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new ServiceException("????????????????????????");
    }
    if (rechargeOrder.getStatus() != RechargeStatus.UNHANDLED.getValue()
        && rechargeOrder.getStatus() != RechargeStatus.HANDLED.getValue()) {
      throw new ServiceException("??????????????????");
    }
  }

  /** ????????????????????????????????????????????????????????? ??????????????????????????????????????? ?????????????????? */
  public void crossAccountCheck(RechargeOrder rechargeOrder) {
    UserCredential userCredential = SecurityUserHolder.getCredential();
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    boolean toCheck =
        BooleanEnum.NO.match(limit.getIsHandledAllowOthersOperate())
            && !userCredential.isSuperAdmin();
    if (toCheck) {
      if (!Objects.equals(RechargeStatus.UNHANDLED.getValue(), rechargeOrder.getStatus())
              && ObjectUtil.isNotEmpty(rechargeOrder.getAcceptAccount())
              && !userCredential.getUsername().equals(rechargeOrder.getAcceptAccount())) {
        throw new ServiceException("????????????????????????:" + rechargeOrder.getOrderNo());
      }
    }
  }

  public void rechargeProcess(RechargeOrder rechargeOrder) {
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    boolean isRechargeProcess = BooleanEnum.YES.match(limit.getIsRechargeProcess());
    if (isRechargeProcess
        && !ObjectUtil.equal(RechargeStatus.HANDLED.getValue(), rechargeOrder.getStatus())) {
      throw new ServiceException("??????????????????:" + rechargeOrder.getOrderNo());
    }
  }

  private void verifyUser(Member member, MemberInfo memberInfo, boolean checkLimitRech) {
    if (member == null) {
      throw new ServiceException("memberInfo_exist");
    }
    if (memberInfo == null) {
      throw new ServiceException("user_not_exist");
    }
    if (!MemberEnums.Status.ENABlED.match(member.getStatus())) {
      log.error(
          "--account:{},userId:{},state:{}--",
          member.getAccount(),
          member.getId(),
          member.getStatus());
      throw new ServiceException(String.format("?????????????????????:%s", member.getAccount()));
    }
    if (MemberEnums.Type.TEST.match(member.getUserType())) {
      throw new ServiceException("???????????????????????????");
    }
  }

  private void checkZzhRechargeAmountAudit(
      String userName, AdminLimitInfo adminLimitInfo, Integer mode, BigDecimal rechargeAmount) {

    if (mode == RechargeMode.TRANSFER.getValue() || mode == RechargeMode.ONLINE_PAY.getValue()) {

      if (!adminLimitInfo.getMaxRechargeAmount().equals(BigDecimal.ZERO)
          && rechargeAmount.compareTo(adminLimitInfo.getMaxRechargeAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("???????????????????????????????????????")
                .append(adminLimitInfo.getMaxRechargeAmount())
                .append("?????? ????????????")
                .append(
                    MoneyUtils.toYuanStr(
                        rechargeAmount.subtract(adminLimitInfo.getMaxRechargeAmount())))
                .append("???");
        throw new ServiceException(buffer.toString());
      }
    } else if (mode == RechargeMode.MANUAL.getValue()) {
      if (!adminLimitInfo.getMaxManualRechargeAmount().equals(BigDecimal.ZERO)
          && rechargeAmount.compareTo(adminLimitInfo.getMaxManualRechargeAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("???????????????????????????????????????????????????")
                .append(adminLimitInfo.getMaxManualRechargeAmount())
                .append("??????  ????????????")
                .append(
                    MoneyUtils.toYuanStr(
                        rechargeAmount.subtract(adminLimitInfo.getMaxManualRechargeAmount())))
                .append("???");
        throw new ServiceException(buffer.toString());
      }
    }
  }

  private void recalculateDiscountAmount(RechargeOrder rechargeOrder, boolean isFirstRecharge) {
    if (Objects.equals(rechargeOrder.getMode(), RechargeMode.MANUAL.getValue())) {
      return;
    }
    if (BigDecimal.ZERO.compareTo(rechargeOrder.getDiscountAmount()) > 0) {
      return;
    }
    if (!Objects.equals(
        rechargeOrder.getDiscountRechargeFlag(),
        BuildInDiscountType.FIRST_RECHARGE.getRechargeFlag())) {
      return;
    }
    if (!Objects.equals(
        rechargeOrder.getDiscountType(), BuildInDiscountType.FIRST_RECHARGE.getValue())) {
      return;
    }
    if (!isFirstRecharge) {
      return;
    }
    rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
    rechargeOrder.setDiscountDml(BigDecimal.ZERO);
    rechargeOrder.setDiscountRechargeFlag(0);
    rechargeOrder.setDiscountType(null);
    rechargeOrder.setTotalAmount(rechargeOrder.getAmount());
  }

  private void updateRechargeOrder(RechargeOrder rechargeOrder) {
    LambdaUpdateWrapper<RechargeOrder> update = Wrappers.lambdaUpdate();
    update
        .set(RechargeOrder::getStatus, rechargeOrder.getStatus())
        .set(RechargeOrder::getAuditTime, rechargeOrder.getAuditTime())
        .set(RechargeOrder::getAuditorAccount, rechargeOrder.getAuditorAccount())
        .eq(RechargeOrder::getId, rechargeOrder.getId());
    if (!this.update(new RechargeOrder(), update)) {
      throw new ServiceException("??????????????????");
    }
    RechargeOrderHistory rechargeOrderHistory = new RechargeOrderHistory();
    BeanUtils.copyProperties(rechargeOrder, rechargeOrderHistory);
    rechargeOrderHistoryMapper.insert(rechargeOrderHistory);
  }

  private void updateRechargeMoney(RechargeOrder rechargeOrder, String userType) {
    // ??????????????????????????????
    if (rechargeOrder.getPayAccountId() != null) {
      PayAccount payAccount = payAccountService.getById(rechargeOrder.getPayAccountId());
      if (payAccount == null) {
        throw new ServiceException("?????????????????????????????????????????????????????????????");
      }
      if (!StringUtils.equals(UserTypes.PROMOTION.value(), userType)) {
        payAccountService
            .lambdaUpdate()
            .set(PayAccount::getRechargeTimes, payAccount.getRechargeTimes() + 1)
            .set(
                PayAccount::getRechargeAmount,
                payAccount.getRechargeAmount().add(rechargeOrder.getPayAmount()))
            .eq(PayAccount::getId, payAccount.getId())
            .update(new PayAccount());
      }

      // ?????????????????????????????????
      if (validateLimitAmount(payAccount.getLimitInfo(), payAccount.getRechargeAmount())) {
        LambdaUpdateWrapper<PayAccount> update = Wrappers.lambdaUpdate();
        update
            .set(PayAccount::getStatus, SwitchStatusEnum.DISABLED.getValue())
            .eq(PayAccount::getId, payAccount.getId());
        payAccountService.update(new PayAccount(), update);
      }
    }

    // ??????????????????????????????
    if (rechargeOrder.getTpMerchantId() != null) {
      TpMerchant tpMerchant = tpMerchantService.getById(rechargeOrder.getTpMerchantId());
      if (tpMerchant == null) {
        throw new ServiceException("???????????????????????????????????????????????????????");
      }
      LambdaUpdateWrapper<TpMerchant> update = Wrappers.lambdaUpdate();
      update
          .set(TpMerchant::getRechargeTimes, tpMerchant.getRechargeTimes() + 1)
          .set(
              TpMerchant::getRechargeAmount,
              tpMerchant.getRechargeAmount().add(rechargeOrder.getPayAmount()))
          .eq(TpMerchant::getId, tpMerchant.getId());
      tpMerchantService.update(new TpMerchant(), update);
    }

    // ?????????????????????????????????
    if (rechargeOrder.getTpPayChannelId() != null) {
      TpPayChannel tpPayChannel = tpPayChannelService.getById(rechargeOrder.getTpPayChannelId());
      if (tpPayChannel == null) {
        throw new ServiceException("?????????????????????????????????????????????????????????????");
      }
      if (!StringUtils.equals(UserTypes.PROMOTION.value(), userType)) {
        LambdaUpdateWrapper<TpPayChannel> update = Wrappers.lambdaUpdate();
        update
            .set(TpPayChannel::getRechargeTimes, tpPayChannel.getRechargeTimes() + 1)
            .set(
                TpPayChannel::getRechargeAmount,
                tpPayChannel.getRechargeAmount().add(rechargeOrder.getPayAmount()))
            .eq(TpPayChannel::getId, tpPayChannel.getId());
        tpPayChannelService.update(update);
      }

      // ?????????????????????????????????
      if (validateLimitAmount(tpPayChannel.getLimitInfo(), tpPayChannel.getRechargeAmount())) {
        LambdaUpdateWrapper<TpPayChannel> update = Wrappers.lambdaUpdate();
        update.set(TpPayChannel::getStatus, 1).eq(TpPayChannel::getId, tpPayChannel.getId());
        tpPayChannelService.update(new TpPayChannel(), update);
      }
    }
  }

  private boolean validateLimitAmount(String limitInfo, BigDecimal amount) {
    if (StringUtils.isNotBlank(limitInfo)) {
      ChannelLimitsBean limitsBean = ChannelLimitsBean.conver2Bean(limitInfo);
      if (TrueFalse.TRUE.getValue() == limitsBean.getLimitStatus()) {
        return limitsBean.getLimitAmount().compareTo(amount) < 0;
      }
    }
    return false;
  }

  private RechargeOrder buildManualRechargeOrder(ManualRechargeOrderBo manualRechargeOrderBo) {
    RechargeOrder rechargeOrder = new RechargeOrder();
    // ??????????????????memberId?????????ID
    // ????????????????????????
    Member member = memberService.getById(manualRechargeOrderBo.getMemberId());
    MemberInfo memberInfo = memberInfoService.getById(manualRechargeOrderBo.getMemberId());
    verifyUser(member, memberInfo, false);
    fillUserInfo(rechargeOrder, member, memberInfo);
    /* ?????????????????? */
    verifyAmount(manualRechargeOrderBo.getAmount(), manualRechargeOrderBo.getDiscountAmount());
    rechargeOrder.setAmount(manualRechargeOrderBo.getAmount());
    rechargeOrder.setPayAmount(manualRechargeOrderBo.getAmount());
    boolean isDiscountIgnored = false;
    if (manualRechargeOrderBo.getPointFlag() == TrueFalse.TRUE.getValue()
        && manualRechargeOrderBo.getDiscountType() != null) {
      if (bizBlacklistFacade.isUserInBlacklist(
          rechargeOrder.getMemberId(), BizBlacklistType.RECHARGE_DISCOUNT)) {
        rechargeOrder.setDiscountType(null);
        rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
        rechargeOrder.setDiscountDml(BigDecimal.ZERO);
        isDiscountIgnored = true;
      } else {
        // ??????????????????
        DiscountType discountType =
            discountTypeService.getByValue(manualRechargeOrderBo.getDiscountType());
        verifyDiscountType(discountType);
        rechargeOrder.setDiscountRechargeFlag(discountType.getRechargeFlag());
        rechargeOrder.setDiscountType(discountType.getValue());
        rechargeOrder.setDiscountAmount(manualRechargeOrderBo.getDiscountAmount());
        rechargeOrder.setDiscountDml(manualRechargeOrderBo.getDiscountDml());
      }
    } else {
      rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
      rechargeOrder.setDiscountDml(BigDecimal.ZERO);
    }

    // ??????????????? = ???????????? + ????????????
    rechargeOrder.setTotalAmount(
        rechargeOrder.getPayAmount().add(rechargeOrder.getDiscountAmount()));

    // ???????????????
    rechargeOrder.setDmlFlag(manualRechargeOrderBo.getDmlFlag());
    rechargeOrder.setNormalDml(manualRechargeOrderBo.getNormalDml());

    // ????????????????????????
    rechargeOrder.setPointFlag(manualRechargeOrderBo.getPointFlag());

    // ??????????????????
    rechargeOrder.setRemarks(manualRechargeOrderBo.getRemarks());

    // ??????????????????
    if (isDiscountIgnored) {
      StringBuilder sb = new StringBuilder("???????????????????????????????????????????????????????????????");
      Optional.ofNullable(manualRechargeOrderBo.getAuditRemarks())
          .filter(StringUtils::isNotBlank)
          .ifPresent(e -> sb.append(String.format("[%s]", e)));
      rechargeOrder.setAuditRemarks(sb.toString());
    } else {
      rechargeOrder.setAuditRemarks(manualRechargeOrderBo.getAuditRemarks());
    }
    rechargeOrder.setMode(RechargeMode.MANUAL.getValue());

    // ??????????????????????????????
    rechargeOrder.setStatus(RechargeStatus.UNHANDLED.getValue());

    // ????????????????????????????????????
    rechargeOrder.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));

    // ???????????? ????????????????????????
    rechargeOrder.setCreateTime(new Date());
    // ??????????????????????????????
    rechargeOrder.setMemberType(member.getUserType());
    // ??????????????????
    MemberRechargeLimit limit = limitInfoService.getRechargeLimit();
    rechargeOrder.setOrderExpireTime(limit.getOrderExpireTime());
    return rechargeOrder;
  }

  private void fillUserInfo(RechargeOrder rechargeOrder, Member member, MemberInfo memberInfo) {
    rechargeOrder.setMemberId(member.getId());
    rechargeOrder.setAccount(member.getAccount());
    rechargeOrder.setNickname(member.getRealName());
    rechargeOrder.setMemberLevel(member.getUserLevel().toString());
    rechargeOrder.setSuperId(member.getParentId());
    rechargeOrder.setSuperAccount(member.getParentName());
    rechargeOrder.setSuperPath(member.getSuperPath());
    rechargeOrder.setBalance(memberInfo.getBalance());
    rechargeOrder.setMemberType(member.getUserType());
  }

  private void verifyAmount(BigDecimal amount, BigDecimal discountAmount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new ServiceException("????????????????????????");
    }
    if (amount.compareTo(BigDecimal.ZERO) == 0
        && (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) <= 0)) {
      throw new ServiceException("????????????????????????");
    }
    // ????????????????????????
    String maxAmount = configService.getValue(MAX_RECHARGE_MONEY);
    if (amount.compareTo(new BigDecimal(maxAmount)) > 0) {
      throw new ServiceException("????????????????????????????????????????????????????????????" + maxAmount);
    }

    String maxDiscount = configService.getValue(MAX_DISCOUNT_MONEY);
    if (discountAmount != null) {
      if (discountAmount.compareTo(new BigDecimal(maxDiscount)) > 0) {
        throw new ServiceException("??????????????????????????????????????????????????????????????????" + maxDiscount);
      }
    }
  }

  private void verifyDiscountType(DiscountType discountType) {
    Assert.notNull(discountType, "?????????????????????");
    Assert.isTrue(SwitchStatusEnum.ENABLED.match(discountType.getStatus()), "?????????????????????");
  }

  private SummaryVO amountSum(RechargeOrderQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrder> queryHandle = Wrappers.lambdaQuery();
    SummaryVO summaryVO = new SummaryVO();
    queryHandle
        .eq(RechargeOrder::getStatus, RechargeStatus.HANDLED.getValue())
        .in(ObjectUtils.isNotNull(dto.getModeList()), RechargeOrder::getMode, dto.getModeList())
        .in(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && MemberEnums.Type.MEMBER.match(dto.getMemberType()),
            RechargeOrder::getMemberType,
            MemberEnums.Type.MEMBER.value(),
            MemberEnums.Type.PROMOTION.value())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && MemberEnums.Type.PROMOTION.match(dto.getMemberType()),
            RechargeOrder::getMemberType,
            dto.getMemberType())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrder::getMemberLevel,
            dto.getMemberLevelList());
    summaryVO.setAllHandledSum(rechargeOrderMapper.summaryRechargeOrder(queryHandle));

    LambdaQueryWrapper<RechargeOrder> queryUnHandle = Wrappers.lambdaQuery();
    queryUnHandle
        .eq(RechargeOrder::getStatus, RechargeStatus.UNHANDLED.getValue())
        .in(ObjectUtils.isNotNull(dto.getModeList()), RechargeOrder::getMode, dto.getModeList())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrder::getMemberLevel,
            dto.getMemberLevelList());
    summaryVO.setAllUnhandledSum(rechargeOrderMapper.summaryRechargeOrder(queryUnHandle));
    return summaryVO;
  }

  /** ???????????????????????????????????????????????????????????? */
  @Override
  public List<JSONObject> getSpreadReport(
      List<SpreadUnion> list, String startTime, String endTime) {
    return rechargeOrderMapper.getSpreadReport(list, startTime, endTime);
  }

  @Override
  public List<ThreeRechReportVo> findThreeRechReport(GameRWDataReportDto dto) {
    List<RechargeOrder> list = rechargeOrderMapper.selectList(this.builderMemberTodayQuery(dto));
    return BeanUtil.copyToList(list, ThreeRechReportVo.class);
  }

  @Override
  public long getUntreatedRechargeCount() {
    return this.lambdaQuery().eq(RechargeOrder::getMode, 1).eq(RechargeOrder::getStatus, 1).count();
  }

  @Override
  public void expired(Long id) {
    RechargeOrder rechargeOrder = this.getById(id);
    if (RechargeStatus.UNHANDLED.match(rechargeOrder.getStatus())) {
      rechargeOrder.setStatus(RechargeStatus.CANCELLED.getValue());
      rechargeOrder.setAuditRemarks("????????????");
      rechargeOrder.setAuditTime(new Date());

      rechargeOrder.setAuditorAccount("????????????");
      rechargeOrder.setRemarks("????????????");
      if (this.updateById(rechargeOrder)) {
        log.info("????????????????????????????????????{}", rechargeOrder.getOrderNo());
        RechargeOrderHistory rechargeOrderHistory = new RechargeOrderHistory();
        BeanUtils.copyProperties(rechargeOrder, rechargeOrderHistory);
        rechargeOrderHistoryMapper.insert(rechargeOrderHistory);
      }
    }
  }

  @Async
  @Override
  public void batchMemberRecharge(
      UserEquipment clientInfo,
      List<RechargeMemberFileBean> strAccount,
      ManualRechargeOrderDto dto) {
    List<RechargeMemberFileBean> strAccountList = new ArrayList<>();
    // ??????vip ??????????????????
    if (ObjectUtil.isNotEmpty(dto.getLevel()) || ObjectUtil.isNotEmpty(dto.getVip())) {
      List<RechargeMemberFileBean> accountStr =
              memberService.findMemberRechVip(dto.getLevel(), dto.getVip());
      if (ObjectUtil.isNotEmpty(accountStr)) {
        strAccountList.addAll(accountStr);
      }
    }
    if (ObjectUtil.isNotEmpty(strAccount)) {
      strAccountList.addAll(strAccount);
    }
    if (ObjectUtil.isEmpty(strAccountList)) {
      return;
    }

    strAccountList = strAccountList.stream().distinct().collect(Collectors.toList());

    strAccountList.forEach(
        a -> {
          MemberBalanceVO memberVip = memberService.findMemberVip(a.getUsername());
          if (memberVip == null || StringUtils.isEmpty(memberVip.getAccount())) {
            return;
          }
          log.info("?????????{},????????????:{}", a, dto.getAmount());
          dto.setAccount(memberVip.getAccount());
          ManualRechargeOrderBo manualRechargeOrderBo = new ManualRechargeOrderBo();
          BeanUtil.copyProperties(dto, manualRechargeOrderBo);
          manualRechargeOrderBo.setMemberId(memberVip.getId());
          try {
            manual(manualRechargeOrderBo, clientInfo);
          } catch (Exception e) {
            log.info("????????????", e);
          }
        });
  }

  @Async
  @Override
  public void batchFileMemberRecharge(
      List<MemberRechBalanceVO> list, Integer discountType, UserEquipment userEquipment) {
    list.forEach(
        a -> {
          MemberInfoVO memberInfo = memberService.getMemberInfo(a.getAccount());
          if (memberInfo == null || StringUtils.isEmpty(memberInfo.getAccount())) {
            return;
          }

          // ???????????????????????????
          try {
            ManualRechargeOrderBo manualRechargeOrderBo =
                fillRechargeOrder(a, memberInfo, discountType);
            log.info("???????????????{}", JSON.toJSONString(manualRechargeOrderBo));
            manual(manualRechargeOrderBo, userEquipment);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void add(
      Member member,
      BigDecimal balance,
      RechargeOrder rechargeOrder,
      String content,
      String operator,
      String... remark) {
    MemberBill bill = new MemberBill();
    bill.setBalance(balance);
    bill.setOrderNo(rechargeOrder.getOrderNo());
    bill.setTranType(RechargeMode.getTranType(rechargeOrder.getMode()));
    bill.setAmount(rechargeOrder.getTotalAmount());
    bill.setContent(content);
    bill.setOperator(operator);
    if (remark != null && remark.length > 0 && StringUtils.isNotEmpty(remark[0])) {
      bill.setRemark(remark[0]);
    }
    memberBillService.save(member, bill);
  }

  private void addUserBill(
      RechargeOrder rechargeOrder, Member member, BigDecimal balance, String operator) {
    StringBuilder sb = new StringBuilder();
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
    sb.append("???????????? ")
        .append(rechargeOrder.getOrderNo())
        .append("??????????????? ")
        .append(
            CNYUtils.formatYuanAsYuan(
                rechargeOrder.getPayAmount().setScale(2, BigDecimal.ROUND_DOWN)))
        .append("??????????????? ")
        .append(
            CNYUtils.formatYuanAsYuan(
                rechargeOrder.getDiscountAmount().setScale(2, BigDecimal.ROUND_DOWN)))
        .append("????????? ")
        .append(
            df.format(
                balance.add(rechargeOrder.getTotalAmount().setScale(2, BigDecimal.ROUND_DOWN))));
    if (StringUtils.isNotEmpty(rechargeOrder.getPayType())) {
      sb.append("??????????????? ").append(rechargeOrder.getPayTypeName());
    }
    if (rechargeOrder.getPayAccountId() != null) {
      sb.append("??????????????? ").append(rechargeOrder.getPayAccountAccount());
      if (StringUtils.isNotEmpty(rechargeOrder.getPayAccountOwner())) {
        sb.append("???????????? ").append(rechargeOrder.getPayAccountOwner());
      }
      if (StringUtils.isNotEmpty(rechargeOrder.getPayAccountBankName())) {
        sb.append("??????????????? ").append(rechargeOrder.getPayAccountBankName());
      }
    }
    sb.append("???");
    if (StringUtils.isNotEmpty(rechargeOrder.getTpInterfaceCode())) {
      sb.append("??????????????? ").append(rechargeOrder.getTpInterfaceName());
    }
    add(member, balance, rechargeOrder, sb.toString(), operator, "");
  }

  private void fillClientInfo(RechargeOrder rechargeOrder, UserEquipment clientInfo) {
    rechargeOrder.setBrowser(clientInfo.getUserAgent().getBrowser().getName());
    rechargeOrder.setOs(clientInfo.getUserAgent().getOs().getName());
    rechargeOrder.setIpAddress(clientInfo.getIpAddress());
    rechargeOrder.setUserAgent(clientInfo.getUserAgentString());
  }

  /** ?????????????????? */
  private QueryWrapper<RechargeOrder> builderMemberTodayQuery(GameRWDataReportDto dto) {
    QueryWrapper<RechargeOrder> queryWrapper = new QueryWrapper<>();
    return queryWrapper
        .select("tp_interface_code,tp_interface_name,sum(amount) as amount")
        .eq("status", com.gameplat.common.enums.RechargeStatus.SUCCESS.getValue())
        .between("audit_time", dto.getStartTime(), dto.getEndTime())
        .eq("mode", com.gameplat.common.enums.RechargeStatus.HANDLED.getValue())
        .eq(StringUtils.isNotEmpty(dto.getAccount()), "account", dto.getAccount())
        .eq(
            StringUtils.isNotEmpty(dto.getSuperAccount()) && EnableEnum.isEnabled(dto.getFlag()),
            "super_account",
            dto.getSuperAccount())
        .eq(
            StringUtils.isNotEmpty(dto.getSuperAccount())
                && EnableEnum.DISABLED.match(dto.getFlag()),
            "super_path",
            dto.getSuperAccount())
        .groupBy("tp_interface_code");
  }

  private ManualRechargeOrderBo fillRechargeOrder(
      MemberRechBalanceVO memberRechBalanceVO, MemberInfoVO memberInfoVO, Integer discountType) {
    ManualRechargeOrderBo orderBo = new ManualRechargeOrderBo();
    orderBo.setMemberId(memberInfoVO.getId());
    orderBo.setAccount(memberRechBalanceVO.getAccount());
    orderBo.setPointFlag(1);
    orderBo.setRemarks(memberRechBalanceVO.getRemark());
    orderBo.setAuditRemarks(memberRechBalanceVO.getAuditRemarks());
    orderBo.setSkipAuditing(true);
    orderBo.setAmount(BigDecimal.ZERO);
    orderBo.setNormalDml(BigDecimal.ZERO);
    orderBo.setDiscountType(discountType);
    orderBo.setDiscountAmount(memberRechBalanceVO.getAmount());
    orderBo.setDiscountDml(
        memberRechBalanceVO.getBetMultiple().multiply(memberRechBalanceVO.getAmount()));
    orderBo.setDmlFlag(1);
    return orderBo;
  }
}
