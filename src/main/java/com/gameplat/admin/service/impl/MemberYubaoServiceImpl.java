package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberYubaoInterestMapper;
import com.gameplat.admin.mapper.MemberYubaoMapper;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberYubaoService;
import com.gameplat.admin.util.MoneyUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.model.bean.limit.YubaoLimit;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.member.MemberYubao;
import com.gameplat.model.entity.member.MemberYubaoInterest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberYubaoServiceImpl extends ServiceImpl<MemberYubaoMapper, MemberYubao>
        implements MemberYubaoService {

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberYubaoInterestMapper memberYubaoInterestMapper;

  @Autowired private MemberBillService memberBillService;

  @Override
  public void recycle(String account, Long memberId,Double money) {
    if (memberId == null || memberId.longValue() <= 0) {
      throw new ServiceException("会员id不正确");
    }
    QueryWrapper<MemberInfo> queryMemberInfo=new QueryWrapper<>();
    queryMemberInfo.eq("member_id",memberId);
    MemberInfo extInfo = memberInfoService.getOne(queryMemberInfo);
    if (extInfo == null) {
      throw new ServiceException("会员不存在");
    }
    if (money == null || money <= 0) {
      throw new ServiceException("金额不正确");
    }
    LambdaQueryWrapper<MemberYubao> query = Wrappers.lambdaQuery();
    query.eq(MemberYubao::getAccount,account);

    List<MemberYubao> yuBaos = this.list(query);
    if(CollectionUtils.isEmpty(yuBaos)){
      throw new ServiceException("无记录");
    }
    // 四舍五入，保留4位小数
    double totalMoney = MoneyUtils.fix(4, yuBaos.stream().map(MemberYubao::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add)).doubleValue();
    // 建议使用这种方式比较
    if (BigDecimal.valueOf(totalMoney).compareTo(BigDecimal.valueOf(money)) < 0) {
      throw new ServiceException("余额宝内金额不足,转出失败");
    }
    List<MemberYubao> transferOutList = new ArrayList<>();
    double transferMoney = money;
    for (MemberYubao yuBao: yuBaos) {
      double yubaoTransfer = Math.min(yuBao.getMoney().doubleValue(), transferMoney);
      yuBao.setMoney(yuBao.getMoney().subtract(new BigDecimal(yubaoTransfer)));
      transferMoney = transferMoney - yubaoTransfer;
      transferOutList.add(yuBao);
      if (transferMoney == 0) {
        break;
      }
    }
    this.baseMapper.updateYubaoList(transferOutList);
    this.baseMapper.updateYubaoMoney(memberId, -money, extInfo.getBalance());

    String yubaoBalance = MoneyUtils.toYuanStr(extInfo.getYubaoAmount().subtract(BigDecimal.valueOf(money)));
    String content = "转出余额宝，转出前余额宝余额：" + MoneyUtils.toYuanStr(extInfo.getYubaoAmount()) + "元，转出后余额宝余额：" + yubaoBalance + "元";

    // 添加流水记录
    MemberBill memberBill = new MemberBill();
    memberBill.setMemberId(memberId);
    memberBill.setAccount(account);
    memberBill.setMemberPath("");
    memberBill.setTranType(TranTypes.YUBAO_IN.getValue());
    memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
    memberBill.setAmount(new BigDecimal(money));
    memberBill.setBalance(extInfo.getBalance());
    memberBill.setOperator(account);
    memberBill.setRemark(content);
    memberBill.setContent(content);
    memberBillService.save(memberBill);
  }

  @Override
  public void settle() {
    long startTime = System.currentTimeMillis();

    YubaoLimit yubaoLimit=limitInfoService.getLimitInfo(LimitEnums.YUBAO_LIMIT, YubaoLimit.class).orElseThrow(() -> new ServiceException("余额宝配置信息不存在!"));

    boolean isOpen = Objects.equals(yubaoLimit.getOpenSwitch(),1);
    if (!isOpen) {
      log.info("yubao is closed.");
      return;
    }
    boolean incomeSwitch = Objects.equals(yubaoLimit.getIncomeSwitch(),1);
    if (!incomeSwitch) {
      log.info("余额宝计算收益是关闭的");
      return;
    }
    log.info("start settle yubao interest.");
    String baseRate =yubaoLimit.getBaseRateOfReturn().toString();
    String activeRate = yubaoLimit.getActiveRateOfReturn().toString();
    String activeDml = yubaoLimit.getActiveDml().toString();

    String settleDate = DateUtil.dateToYMD(new Date(System.currentTimeMillis() - 24 * 3600 * 1000));
    List<MemberYubao> yuBaoList = null;
    long index = 1;
    int count=1000;
    do {
      PageDTO<MemberYubao> page = new PageDTO<>();
      page.setCurrent(index++);
      page.setSize(count);

      QueryWrapper<MemberYubao> queryWrapper = new QueryWrapper<>();
      queryWrapper.select("id,member_id as memberId,account,real_name,super_path,IFNULL(sum(money),0) as money")
              .lt("transfer_in_time", DateUtil.getDateEnd(new Date()))
              .groupBy("member_id")
              .orderByAsc("member_id");
      yuBaoList = this.page(page, queryWrapper).getRecords();
      for (MemberYubao yuBao : yuBaoList) {
        try {
          addYubaoInterest(yuBao, settleDate, baseRate, activeRate, activeDml);
        } catch (Exception e) {
          log.error("addYubaoInterest error:", e);
        }
      }
    } while (yuBaoList!=null && yuBaoList.size() > 0);
//    userBusDayReportService.save(BusReportType.YUBAO.getValue(), settleDate);
    log.info("finish settle yubao interest. cost={}", System.currentTimeMillis() - startTime);
  }

  @Override
  public IPage<MemberYubaoInterest> queryYubaoInterest(String account, String startDate, String endDate, PageDTO<MemberYubaoInterest> page) {
    LambdaQueryWrapper<MemberYubaoInterest> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotEmpty(account)) {
      query.eq(MemberYubaoInterest::getAccount, account);
    }
    if(StringUtils.isNotEmpty(startDate)){
      query.ge(MemberYubaoInterest::getDate,DateUtil.strToDate(startDate));
    }
    if(StringUtils.isNotEmpty(endDate)){
      query.le(MemberYubaoInterest::getDate,DateUtil.strToDate(endDate));
    }
    query.orderByDesc(MemberYubaoInterest::getId);
    return memberYubaoInterestMapper.selectPage(page, query);
  }

  /**
   *  添加余额宝收益
   */
  private void addYubaoInterest(MemberYubao yuBao, String settleDate, String baseRate, String activeRate, String activeDml)  {
//    if (yuBaoDao.isSettleInterest(yuBao.getMemberId(), settleDate)) {
//      return;
//    }
    MemberInfo extInfo = memberInfoService.getById(yuBao.getMemberId());
    if (extInfo == null) {
      return;
    }
//    if (bizBlacklistFacade.isUserInBlacklist(yuBao.getUserId(), BlacklistConstant.BizBlacklistType.YU_BAO_INTEREST)) {
//      log.info("黑名单用户不发放余额宝收益,account={},userId={}", extInfo.getAccount(), extInfo.getUserId());
//      return;
//    }
    Date date = DateUtil.getParseDate(settleDate);


    double interestRate = Double.parseDouble(baseRate);
//    if (busDayReport != null) {
//      double totalBetMoney = busDayReport.getCpBetMoney() + busDayReport.getLiveBetMoney() + busDayReport.getSpBetMoney();
//      if (totalBetMoney >= activeDml) {
//        interestRate += activeRate;
//      }
//    }
    interestRate = interestRate * extInfo.getYubaoDegrade() / 100; // 降级

    interestRate = floor(interestRate, 4);
    double interestMoney = floor(yuBao.getMoney().doubleValue() * interestRate / 36500, 4);
    if (interestMoney < 0.01) {
      log.info("ignore interestMoney < 0.01,account={}", yuBao.getAccount());
      return;
    }
    // 添加收益金额
    memberYubaoInterestMapper.addYubaoInterest(yuBao.getId(),yuBao.getMemberId(), interestMoney);

    // 添加收益记录
    MemberYubaoInterest yuBaoInterest = new MemberYubaoInterest();
    yuBaoInterest.setDate(date);
    yuBaoInterest.setMemberId(yuBao.getMemberId());
    yuBaoInterest.setAccount(yuBao.getAccount());
    yuBaoInterest.setRealName(yuBao.getRealName());
    yuBaoInterest.setSuperPath(yuBao.getSuperPath());
    yuBaoInterest.setYubaoMoney(yuBao.getMoney());
    yuBaoInterest.setInterestMoney(new BigDecimal(interestMoney));
    yuBaoInterest.setInterestRate(new BigDecimal(interestRate));
    yuBaoInterest.setSettleTime(new Date());
    memberYubaoInterestMapper.insert(yuBaoInterest);
  }

  private static double floor(double val, int len) {
    int factory = 1;
    for (int i = 0; i < len; i++) {
      factory *= 10;
    }
    return Math.floor(val * factory) / factory;
  }
}