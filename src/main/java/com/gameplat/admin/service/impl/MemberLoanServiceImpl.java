package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberLoanMapper;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.vo.LoanVO;
import com.gameplat.admin.model.vo.MemberLoanSumVO;
import com.gameplat.admin.model.vo.MemberLoanVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.util.UUIDUtils;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.model.entity.member.*;
import com.gameplat.model.entity.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lily
 * @description
 * @date 2022/3/6
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberLoanServiceImpl extends ServiceImpl<MemberLoanMapper, MemberLoan>
    implements MemberLoanService {

  @Autowired private MemberService memberService;

  @Autowired private MemberInfoService memberInfoService;

  @Lazy @Autowired private MemberGrowthLevelService memberGrowthLevelService;

  @Autowired private MemberBillService memberBillService;

  @Autowired private MessageInfoService messageInfoService;

  @Autowired private MemberLoanMapper memberLoanMapper;

  /** 分页查 */
  @Override
  public LoanVO page(PageDTO<MemberLoan> page, MemberLoanQueryDTO dto) {

    IPage<MemberLoanVO> memberLoanList = memberLoanMapper.page(page, dto);

    BigDecimal total = memberLoanMapper.getTotalSum();

    LoanVO loanVO = new LoanVO();
    loanVO.setPage(memberLoanList);
    loanVO.setMemberLoanSumVO(
        new MemberLoanSumVO() {
          {
            setTotal(total);
          }
        });

    return loanVO;
  }

  @Override
  public void recycle(String idList) {
    String username = GlobalContextHolder.getContext().getUsername();
    String[] split = idList.split(",");

    for (String memberId : split) {
      String orderNo = UUIDUtils.getUUID();
      Long id = Long.parseLong(memberId);
      Member member = memberService.getById(id);
      MemberInfo memberInfo = memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, id).one();
      MemberGrowthLevel memberGrowthLevel =
          memberGrowthLevelService.getLevel(memberInfo.getVipLevel());
      MemberLoanVO memberLoan = memberLoanMapper.getNewRecord(id);
      // 当前账户欠款余额
      BigDecimal overdraftMoney = memberLoan.getOverdraftMoney();
      //剩余欠款金额
      BigDecimal afterOverdraftMoney = new BigDecimal(0.00);
      // 当前账户余额
      BigDecimal balance = memberInfo.getBalance();
      //还款状态 0:未结清  1:已结清
      Integer loanStatus = 1;
      // 1.判断当前余额是否比欠款金额多
      if (balance.compareTo(overdraftMoney) < 0) {
        loanStatus = 0;
        //剩余未还金额
        afterOverdraftMoney = overdraftMoney.subtract(balance);
        //扣除金额
        overdraftMoney = balance;
        balance = new BigDecimal(0.00);

      }
      repay(overdraftMoney, id);

      // 2.添加member_loan表回收数据
      MemberLoan loan = new MemberLoan();
      loan.setMemberId(id)
          .setAccount(member.getAccount())
          .setUserLevel(member.getUserLevel())
          .setSuperPath(member.getSuperPath())
          .setParentId(member.getParentId())
          .setParentName(member.getParentName())
          .setVipLevel(memberInfo.getVipLevel())
          .setMemberBalance(overdraftMoney)
          .setLoanMoney(memberGrowthLevel.getLoanMoney())
          .setRepayTime(new Date())
          .setLoanStatus(loanStatus)
          .setOverdraftMoney(afterOverdraftMoney)
          .setOrderNo(orderNo)
          .setType(3)
          .setLoanTime(memberLoanMapper.getRecentLoanTime(id));
      this.save(loan);

      // 3.修改账户余额
      memberInfoService.updateBalance(memberLoan.getMemberId(), new BigDecimal(overdraftMoney.toString()).negate());


      // 4.添加流水表
      MemberBill memberBill = new MemberBill();
      memberBill.setMemberId(memberLoan.getMemberId());
      memberBill.setAccount(member.getAccount());
      memberBill.setMemberPath(member.getSuperPath());
      memberBill.setTranType(TranTypes.LOAN_RECYCLE.getValue());
      memberBill.setAmount(overdraftMoney);
      memberBill.setBalance(balance);
      memberBill.setContent(
          "管理员:" + username + " 于 " + DateUtil.now() + "回收借呗欠款 " + overdraftMoney+ " 元");
      memberBill.setRemark("回收欠款");
      memberBill.setOperator(username);
      memberBill.setOrderNo(orderNo);
      memberBillService.save(memberBill);

      // 5.添加消息表
      Message message = new Message();
      message.setTitle("借呗欠款回收");
      message.setContent("系统回收借呗欠款：" + overdraftMoney + " 元");
      message.setCategory(4);
      message.setPushRange(2);
      message.setLinkAccount(member.getAccount());
      message.setType(1);
      message.setStatus(1);
      message.setRemarks("借呗欠款回收");
      messageInfoService.save(message);
    }
  }

  /** 判断是否有欠款 */
  @Override
  public Boolean getNewRecord(Long memberId) {
    BigDecimal overdraftMoney = new BigDecimal("0.00");
    MemberLoanVO record = memberLoanMapper.getNewRecord(memberId);
    if(null != record){
      overdraftMoney = record.getOverdraftMoney();
    }
    if(overdraftMoney.compareTo(new BigDecimal("0.0000")) > 0){
      return true;
    }else{
      return false;
    }
  }

  /** 还款 */
  @Override
  public void repay(BigDecimal money, Long memberId) {

    //1.查询没有结清的数据
    List<MemberLoan> remainMoneyList =
            this.lambdaQuery()
                    .eq(MemberLoan::getMemberId, memberId)
                    .eq(MemberLoan::getLoanStatus, 0)
                    .eq(MemberLoan::getType, 2)
                    .list();

    BigDecimal sub = new BigDecimal("0.00");
    for (int i = 0; i < remainMoneyList.size(); i++) {
      Integer loanStatus = 0;
      MemberLoan memberLoan1 = remainMoneyList.get(i);
      //未还金额
      BigDecimal beforeRemainMoney = memberLoan1.getRemainMoney();
      BigDecimal afterRemainMoney = new BigDecimal("0.00");
      if(i == 0){
        sub = beforeRemainMoney.subtract(money);
        if(sub.compareTo(new BigDecimal("0.00")) > 0){
          MemberLoan entity = new MemberLoan();
          entity.setId(memberLoan1.getId());
          entity.setRemainMoney(sub);
          this.updateById(entity);
          return;
        }
      }
      if(i == 0){
        afterRemainMoney = sub;
      }else{
        afterRemainMoney = beforeRemainMoney.add(sub);
        sub = afterRemainMoney;
      }

      if(afterRemainMoney.compareTo(new BigDecimal("0.0000")) == 0){
        loanStatus = 1;
        MemberLoan entity = new MemberLoan();
        entity.setId(memberLoan1.getId());
        entity.setLoanStatus(loanStatus);
        entity.setRemainMoney(afterRemainMoney);
        this.updateById(entity);
        return;
      }
      if(afterRemainMoney.compareTo(new BigDecimal("0.0000")) < 0){
        loanStatus = 1;
      }

      MemberLoan entity = new MemberLoan();
      entity.setId(memberLoan1.getId());
      entity.setLoanStatus(loanStatus);
      entity.setRemainMoney(afterRemainMoney);
      this.updateById(entity);
    }
  }
}
