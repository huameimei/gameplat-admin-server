package com.gameplat.admin.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.component.WithdrawQueryCondition;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.convert.MemberWithdrawConvert;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.enums.CashEnum;
import com.gameplat.admin.enums.ProxyPayStatusEnum;
import com.gameplat.admin.feign.MessageFeignClient;
import com.gameplat.admin.mapper.MemberWithdrawMapper;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.model.bean.AdminLimitInfo;
import com.gameplat.admin.model.bean.DirectCharge;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.bean.ProxyPayMerBean;
import com.gameplat.admin.model.dto.MemberWithdrawDTO;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawBankVo;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.constant.NumberConstant;
import com.gameplat.common.constant.SocketEnum;
import com.gameplat.common.enums.*;
import com.gameplat.common.metadata.Pages;
import com.gameplat.common.model.bean.Builder;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.common.model.bean.limit.MemberWithdrawLimit;
import com.gameplat.message.model.MessagePayload;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.message.MessageDistribute;
import com.gameplat.model.entity.pay.PpInterface;
import com.gameplat.model.entity.pay.PpMerchant;
import com.gameplat.model.entity.sys.SysDictData;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWithdrawServiceImpl extends ServiceImpl<MemberWithdrawMapper, MemberWithdraw>
    implements MemberWithdrawService {

  @Autowired private MemberWithdrawConvert userWithdrawConvert;

  @Autowired(required = false)
  private MemberWithdrawMapper memberWithdrawMapper;

  @Autowired private MemberWithdrawHistoryService memberWithdrawHistoryService;

  @Autowired private MemberService memberService;

  @Autowired private SysUserService sysUserService;

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private PpInterfaceService ppInterfaceService;

  @Autowired private PpMerchantService ppMerchantService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private ValidWithdrawService validWithdrawService;

  @Autowired private RechargeOrderService rechargeOrderService;

  @Autowired private BizBlacklistFacade bizBlacklistFacade;

  @Autowired private ConfigService configService;

  @Autowired private MemberRwReportService memberRwReportService;

  @Autowired(required = false)
  private MessageMapper messageMapper;

//  @Autowired private MessageDistributeService messageDistributeService;
  @Autowired private SysDictDataService sysDictDataService;

  @Autowired(required = false)
  private MessageFeignClient client;

  @Autowired
  private DistributedLocker distributedLocker;

  private static boolean verifyPpMerchant(
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppInterface) {
    ProxyPayMerBean proxyPayMerBean = ProxyPayMerBean.conver2Bean(ppMerchant.getMerLimits());
    if (memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMinLimitCash()) < 0
        || memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMaxLimitCash()) > 0) {
      log.info("???????????????????????????????????????????????????????????????????????????????????????" + ppMerchant.getName());
      return true;
    }

    if (StringUtils.isNotEmpty(proxyPayMerBean.getUserLever())) {
      if (!StringUtils.contains(
          "," + proxyPayMerBean.getUserLever() + ",",
          String.format("%s" + memberWithdraw.getMemberLevel() + "%s", ",", ","))) {
        log.info("??????????????????????????????????????????????????????????????????????????????????????????" + ppMerchant.getName());
        return true;
      }
    }

    /** ?????????????????????????????? */
    if (!WithdrawTypeConstant.DIRECT.equals(memberWithdraw.getWithdrawType())
            || !WithdrawTypeConstant.BANK.equals(memberWithdraw.getWithdrawType())) {
      log.info("????????????????????????????????????????????????????????????" + ppMerchant.getName());
      return false;
    }

    List<MemberWithdrawBankVo> bankVoList =
            JSONUtil.toList(
                    (JSONArray) JSONUtil.parseObj(ppInterface.getLimtInfo()).get("banks"),
                    MemberWithdrawBankVo.class);
    // ????????????????????????
    boolean isBankName = true;
    for (MemberWithdrawBankVo ex : bankVoList) {
      if (StringUtils.contains(ex.getName(), memberWithdraw.getBankName())
              || StringUtils.contains(memberWithdraw.getBankName(), ex.getName())) {
        isBankName = false;
        break;
      }
      if (StringUtils.contains(ex.getName(), "??????")
          && StringUtils.contains(memberWithdraw.getBankName(), "??????")) {
        isBankName = false;
        break;
      }
    }
    if (isBankName) {
      log.info("?????????????????????????????????????????????????????????????????????????????????" + ppMerchant.getName());
      return true;
    }
    return false;
  }

  @Autowired
  private WithdrawQueryCondition condition;

  @Override
  public IPage<MemberWithdrawVO> findPage(Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto) {
    QueryWrapper<MemberWithdraw> queryWrapper = condition.buildQuerySql(dto);
    // ??????????????????????????????????????????????????????
    return Pages.of(memberWithdrawMapper.findPage(page, queryWrapper), amountSum());
  }

  @Override
  public void updateCounterFee(Long id, BigDecimal afterCounterFee) {
    if (null == afterCounterFee || afterCounterFee.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ServiceException("???????????????????????????????????????????????????????????????");
    }
    MemberWithdraw memberWithdraw = this.getById(id);
    BigDecimal approveMoney = memberWithdraw.getCashMoney().subtract(afterCounterFee);
    BigDecimal approveCurrencyCount = BigDecimal.ZERO;
    if (Objects.nonNull(memberWithdraw.getWithdrawType())
        && !memberWithdraw.getWithdrawType().equals(WithdrawTypeConstant.BANK)
        && !memberWithdraw.getWithdrawType().equals(WithdrawTypeConstant.DIRECT)) {
      approveCurrencyCount = approveMoney.divide(memberWithdraw.getCurrencyRate());
    }
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update
        .set(MemberWithdraw::getCounterFee, afterCounterFee)
        .set(MemberWithdraw::getApproveMoney, approveMoney)
        .set(MemberWithdraw::getApproveCurrencyCount, approveCurrencyCount)
        .eq(MemberWithdraw::getId, id);
    if (!this.update(new MemberWithdraw(), update)) {
      log.error("??????????????????????????????id=" + id + ",??????????????????afterCounterFee=" + afterCounterFee);
      throw new ServiceException("???????????????");
    }
  }

  @Override
  public void updateRemarks(Long id, String cashReason) {
    if (null == cashReason) {
      throw new ServiceException("????????????????????????!");
    }
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update.set(MemberWithdraw::getCashReason, cashReason).eq(MemberWithdraw::getId, id);
    this.update(new MemberWithdraw(), update);
  }

  @Override
  public void modify(
          Long id,
          Integer cashStatus,
          Integer curStatus,
          UserEquipment userEquipment,
          String cashReason) {
    if (null == id || null == cashStatus || null == curStatus || cashStatus.equals(curStatus)) {
      throw new ServiceException("???????????????.");
    }

    MemberWithdraw memberWithdraw = this.getById(id);
    if (memberWithdraw == null) {
      throw new ServiceException("???????????????????????????????????????");
    }

    // ?????????????????????
    boolean isDirect = WithdrawTypeConstant.DIRECT.equals(memberWithdraw.getWithdrawType());
    Integer origCashStatus = memberWithdraw.getCashStatus();
    if (!curStatus.equals(origCashStatus)) {
      throw new ServiceException("?????????????????????,???????????????.");
    }

    boolean isFinishedOrder =
        (WithdrawStatus.SUCCESS.getValue() == curStatus
            || WithdrawStatus.CANCELLED.getValue() == curStatus
            || WithdrawStatus.REFUSE.getValue() == curStatus);
    if (isFinishedOrder) {
      throw new ServiceException("?????????????????????,???????????????");
    }

    Member member = memberService.getById(memberWithdraw.getMemberId());
    MemberInfo memberInfo = memberInfoService.getById(memberWithdraw.getMemberId());
    if (member == null || memberInfo == null) {
      log.error("??????ID?????????:" + memberWithdraw.getMemberId());
      throw new ServiceException("???????????????");
    }

    UserCredential credential = SecurityUserHolder.getCredential();
    if (WithdrawStatus.UNHANDLED.getValue() != curStatus) {
      // ???????????????????????????????????????????????????????????????
      crossAccountCheck(credential, memberWithdraw);
    }

    // ?????????????????? ,???????????????????????????????????????
    if (ObjectUtil.equals(WithdrawStatus.SUCCESS.getValue(), cashStatus)) {
      withdrawProcess(memberWithdraw, cashStatus);
    }

    // ???????????????????????????????????????????????????
    if (null != credential.getUsername()
        && StringUtils.equals(UserTypes.SUBUSER.value(), credential.getUserType())
        && WithdrawStatus.SUCCESS.match(cashStatus)) {
      SysUser sysUser = sysUserService.getByUsername(credential.getUsername());
      AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          memberWithdraw.getCashMode(),
          memberWithdraw.getCashMoney(),
          credential.getUsername());
    }
    String approveReason = null;
    if (isDirect) {
      approveReason = "????????????";
    }
    // ??????????????????
    memberWithdraw.setMemberLevel(member.getUserLevel());
    memberWithdraw.setCashStatus(cashStatus);
    memberWithdraw.setApproveReason(approveReason);
    boolean toFinishCurrentOrder =
        (WithdrawStatus.SUCCESS.getValue() == cashStatus
            || WithdrawStatus.CANCELLED.getValue() == cashStatus
            || WithdrawStatus.REFUSE.getValue() == cashStatus);
    if (Objects.equals(WithdrawStatus.HANDLED.getValue(), cashStatus)) {
      memberWithdraw.setAcceptAccount(credential.getUsername());
      memberWithdraw.setAcceptTime(new Date());
      updateWithdraw(memberWithdraw, origCashStatus);
      log.info("????????????????????? ??????????????????{}", memberWithdraw.getCashOrderNo());
    } else if (Objects.equals(WithdrawStatus.UNHANDLED.getValue(), cashStatus)) {
      memberWithdraw.setAcceptAccount(credential.getUsername());
      memberWithdraw.setAcceptTime(new Date());
      updateWithdraw(memberWithdraw, origCashStatus);
      log.info("????????????????????????,???????????????{}", memberWithdraw.getCashOrderNo());
    } else if (Objects.equals(
        ProxyPayStatusEnum.PAY_PROGRESS.getCode(), memberWithdraw.getProxyPayStatus())) {
      memberWithdraw.setAcceptAccount(credential.getUsername());
      memberWithdraw.setAcceptTime(new Date());
      updateWithdraw(memberWithdraw, origCashStatus);
      log.info("???????????? ?????????????????? ??????????????????{}", memberWithdraw.getCashOrderNo());
    } else if (toFinishCurrentOrder) {
      String lock_key = String.format(CachedKeys.MEMBER_FINANCE, memberWithdraw.getAccount());
      RLock lock = distributedLocker.lock(lock_key);
      try {
        memberWithdraw.setOperatorAccount(credential.getUsername());
        memberWithdraw.setOperatorTime(new Date());
        updateWithdraw(memberWithdraw, origCashStatus);
        if (WithdrawStatus.SUCCESS.getValue() == cashStatus) {
          // ????????????????????????????????????,??????????????????????????????????????????
          if (!UserTypes.PROMOTION.value().equals(member.getUserType())) {
            memberRwReportService.addWithdraw(
                    member, memberInfo.getTotalWithdrawTimes(), memberWithdraw);
          }
          // ??????????????????????????????????????????
          validWithdrawService.remove(memberWithdraw.getMemberId(), memberWithdraw.getCreateTime());
          // ????????????
          if (isDirect) {
            this.directCharge(memberWithdraw, credential, userEquipment);
          }
          // ??????????????????????????????
          memberInfoService.updateFreeze(member.getId(), memberWithdraw.getCashMoney().negate());
          // ?????????????????????????????????
          memberInfoService.updateUserWithTimes(
                  member.getId(),
                  memberWithdraw.getCashMoney().negate(),
                  memberWithdraw.getPointFlag());
        } else if (WithdrawStatus.CANCELLED.getValue() == cashStatus) { // ??????????????????
          if (ObjectUtil.isNotEmpty(cashReason)) {
            memberWithdraw.setApproveReason(cashReason);
            if (null == cashReason) {
              throw new ServiceException("????????????????????????!");
            }
            LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
            update
                    .set(MemberWithdraw::getApproveReason, cashReason)
                    .set(MemberWithdraw::getOperatorTime, new Date())
                    .set(MemberWithdraw::getOperatorAccount, credential.getUsername())
                    .eq(MemberWithdraw::getId, id);

            this.update(new MemberWithdraw(), update);
          }
          // ??????????????????????????????
          memberInfoService.updateFreeze(member.getId(), memberWithdraw.getCashMoney().negate());
          // ????????????????????????
          memberInfoService.updateBalance(member.getId(), memberWithdraw.getCashMoney());
          String billContent =
                  String.format(
                          "????????????%s?????????%s??????????????????%.3f???,?????????????????????:%.3f???",
                          DateUtil.getNowTime(),
                          member.getAccount(),
                          memberWithdraw.getCashMoney(),
                          (memberInfo.getBalance().add(memberWithdraw.getCashMoney())));
          MemberBill bill = new MemberBill();
          bill.setBalance(memberInfo.getBalance());
          bill.setOrderNo(memberWithdraw.getCashOrderNo());
          bill.setTranType(TranTypes.WITHDRAW_FAIL.getValue());
          bill.setAmount(memberWithdraw.getCashMoney());
          bill.setContent(billContent);
          bill.setOperator(credential.getUsername());
          memberBillService.save(member, bill);
        } else if (WithdrawStatus.REFUSE.getValue() == cashStatus) {
          String content =
                  String.format(
                          "??????%s?????????????????????????????????????????????%s????????????%s",
                          DateUtil.getDateToString(
                                  memberWithdraw.getCreateTime(), DateUtil.YYYY_MM_DD_HH_MM_SS),
                          memberWithdraw.getCashOrderNo(),
                          memberWithdraw.getCashMoney());
          MemberBill bill = new MemberBill();
          bill.setBalance(memberInfo.getBalance());
          bill.setOrderNo(memberWithdraw.getCashOrderNo());
          bill.setTranType(TranTypes.WITHDRAW_FAIL.getValue());
          bill.setAmount(BigDecimal.ZERO);
          bill.setContent(content);
          bill.setOperator(credential.getUsername());
          memberBillService.save(member, bill);
          // ??????????????????????????????
          memberInfoService.updateFreeze(member.getId(), memberWithdraw.getCashMoney().negate());
        }
      } finally {
        distributedLocker.unlock(lock);
      }
      // ??????????????????
      MemberWithdraw withdraw = this.getById(id);
      insertWithdrawHistory(withdraw);
    }
    // ????????????
    if (ObjectUtil.equals(cashStatus, WithdrawStatus.SUCCESS.getValue())
        || ObjectUtil.equals(cashStatus, WithdrawStatus.CANCELLED.getValue())) {
      if (ObjectUtil.equals(WithdrawStatus.SUCCESS.getValue(), cashStatus)) {
        this.addMessageInfo(memberWithdraw, WithdrawStatus.SUCCESS.getValue());
        MemberWithdrawLimit withradLimit = limitInfoService.getWithradLimit();
        this.sendMessage(
            memberWithdraw.getAccount(),
                SocketEnum.SOCKET_WITHDRAW_SUCCESS,
            withradLimit.getUserApplyLoanAfterHintsMessage());
      } else if (ObjectUtil.equals(cashStatus, WithdrawStatus.CANCELLED.getValue())) {
        this.addMessageInfo(memberWithdraw, WithdrawStatus.CANCELLED.getValue());
        this.sendMessage(
            memberWithdraw.getAccount(),
                SocketEnum.SOCKET_WITHDRAW_CANCEL,
            SocketEnum.SEND_WITHDRAW_FAIL_MESSAGE);
      }
    }
  }

  @Override
  public void batchModify(
          List<MemberWithdrawDTO> dtoList,
          WithdrawStatus status,
          UserEquipment equipment,
          String cashReason) {
    dtoList.forEach(
            e -> modify(e.getId(), status.getValue(), e.getCurStatus(), equipment, cashReason));
  }

  /** ????????????????????????????????????????????? */
  @Override
  public List<PpMerchant> queryProxyMerchant(Long id) {
    // ??????????????????????????????????????????????????????
    MemberWithdraw memberWithdraw = this.getById(id);
    if (null == memberWithdraw) {
      throw new ServiceException("??????????????????");
    }

    // ?????????????????????????????????
    List<PpMerchant> merchantList =
        ppMerchantService.queryAllMerchant(SwitchStatusEnum.ENABLED.getValue());
    if (CollectionUtils.isEmpty(merchantList)) {
      throw new ServiceException("???????????????????????????");
    }
    // ???????????????????????????????????????????????????
    Iterator<PpMerchant> iterator = merchantList.iterator();
    while (iterator.hasNext()) {
      PpMerchant ppMerchant = iterator.next();
      PpInterface ppInterface = ppInterfaceService.get(ppMerchant.getPpInterfaceCode());

      if (verifyPpMerchant(memberWithdraw, ppMerchant, ppInterface)) {
        iterator.remove();
      }
    }
    return merchantList;
  }

  @Override
  public void save(BigDecimal cashMoney, String cashReason, Integer handPoints, Long memberId) {
    Member member = memberService.getById(memberId); // ??????????????????????????????????????????
    MemberInfo memberInfo = memberInfoService.getById(memberId); // ??????????????????????????????????????????
    // ??????????????????
    checkUserInfo(member, memberInfo, false);
    // ????????????????????????????????????0
    if (cashMoney.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ServiceException("?????????????????????0");
    }

    // ??????????????????????????????????????????
    if (cashMoney.compareTo(memberInfo.getBalance()) > 0) {
      throw new ServiceException("??????????????????????????????");
    }

    // ???????????????????????????????????????????????????
    UserCredential credential = SecurityUserHolder.getCredential();
    SysUser sysUser = sysUserService.getByUsername(credential.getUsername());
    AdminLimitInfo adminLimitInfo = JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != adminLimitInfo
        && StringUtils.equals(UserTypes.SUBUSER.value(), credential.getUserType())) {
      checkZzhWithdrawAmountAudit(
          adminLimitInfo, CashEnum.CASH_MODE_HAND.getValue(), cashMoney, credential.getUsername());
    }

    // ????????????????????????????????????
    MemberWithdraw memberWithdraw = new MemberWithdraw();
    memberWithdraw.setCashOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
    memberWithdraw.setMemberId(memberId);
    memberWithdraw.setAccount(member.getAccount());
    memberWithdraw.setRealName(member.getRealName());
    memberWithdraw.setAccountMoney(memberInfo.getBalance());
    memberWithdraw.setCashMoney(cashMoney);
    memberWithdraw.setCashReason(cashReason);
    memberWithdraw.setCashMode(CashEnum.CASH_MODE_HAND.getValue());
    memberWithdraw.setCashStatus(WithdrawStatus.SUCCESS.getValue());
    memberWithdraw.setCreateTime(new Date());
    memberWithdraw.setCounterFee(BigDecimal.ZERO);
    memberWithdraw.setApproveMoney(cashMoney);
    memberWithdraw.setMemberLevel(member.getUserLevel());
    memberWithdraw.setSuperId(member.getParentId());
    memberWithdraw.setSuperName(member.getParentName());
    memberWithdraw.setSuperPath(member.getSuperPath());
    memberWithdraw.setPoliceFlag(0);
    memberWithdraw.setMacOs(credential.getDeviceType());
    memberWithdraw.setUserAgent(credential.getUserAgent());
    memberWithdraw.setIpAddress(credential.getLoginIp());
    memberWithdraw.setOperatorAccount(credential.getUsername());
    memberWithdraw.setOperatorTime(new Date());
    memberWithdraw.setPointFlag(handPoints);
    memberWithdraw.setMemberType(member.getUserType());
    memberWithdraw.setWithdrawType(WithdrawTypeConstant.MANUAL); // ?????????????????????????????????????????????
    this.save(memberWithdraw);
    // ????????????????????????
    insertWithdrawHistory(memberWithdraw);

    // ????????????????????????????????????,??????????????????????????????????????????
    if (!UserTypes.PROMOTION.value().equals(member.getUserType())) {
      memberRwReportService.addWithdraw(member, memberInfo.getTotalWithdrawTimes(), memberWithdraw);
    }

    // ???????????????????????????????????????????????????
    memberInfoService.updateBalanceWithWithdraw(memberId, memberWithdraw.getCashMoney());

    // ???????????????????????????????????????
    String content =
        "?????????"
            + sysUser.getUserName()
            + "???"
            + DateUtil.getNowTime()
            + "?????????"
            + member.getAccount()
            + "????????????????????????"
            + String.format("%.3f", cashMoney)
            + "???,?????????????????????:"
            + String.format("%.3f", (memberInfo.getBalance().subtract(cashMoney)))
            + "???";
    MemberBill bill = new MemberBill();
    bill.setBalance(memberInfo.getBalance());
    bill.setOrderNo(memberWithdraw.getCashOrderNo());
    bill.setTranType(TranTypes.TRANSFER_OUT.getValue());
    bill.setAmount(cashMoney.negate());
    bill.setContent(content);
    bill.setOperator(credential.getUsername());
    memberBillService.save(member, bill);
  }

  /**
   * ????????????
   *
   * @param memberWithdraw MemberWithdraw
   * @param userCredential UserCredential
   */
  @SneakyThrows
  public void directCharge(
      MemberWithdraw memberWithdraw, UserCredential userCredential, UserEquipment userEquipment) {
    String configValue = configService.getValue(DictDataEnum.DIRECT_CHARGE);
    Optional.ofNullable(configValue).orElseThrow(() -> new ServiceException("?????????????????????????????????????????????????????????"));
    DirectCharge directCharge = JSON.parseObject(configValue, DirectCharge.class);
    String levels = directCharge.getLevels();
    int pointFlag = directCharge.getPointFlag();
    int dmlFlag = directCharge.getDmlFlag();
    int normalDmlMultiple = directCharge.getNormalDmlMultiple();
    int discountDmlMultiple = directCharge.getDiscountDmlMultiple();
    BigDecimal discountPercentage = directCharge.getDiscountPercentage();
    BigDecimal discountDml = BigDecimal.ZERO;
    BigDecimal discountAmount = BigDecimal.ZERO;
    BigDecimal approveMoney = memberWithdraw.getApproveMoney();
    // ??????????????????
    boolean skipAuditing = directCharge.getSkipAuditing() == 1;
    // ?????????
    Integer discountType = 8080;
    // ???????????????
    BigDecimal normalDml = BigDecimal.ZERO;
    String auditRemarks =
        StringUtil.isBlank(directCharge.getAuditRemarks())
            ? "?????????????????????????????????" + memberWithdraw.getCashOrderNo()
            : directCharge.getAuditRemarks(); // ????????????

    if (!StringUtil.isBlank(levels)) {
      String[] levelArr = levels.split(",");
      boolean contains = Arrays.asList(levelArr).contains(memberWithdraw.getMemberLevel() + "");
      if (contains) {
        if (pointFlag == 1) {
          discountAmount =
              (approveMoney.multiply(discountPercentage))
                  .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        }
      }
    }

    if (dmlFlag == 1) {
      normalDml =
          new BigDecimal(normalDmlMultiple)
              .multiply(approveMoney)
              .setScale(2, RoundingMode.HALF_UP);
      discountDml =
          discountAmount
              .multiply(new BigDecimal(discountDmlMultiple))
              .setScale(2, RoundingMode.HALF_UP);
    }

    ManualRechargeOrderBo manualRechargeOrderBo =
        Builder.of(ManualRechargeOrderBo::new)
            .with(ManualRechargeOrderBo::setDiscountAmount, discountAmount)
            .with(ManualRechargeOrderBo::setAuditRemarks, auditRemarks)
            .with(ManualRechargeOrderBo::setPointFlag, pointFlag)
            .with(ManualRechargeOrderBo::setDmlFlag, dmlFlag)
            .with(ManualRechargeOrderBo::setSkipAuditing, skipAuditing)
            .with(ManualRechargeOrderBo::setAccount, memberWithdraw.getAccount())
            .with(ManualRechargeOrderBo::setMemberId, memberWithdraw.getMemberId())
            .with(ManualRechargeOrderBo::setAmount, approveMoney)
            .with(ManualRechargeOrderBo::setNormalDml, normalDml)
            .with(ManualRechargeOrderBo::setDiscountDml, discountDml)
            .with(ManualRechargeOrderBo::setDiscountType, discountType)
            .with(ManualRechargeOrderBo::setRemarks, directCharge.getRemarks())
            .build();
    rechargeOrderService.manual(manualRechargeOrderBo, userEquipment);
    log.info(
        "\n??????????????????:{},\n??????????????? ???????????????{}??????????????????{}??????????????????{}?????????????????????{},??????????????????{},?????????{},?????????{}",
        directCharge,
        manualRechargeOrderBo.getAccount(),
        manualRechargeOrderBo.getAmount(),
        manualRechargeOrderBo.getDiscountAmount(),
        manualRechargeOrderBo.getNormalDml(),
        manualRechargeOrderBo.getDiscountDml(),
        manualRechargeOrderBo.getAuditRemarks(),
        directCharge.getLevels());

    // ???????????????????????????????????????
    if (skipAuditing) {
      // ???????????????????????????????????????
      if (bizBlacklistFacade.isUserInBlacklist(
          memberWithdraw.getMemberId(), BizBlacklistType.RECHARGE_GROWTH)) {
        log.info(
            "???????????????????????????cashOrderNo={}??????????????????account={}?????????????????????????????????????????????'???????????????'????????????",
            memberWithdraw.getCashOrderNo(),
            memberWithdraw.getAccount());
      }
    }
  }

  @Override
  public long getUntreatedWithdrawCount() {
    return this.lambdaQuery().eq(MemberWithdraw::getCashStatus, 1).count();
  }

  private void insertWithdrawHistory(MemberWithdraw memberWithdraw) {
    MemberWithdrawHistory memberWithdrawHistory = new MemberWithdrawHistory();
    BeanUtils.copyProperties(memberWithdraw, memberWithdrawHistory);
    memberWithdrawHistoryService.save(memberWithdrawHistory);
  }

  /**
   * ?????????????????????????????????
   *
   * @param adminLimitInfo AdminLimitInfo
   * @param cashMode Integer
   */
  private void checkZzhWithdrawAmountAudit(
      AdminLimitInfo adminLimitInfo, Integer cashMode, BigDecimal cashMoney, String userName) {
    if (cashMode == CashEnum.CASH_MODE_USER.getValue()) {
      if (adminLimitInfo.getMaxWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxWithdrawAmount()) > 0) {
        String buffer =
            userName
                + "???????????????????????????????????????????????????"
                + adminLimitInfo.getMaxWithdrawAmount()
                + "?????? ????????????"
                + MoneyUtils.toYuanStr(cashMoney.subtract(adminLimitInfo.getMaxWithdrawAmount()))
                + "???";
        throw new ServiceException(buffer);
      }
    } else if (cashMode == CashEnum.CASH_MODE_HAND.getValue()) {
      if (adminLimitInfo.getMaxManualWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxManualWithdrawAmount()) > 0) {
        String buffer =
            userName
                + "??????????????????????????????????????????????????????"
                + adminLimitInfo.getMaxManualWithdrawAmount()
                + "?????? ????????????"
                + MoneyUtils.toYuanStr(
                    cashMoney.subtract(adminLimitInfo.getMaxManualWithdrawAmount()))
                + "???";
        throw new ServiceException(buffer);
      }
    }
  }

  private void updateWithdraw(MemberWithdraw memberWithdraw, Integer origCashStatus) {
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update
        .set(MemberWithdraw::getCashStatus, memberWithdraw.getCashStatus())
            .set(MemberWithdraw::getAcceptAccount, memberWithdraw.getAcceptAccount())
            .set(MemberWithdraw::getAcceptTime, memberWithdraw.getAcceptTime())
        .set(MemberWithdraw::getApproveReason, memberWithdraw.getCashReason())
        .set(MemberWithdraw::getOperatorAccount, memberWithdraw.getOperatorAccount())
        .set(MemberWithdraw::getOperatorTime, memberWithdraw.getOperatorTime())
        .eq(MemberWithdraw::getId, memberWithdraw.getId())
        .eq(MemberWithdraw::getCashStatus, origCashStatus);
    if (!this.update(update)) {
      log.error(
          "???????????????????????????memberWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + origCashStatus);
      throw new ServiceException("???????????????");
    }
  }

  /** ????????????????????????????????????????????????????????? ??????????????????????????????????????? */
  private void crossAccountCheck(UserCredential userCredential, MemberWithdraw memberWithdraw) {
    if (userCredential != null
        && StringUtils.isNotEmpty(userCredential.getUsername())
        && null != memberWithdraw) {

      MemberRechargeLimit limitInfo = limitInfoService.getRechargeLimit();
      boolean toCheck =
          BooleanEnum.NO.match(limitInfo.getIsHandledAllowOthersOperate())
              && !userCredential.isSuperAdmin();
      if (toCheck) {
        if (!Objects.equals(WithdrawStatus.UNHANDLED.getValue(), memberWithdraw.getCashStatus())
            && !StringUtils.equalsIgnoreCase(
                userCredential.getUsername(), memberWithdraw.getAcceptAccount())) {
          throw new ServiceException("????????????????????????:" + memberWithdraw.getCashOrderNo());
        }
      }
    }
  }

  /** ????????????????????????????????? */
  private void withdrawProcess(MemberWithdraw memberWithdraw, Integer cashStatus) {
    MemberWithdrawLimit withradLimit = limitInfoService.getWithradLimit();
    boolean withdrawProcess = BooleanEnum.YES.match(withradLimit.getIsWithdrawProcess());
    if (withdrawProcess
        && memberWithdraw.getCashStatus() != WithdrawStatus.HANDLED.getValue()
        && WithdrawStatus.SUCCESS.getValue() == cashStatus) {
      throw new ServiceException("?????????????????????:" + memberWithdraw.getCashOrderNo());
    }
  }

  /** ????????????????????????????????? */
  private void checkUserInfo(Member member, MemberInfo memberInfo, boolean checkUserState) {
    // ????????????????????????
    if (member == null) {
      throw new ServiceException("????????????????????????");
    }
    // ???????????????????????????????????????
    if (memberInfo == null) {
      throw new ServiceException("??????????????????????????????");
    }
    if (checkUserState) {
      // ????????????????????????
      if (!MemberEnums.Status.ENABlED.match(member.getStatus())) {
        throw new ServiceException("?????????????????????");
      }
    }
    if (member.getUserType().equals(UserTypes.TEST.value())) {
      throw new ServiceException("?????????????????????");
    }
  }

  /**
   * ????????????
   *
   * @param memberWithdraw ????????????
   * @param state ??????
   */
  private void addMessageInfo(MemberWithdraw memberWithdraw, Integer state) {
    if (this.verifyMessage() == com.gameplat.common.enums.TrueFalse.FALSE.getValue()) {
      return;
    }
    Message messageInfo = new Message();
    messageInfo.setTitle(title(state));
    messageInfo.setContent(
        context(state, memberWithdraw.getCreateTime(), memberWithdraw.getCashMoney()));
    messageInfo.setCategory(4);
    messageInfo.setPosition(1);
    messageInfo.setShowType(0);
    messageInfo.setPopsCount(0);
    messageInfo.setPushRange(2);
    messageInfo.setLinkAccount(memberWithdraw.getAccount());
    messageInfo.setSort(0);
    messageInfo.setType(1);
    messageInfo.setLanguage("zh-CN");
    messageInfo.setStatus(1);
    messageInfo.setImmediateFlag(0);
    messageInfo.setRemarks("????????????");
    messageInfo.setCreateBy("system");
    messageInfo.setCreateTime(new Date());
    messageMapper.insert(messageInfo);

  }

  private void sendMessage(String account, String channel, String message) {
    MessagePayload payload = MessagePayload.builder().channel(channel).title(message).build();
    log.info("????????????=============>????????????Socket??????,????????????{}", payload);
    client.sendToUser(account, payload);
  }

  private int verifyMessage() {
    SysDictData sysDictData =
        sysDictDataService.getDictData(
            DictTypeEnum.SYSTEM_PARAMETER_CONFIG.getValue(),
            DictDataEnum.WITHDRAW_PUSH_MSG.getLabel());
    return ObjectUtil.isNull(sysDictData) ? 0 : Convert.toInt(sysDictData.getDictValue());
  }

  private String context(Integer state, Date date, BigDecimal money) {
    String context;
    String dateStr = DateUtil.dateToStr(date, DateUtil.YYYY_MM_DD_HH_MM_SS);
    if (ObjectUtil.equals(3, state)) {
      context = "??????" + dateStr + "?????????" + money.setScale(2, RoundingMode.CEILING) + "??????????????????";
    } else {
      context = "??????" + dateStr + "?????????" + money.setScale(2, RoundingMode.CEILING) + "????????????";
    }
    return context;
  }

  private String title(int state) {
    String title;
    if (ObjectUtil.equals(state, 3)) {
      title = "????????????";
    } else {
      title = "????????????";
    }

    return title;
  }

  private SummaryVO amountSum() {
    LambdaQueryWrapper<MemberWithdraw> queryHandle = Wrappers.lambdaQuery();
    SummaryVO summaryVO = new SummaryVO();
    queryHandle.eq(MemberWithdraw::getCashStatus, WithdrawStatus.HANDLED.getValue());
    summaryVO.setAllHandledSum(memberWithdrawMapper.summaryMemberWithdraw(queryHandle));

    LambdaQueryWrapper<MemberWithdraw> queryUnHandle = Wrappers.lambdaQuery();
    queryUnHandle.eq(MemberWithdraw::getCashStatus, WithdrawStatus.UNHANDLED.getValue());
    summaryVO.setAllUnhandledSum(memberWithdrawMapper.summaryMemberWithdraw(queryUnHandle));
    return summaryVO;
  }
}
