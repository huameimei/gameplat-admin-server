package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberBillMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.service.MemberBillService;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberBillServiceImpl extends ServiceImpl<MemberBillMapper, MemberBill> implements
    MemberBillService {

  @Override
  public void save(Member member, MemberBill memberBill) throws Exception {
    memberBill.setAccount(member.getAccount());
    memberBill.setMemberPaths(member.getSuperPath());
    memberBill.setTableIndex(member.getTableIndex());
    memberBill.setMemberId(member.getId());
    this.save(memberBill);
  }

}
