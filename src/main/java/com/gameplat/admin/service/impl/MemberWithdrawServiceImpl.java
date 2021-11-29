package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.convert.MemberWithdrawConvert;
import com.gameplat.admin.enums.AllowOthersOperateEnums;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.enums.CashEnum;
import com.gameplat.admin.enums.LimitEnums;
import com.gameplat.admin.enums.ProxyPayStatusEnum;
import com.gameplat.admin.enums.TranTypes;
import com.gameplat.admin.enums.UserStates;
import com.gameplat.admin.enums.WithdrawStatus;
import com.gameplat.admin.mapper.MemberWithdrawMapper;
import com.gameplat.admin.model.bean.AdminLimitInfo;
import com.gameplat.admin.model.bean.DirectCharge;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.bean.ProxyPayMerBean;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.domain.MemberWithdraw;
import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.domain.PpInterface;
import com.gameplat.admin.model.domain.PpMerchant;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.domain.limit.MemberRechargeLimit;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.admin.model.vo.SummaryVO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.MemberBalanceService;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import com.gameplat.admin.service.MemberWithdrawService;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.admin.service.PpMerchantService;
import com.gameplat.admin.service.RechargeOrderService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.service.ValidWithdrawService;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.common.model.bean.Builder;
import com.gameplat.common.util.DateUtil;
import com.gameplat.security.context.UserCredential;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.util.StringUtils;
import com.gameplat.common.exception.ServiceException;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWithdrawServiceImpl extends ServiceImpl<MemberWithdrawMapper, MemberWithdraw>
    implements MemberWithdrawService {

  @Autowired
  private MemberWithdrawConvert userWithdrawConvert;

  @Autowired
  private MemberWithdrawMapper memberWithdrawMapper;

  @Autowired
  private MemberWithdrawHistoryService memberWithdrawHistoryService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private MemberBalanceService memberBalanceService;

  @Autowired
  private LimitInfoService limitInfoService;

  @Autowired
  private PpInterfaceService ppInterfaceService;

  @Autowired
  private PpMerchantService ppMerchantService;

  @Autowired
  private MemberInfoService memberInfoService;

  @Autowired
  private MemberBillService memberBillService;

  @Autowired
  private ValidWithdrawService validWithdrawService;

  @Autowired
  private RechargeOrderService rechargeOrderService;

  @Autowired
  private BizBlacklistFacade bizBlacklistFacade;

  @Autowired
  private SysDictDataService sysDictDataService;

  @Override
  public PageExt<MemberWithdrawVO, SummaryVO> findPage(
      Page<MemberWithdraw> page, MemberWithdrawQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdraw> query = Wrappers.lambdaQuery();
    query
        .in(
            ObjectUtils.isNotNull(dto.getBankNameList()),
            MemberWithdraw::getBankName,
            dto.getBankNameList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSuperName()),
            MemberWithdraw::getSuperName,
            dto.getSuperName())
        .eq(
            ObjectUtils.isNotEmpty(dto.getBankCard()),
            MemberWithdraw::getBankCard,
            dto.getBankCard())
        .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberWithdraw::getAccount, dto.getAccount())
        .ge(
            ObjectUtils.isNotEmpty(dto.getCashMoneyFrom()),
            MemberWithdraw::getCashMoney,
            dto.getCashMoneyFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getCashMoneyFromTo()),
            MemberWithdraw::getCashMoney,
            dto.getCashMoneyFromTo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType()),
            MemberWithdraw::getMemberType,
            dto.getMemberType())
        .eq(
            ObjectUtils.isNotEmpty(dto.getCashOrderNo()),
            MemberWithdraw::getCashOrderNo,
            dto.getCashOrderNo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getOperatorAccount()),
            MemberWithdraw::getOperatorAccount,
            dto.getOperatorAccount())
        .ge(
            ObjectUtils.isNotNull(dto.getCreateTimeFrom()),
            MemberWithdraw::getCreateTime,
            dto.getCreateTimeFrom())
        .le(
            ObjectUtils.isNotNull(dto.getCreateTimeTo()),
            MemberWithdraw::getCreateTime,
            dto.getCreateTimeTo())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            MemberWithdraw::getMemberLevel,
            dto.getMemberLevelList());
    if (ObjectUtils.isNotNull(dto.getRechargeStatusList())
        && dto.getRechargeStatusList().size() > 0) {
      query
          .eq(dto.getRechargeStatusList().contains(3), MemberWithdraw::getWithdrawType, "BANK")
          .eq(dto.getRechargeStatusList().contains(4), MemberWithdraw::getWithdrawType, "DIRECT")
          .notIn(
              dto.getRechargeStatusList().contains(5),
              MemberWithdraw::getWithdrawType,
              "BANK",
              "MANUAL",
              "DIRECT")
          .gt(dto.getRechargeStatusList().contains(6), MemberWithdraw::getCounterFee, 0);
    }
    if (ObjectUtils.isNotNull(dto.getCashStatusList())) {
      query.in(MemberWithdraw::getCashStatus, dto.getCashStatusList());
    } else {
      query.le(MemberWithdraw::getCashStatus, WithdrawStatus.HANDLED.getValue());
    }
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        dto.getOrder().equals("ASC"),
        MemberWithdraw::getCreateTime);
    IPage<MemberWithdrawVO> data = this.page(page, query).convert(userWithdrawConvert::toVo);
    // 统计受理订单总金额、未受理订单总金额
    SummaryVO summaryVO = amountSum();
    return new PageExt<MemberWithdrawVO, SummaryVO>(data, summaryVO);
  }

  @Override
  public void updateCounterFee(Long id, BigDecimal afterCounterFee) {
    if (null == afterCounterFee || afterCounterFee.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ServiceException("调整后的手续费不能为空或者为负数，请检查！");
    }
    MemberWithdraw memberWithdraw = this.getById(id);
    BigDecimal approveMoney = memberWithdraw.getCashMoney().subtract(afterCounterFee);
    BigDecimal approveCurrencyCount = BigDecimal.ZERO;
    if (Objects.nonNull(memberWithdraw.getWithdrawType())
        && !memberWithdraw.getWithdrawType().equals(WithdrawTypeConstant.BANK_SUB_TYPE)
        && !memberWithdraw.getWithdrawType().equals(WithdrawTypeConstant.DIRECT_SUB_TYPE)) {
      approveCurrencyCount = approveMoney.divide(memberWithdraw.getCurrencyRate());
    }
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update
        .set(MemberWithdraw::getCounterFee, afterCounterFee)
        .set(MemberWithdraw::getApproveMoney, approveMoney)
        .set(MemberWithdraw::getApproveCurrencyCount, approveCurrencyCount)
        .eq(MemberWithdraw::getId, id);
    if (!this.update(new MemberWithdraw(), update)) {
      log.error("更新提现订单手续费：id=" + id + ",调整后手续费afterCounterFee=" + afterCounterFee);
      throw new ServiceException("UW/UPDATE_ERROR, 订单已处理", null);
    }
  }

  @Override
  public void updateRemarks(Long id, String cashReason) {
    if (null == cashReason) {
      throw new ServiceException("备注信息不能为空!");
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
      boolean isDirect,
      String approveReason,
      UserCredential userCredential) throws Exception {
    if (cashStatus.equals(curStatus) || null == id || null == cashStatus || null == curStatus) {
      throw new ServiceException("错误的参数.");
    }
    MemberWithdraw memberWithdraw = memberWithdrawMapper.selectById(id);

    if (memberWithdraw == null) {
      throw new ServiceException("UW/ORDER_NULL,充值订单不存在或订单已处理", null);
    }

    Integer origCashStatus = memberWithdraw.getCashStatus();
    if (!curStatus.equals(origCashStatus)) {
      throw new ServiceException("UW/ORDER_PROCESSED,订单状态已变化,请刷新重试.", null);
    }

    boolean isFinishedOrder =
        (WithdrawStatus.SUCCESS.getValue() == curStatus
            || WithdrawStatus.CANCELLED.getValue() == curStatus
            || WithdrawStatus.REFUSE.getValue() == curStatus);
    if (isFinishedOrder) {
      throw new ServiceException("UW/ORDER_NULL,出款订单已完成,请确认再试", null);
    }

    Member member = memberService.getById(memberWithdraw.getMemberId());
    MemberInfo memberInfo = memberInfoService.getById(memberWithdraw.getMemberId());
    if (member == null || memberInfo == null) {
      log.error("用户ID不存在:" + memberWithdraw.getMemberId());
      throw new ServiceException("UC/EXT_INFO_NULL,用户不存在", null);
    }
    if (WithdrawStatus.UNHANDLED.getValue() != curStatus) {
      // 验证已受理出款订单是否开启允许其他账户验证

      crossAccountCheck(userCredential, memberWithdraw);
    }
    /** 校验子账号当天受理会员取款审核额度 */
    if (null != userCredential.getUsername()
        && StringUtils.equals(UserTypes.SUBUSER.value(), userCredential.getUserType())
        && WithdrawStatus.SUCCESS.getValue() == cashStatus) {
      SysUser sysUser = null;
      if (null != userCredential) {
        sysUser = sysUserService.getByUsername(userCredential.getUsername());
      }
      AdminLimitInfo adminLimitInfo =
          JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          memberWithdraw.getCashMode(),
          memberWithdraw.getCashMoney(),
          userCredential.getUsername());
    }

    if (isDirect) {
      approveReason = approveReason != null ? approveReason + "(免提直充)" : "免提直充";
    }
    memberWithdraw.setCashStatus(cashStatus);
    memberWithdraw.setApproveReason(approveReason);
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    memberWithdraw.setOperatorTime(new Date());
    // 修改订单状态
    updateWithdraw(memberWithdraw, origCashStatus);
    boolean toFinishCurrentOrder =
        (WithdrawStatus.SUCCESS.getValue() == cashStatus
            || WithdrawStatus.CANCELLED.getValue() == cashStatus
            || WithdrawStatus.REFUSE.getValue() == cashStatus);
    if (WithdrawStatus.HANDLED.getValue() == cashStatus) {
      log.info("提现订单受理中 ，订单号为：{}", memberWithdraw.getCashOrderNo());
    } else if (WithdrawStatus.UNHANDLED.getValue() == cashStatus) {
      log.info("放弃受理提现订单,订单号为：{}", memberWithdraw.getCashOrderNo());
    } else if (Objects.equals(
        ProxyPayStatusEnum.PAY_PROGRESS.getCode(), memberWithdraw.getProxyPayStatus())) {
      log.info("提现订单 第三方出款中 ，订单号为：{}", memberWithdraw.getCashOrderNo());
    } else if (toFinishCurrentOrder) {
      if (WithdrawStatus.SUCCESS.getValue() == cashStatus) {
        // 扣除会员余额
        memberBalanceService.updateBalance(member.getId(), memberWithdraw.getCashMoney().negate());
        // 更新充提报表,如果是推广账户不进入报表
        // TODO: 2021/11/2  未完成
        // 删除出款验证打码量记录的数据
        validWithdrawService.remove(memberWithdraw.getMemberId(), memberWithdraw.getCreateTime());
        // 免提直充
        if (isDirect) {
          this.directCharge(memberWithdraw, userCredential);
        }

      } else if (WithdrawStatus.CANCELLED.getValue() == cashStatus) { // 取消出款操作
        // 释放会员提现金额
        memberBalanceService.updateBalance(member.getId(), memberWithdraw.getCashMoney());
        String billContent = String.format("管理员于%s向用户%s提现失败退回%.3f元,账户余额变更为:%.3f元",
            DateUtil.getNowTime(),
            member.getAccount(),
            memberWithdraw.getCashMoney(),
            (memberInfo.getBalance().add(memberWithdraw.getCashMoney()))
        );
        MemberBill bill = new MemberBill();
        bill.setBalance(memberInfo.getBalance());
        bill.setOrderNo(memberWithdraw.getCashOrderNo());
        bill.setTranType(TranTypes.WITHDRAW_FAIL.getValue());
        bill.setAmount(memberWithdraw.getCashMoney());
        bill.setContent(billContent);
        bill.setOperator(userCredential.getUsername());
        memberBillService.save(member, bill);
        //        if (isPush) {
        //          String content = String.format("您于%s提交的取现订单被取消，订单号为%s",
        //              DateUtil.getDateToString(userWithdraw.getAddTime(),
        // DateUtil.YYYY_MM_DD_HH_MM_SS),
        //              userWithdraw.getCashOrderNo());
        //          if (StringUtil.isNotBlank(userWithdraw.getApproveReason())) {
        //            content += String.format(",取消原因:%s", userWithdraw.getApproveReason());
        //          }
        //          pushMessageService.saveByCashier(userWithdraw.getUserId(), content);
        //        }
      } else if (WithdrawStatus.REFUSE.getValue() == cashStatus) {
        String content = String.format("您于%s提交的取现订单被没收，订单号为%s，金额：%s",
            DateUtil.getDateToString(memberWithdraw.getCreateTime(),
                DateUtil.YYYY_MM_DD_HH_MM_SS),
            memberWithdraw.getCashOrderNo(),
            memberWithdraw.getCashMoney());
        MemberBill bill = new MemberBill();
        bill.setBalance(memberInfo.getBalance());
        bill.setOrderNo(memberWithdraw.getCashOrderNo());
        bill.setTranType(TranTypes.WITHDRAW_FAIL.getValue());
        bill.setAmount(BigDecimal.ZERO);
        bill.setContent(content);
        bill.setOperator(userCredential.getUsername());
        memberBillService.save(member, bill);
        //        if (isPush) {
        //          pushMessageService.saveByCashier(userWithdraw.getUserId(), content);
        //        }
      }
      // 新增出款记录
      insertWithdrawHistory(memberWithdraw);
      //    }
    }
  }

  /**
   * 过滤不符合规则的第三方出款商户
   */
  @Override
  public List<PpMerchant> queryProxyMerchant(Long id) {
    // 根据体现记录查询用户的层级和出款金额
    MemberWithdraw memberWithdraw = this.getById(id);
    if (null == memberWithdraw) {
      throw new ServiceException("不存在的记录");
    }

    // 获取所有的可用代付商户
    List<PpMerchant> merchantList = ppMerchantService.queryAllMerchant(0);
    if (CollectionUtils.isEmpty(merchantList)) {
      throw new ServiceException("没有可用的代付商户");
    }
    // 根据用户体现信息，过滤相关代付相符
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
  public void save(
      BigDecimal cashMoney,
      String cashReason,
      Integer handPoints,
      UserCredential userCredential,
      Long memberId)
      throws Exception {
    Member member = memberService.getById(memberId); // 更新金额，从数据库中重新获取
    MemberInfo memberInfo = memberInfoService.getById(memberId); // 更新金额，从数据库中重新获取
    // 校验用户状态
    checkUserInfo(member, memberInfo, false);
    // 判断金额是否为负数或者为0
    if (cashMoney.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ServiceException("取款额度不能为0");
    }

    // 检查取款金额是否超过用户余额
    if (cashMoney.compareTo(memberInfo.getBalance()) > 0) {
      throw new ServiceException("取款额度不能超过余额");
    }

    /** 校验子账号当天受理人工取款审核额度 */
    SysUser sysUser = sysUserService.getByUsername(userCredential.getUsername());
    AdminLimitInfo adminLimitInfo =
        JsonUtils.parse(sysUser.getLimitInfo(), AdminLimitInfo.class);
    if (null != adminLimitInfo
        && StringUtils.equals(UserTypes.SUBUSER.value(), userCredential.getUserType())) {
      checkZzhWithdrawAmountAudit(
          adminLimitInfo,
          CashEnum.CASH_MODE_HAND.getValue(),
          cashMoney,
          userCredential.getUsername());
    }

    // 下面开始添加后台出款记录
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
    memberWithdraw.setMemberLevel(member.getUserLevel().toString());
    memberWithdraw.setSuperId(member.getParentId());
    memberWithdraw.setSuperName(member.getParentName());
    memberWithdraw.setSuperPath(member.getSuperPath());
    memberWithdraw.setPoliceFlag(0);
    memberWithdraw.setMacOs(userCredential.getDeviceType());
    memberWithdraw.setUserAgent(userCredential.getUserAgent());
    memberWithdraw.setIpAddress(userCredential.getLoginIp());
    memberWithdraw.setOperatorAccount(userCredential.getUsername());
    memberWithdraw.setOperatorTime(new Date());
    memberWithdraw.setPointFlag(handPoints);
    memberWithdraw.setMemberType(member.getUserType());
    memberWithdraw.setWithdrawType(WithdrawTypeConstant.MANUAL); // 人工充值设置提现类型为人工充值
    this.save(memberWithdraw);
    // 添加取现历史记录
    insertWithdrawHistory(memberWithdraw);

    // 修改用户金额信息、扣除用户出款金额
    memberBalanceService.updateBalance(
        memberWithdraw.getMemberId(), memberWithdraw.getCashMoney().negate());
    memberInfoService.updateMemberWithdraw(memberInfo, memberWithdraw.getCashMoney());

    // 添加用户账户金额的变更记录
    String content = "管理员" /*+ admin.getAccount() */ + "于" + DateUtil.getNowTime() + "向用户"
        + member.getAccount() + "成功通过后台转出" + String.format("%.3f",
        cashMoney)
        + "元,账户余额变更为:" + String.format("%.3f", (memberInfo.getBalance().subtract(cashMoney)))
        + "元";
    MemberBill bill = new MemberBill();
    bill.setBalance(memberInfo.getBalance());
    bill.setOrderNo(memberWithdraw.getCashOrderNo());
    bill.setTranType(TranTypes.TRANSFER_OUT.getValue());
    bill.setAmount(cashMoney.negate());
    bill.setContent(content);
    bill.setOperator(userCredential.getUsername());
    memberBillService.save(member, bill);
  }

  private void insertWithdrawHistory(MemberWithdraw memberWithdraw) {
    MemberWithdrawHistory memberWithdrawHistory = new MemberWithdrawHistory();
    BeanUtils.copyProperties(memberWithdraw, memberWithdrawHistory);
    memberWithdrawHistoryService.save(memberWithdrawHistory);
  }

  /**
   * 检验子账号出款受理额度
   *
   * @param adminLimitInfo
   * @param cashMode
   * @throws Exception
   */
  public void checkZzhWithdrawAmountAudit(
      AdminLimitInfo adminLimitInfo, Integer cashMode, BigDecimal cashMoney, String userName) {
    if (cashMode == CashEnum.CASH_MODE_USER.getValue()) {
      if (adminLimitInfo.getMaxWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxWithdrawAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("单笔出款受限。受理会员取款额度为：")
                .append(adminLimitInfo.getMaxWithdrawAmount())
                .append("元。 超过额度")
                .append(
                    MoneyUtils.toYuanStr(cashMoney.subtract(adminLimitInfo.getMaxWithdrawAmount())))
                .append("元");
        throw new ServiceException(buffer.toString());
      }
    } else if (cashMode == CashEnum.CASH_MODE_HAND.getValue()) {
      if (adminLimitInfo.getMaxManualWithdrawAmount().compareTo(BigDecimal.ZERO) > 0
          && cashMoney.compareTo(adminLimitInfo.getMaxManualWithdrawAmount()) > 0) {
        StringBuffer buffer =
            new StringBuffer(userName)
                .append("单笔人工出款受限。受理人工取款额度为")
                .append(adminLimitInfo.getMaxManualWithdrawAmount())
                .append("元。 超过额度")
                .append(
                    MoneyUtils.toYuanStr(
                        cashMoney.subtract(adminLimitInfo.getMaxManualWithdrawAmount())))
                .append("元");
        throw new ServiceException(buffer.toString());
      }
    }
  }

  private void updateWithdraw(MemberWithdraw memberWithdraw, Integer origCashStatus) {
    LambdaUpdateWrapper<MemberWithdraw> update = Wrappers.lambdaUpdate();
    update
        .set(MemberWithdraw::getCashStatus, memberWithdraw.getCashStatus())
        .set(MemberWithdraw::getApproveReason, memberWithdraw.getCashReason())
        .set(MemberWithdraw::getOperatorAccount, memberWithdraw.getOperatorAccount())
        .set(MemberWithdraw::getOperatorTime, memberWithdraw.getOperatorTime())
        .eq(MemberWithdraw::getId, memberWithdraw.getId())
        .eq(MemberWithdraw::getCashStatus, origCashStatus);
    if (!this.update(update)) {
      log.error(
          "修改提现订单异常：memberWithdraw="
              + memberWithdraw.toString()
              + ",origCashStatus="
              + origCashStatus);
      throw new ServiceException("UW/UPDATE_ERROR,订单已处理", null);
    }
  }

  /**
   * 开启出入款订单是否允许其他账户操作配置 校验非超管账号是否原受理人
   */
  private void crossAccountCheck(UserCredential userCredential, MemberWithdraw memberWithdraw)
      throws ServiceException {
    if (userCredential != null
        && StringUtils.isNotEmpty(userCredential.getUsername())
        && null != memberWithdraw) {
      MemberRechargeLimit limitInfo =
          Optional.ofNullable(
              limitInfoService.getLimitInfo(
                  LimitEnums.MEMBER_RECHARGE_LIMIT.getName(), MemberRechargeLimit.class))
              .orElseThrow(() -> new ServiceException("加载出入款配置信息失败，请联系客服！"));
      boolean toCheck =
          (!Objects.equals(
              AllowOthersOperateEnums.YES.getValue(),
              limitInfo.getIsHandledAllowOthersOperate()))
              && !userCredential.isSuperAdmin();
      if (toCheck) {
        if (!Objects.equals(WithdrawStatus.UNHANDLED.getValue(), memberWithdraw.getCashStatus())
            && !StringUtils.equalsIgnoreCase(
            userCredential.getUsername(), memberWithdraw.getOperatorAccount())) {
          throw new ServiceException("您无权操作此订单:" + memberWithdraw.getCashOrderNo());
        }
      }
    }
  }

  private static boolean verifyPpMerchant(
      MemberWithdraw memberWithdraw, PpMerchant ppMerchant, PpInterface ppInterface) {
    ProxyPayMerBean proxyPayMerBean = ProxyPayMerBean.conver2Bean(ppMerchant.getMerLimits());
    if (memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMinLimitCash()) < 0
        || memberWithdraw.getCashMoney().compareTo(proxyPayMerBean.getMaxLimitCash()) > 0) {
      log.info("用户出款金额超出商户出款金额范围，过滤此商户，商户名称为：" + ppMerchant.getName());
      return true;
    }

    if (StringUtils.isNotEmpty(proxyPayMerBean.getUserLever())) {
      if (!StringUtils.contains(
          "," + proxyPayMerBean.getUserLever() + ",",
          String.format("%s" + memberWithdraw.getMemberLevel() + "%s", ",", ","))) {
        log.info("用户层级不在此代付商户设置的层级中，过滤此商户，商户名称为：" + ppMerchant.getName());
        return true;
      }
    }

    Map<String, String> banksMap =
        JSONObject.parseObject(
            JSONObject.parseObject(ppInterface.getLimtInfo()).getString("banks"), Map.class);
    /** 模糊匹配银行名称 */
    boolean isBankName = true;
    for (Map.Entry<String, String> entry : banksMap.entrySet()) {
      if (StringUtils.contains(entry.getValue(), memberWithdraw.getBankName())
          || StringUtils.contains(memberWithdraw.getBankName(), entry.getValue())) {
        isBankName = false;
        break;
      }

      if (StringUtils.contains(entry.getValue(), "邮政")
          && StringUtils.contains(memberWithdraw.getBankName(), "邮政")) {
        isBankName = false;
        break;
      }
    }
    if (isBankName) {
      log.info("代付商户不支持用户银行卡出款，过滤此商户，商户名称为：" + ppMerchant.getName());
      return true;
    }
    return false;
  }

  /**
   * 检查用户，封装用户信息
   */
  private void checkUserInfo(Member member, MemberInfo memberInfo, boolean checkUserState)
      throws Exception {
    // 查询用户是否存在
    if (member == null) {
      throw new ServiceException("UC/USER_NOT_EXIST, uc.user_not_exist", null);
    }
    // 查询用户的扩展信息是否为空
    if (memberInfo == null) {
      throw new ServiceException("UC/USER_NOT_EXIST, uc.user_not_exist", null);
    }
    if (checkUserState) {
      // 查询用户是否正常
      if (!UserStates.isMoneyDeal(member.getStatus())) {
        throw new ServiceException("用户已经被冻结");
      }
    }
    if (member.getUserType().equals(UserTypes.TEST.value())) {
      throw new ServiceException("用户为试玩会员");
    }
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

  /**
   * 免提直充
   *
   * @param memberWithdraw
   * @param userCredential
   * @throws Exception
   */
  public void directCharge(MemberWithdraw memberWithdraw, UserCredential userCredential)
      throws Exception {
    SysDictData sysDictData = sysDictDataService.getDictList("system_config", "direct-charge");
    Optional.ofNullable(sysDictData).orElseThrow(() -> new ServiceException("免提直充配置异常，请检查配置是否正确。"));

    DirectCharge directCharge = JSON.parseObject(sysDictData.getDictValue(), DirectCharge.class);
    String levels = directCharge.getLevels(); //层级
    int pointFlag = directCharge.getPointFlag(); //是否计算积分
    int dmlFlag = directCharge.getDmlFlag(); //是否稽查打码量
    int normalDmlMultiple = directCharge.getNormalDmlMultiple(); //常态打码量倍数
    int discountDmlMultiple = directCharge.getDiscountDmlMultiple();  //优惠打码量倍数
    BigDecimal discountPercentage = directCharge.getDiscountPercentage(); //优惠百分比
    BigDecimal discountDml = BigDecimal.ZERO; //优惠打码量
    BigDecimal discountAmount = BigDecimal.ZERO; //优惠金额
    BigDecimal approveMoney = memberWithdraw.getApproveMoney(); //充值金额
    boolean skipAuditing = directCharge.getSkipAuditing() == 1 ? true : false; //是否直接入款
    Integer discountType = 8080; //固定值
    BigDecimal normalDml = BigDecimal.ZERO; //常态打码量
    String auditRemarks =
        StringUtil.isBlank(directCharge.getAuditRemarks()) ? "免提直充，出款订单号：" + memberWithdraw
            .getCashOrderNo()
            : directCharge.getAuditRemarks(); //审核备注

    if (!StringUtil.isBlank(levels)) {
      String[] levelArr = levels.split(",");
      boolean contains = Arrays.stream(levelArr).anyMatch(memberWithdraw.getMemberLevel()::equals);
      if (contains) {
        if (pointFlag == 1) {
          discountAmount = (approveMoney.multiply(discountPercentage)).divide(new BigDecimal(100))
              .setScale(2, BigDecimal.ROUND_HALF_UP);
        }
      }
    }

    if (dmlFlag == 1) {
      normalDml = new BigDecimal(normalDmlMultiple).multiply(approveMoney)
          .setScale(2, BigDecimal.ROUND_HALF_UP);
      discountDml = discountAmount.multiply(new BigDecimal(discountDmlMultiple))
          .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    ManualRechargeOrderBo manualRechargeOrderBo = Builder.of(ManualRechargeOrderBo::new)
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
    rechargeOrderService.manual(manualRechargeOrderBo, userCredential);
    log.info("\n免提直充配置:{},\n充值信息： 会员账号：{}，入款金额：{}，优惠金额：{}，常态打码量：{},优惠打码量：{},备注：{},层级：{}",
        directCharge.toString(),
        manualRechargeOrderBo.getAccount(), manualRechargeOrderBo.getAmount(),
        manualRechargeOrderBo.getDiscountAmount(), manualRechargeOrderBo.getNormalDml(),
        manualRechargeOrderBo.getDiscountDml(), manualRechargeOrderBo.getAuditRemarks(),
        directCharge.getLevels());

    //如果是直接入款则添加成长值
    if (skipAuditing) {
      // 判断是否在充值成长值黑名单
      if (bizBlacklistFacade
          .isUserInBlacklist(memberWithdraw.getMemberId(), BizBlacklistType.RECHARGE_GROWTH)) {
        log.info("免提直充出款单号：cashOrderNo={}，会员账号：account={}不添加成长值，原因：该会员位于'充值成长值'黑名单！",
            memberWithdraw.getCashOrderNo(), memberWithdraw.getAccount());
        return;
      }

      // 充值添加成长值

    }
  }

}
