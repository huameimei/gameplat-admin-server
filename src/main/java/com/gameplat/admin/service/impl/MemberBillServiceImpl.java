package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberBillConvert;
import com.gameplat.admin.mapper.MemberBillMapper;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.model.bean.TranTypeBean;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberBillServiceImpl extends ServiceImpl<MemberBillMapper, MemberBill>
    implements MemberBillService {

  @Autowired private MemberBillConvert memberBillConvert;

  @Override
  public void save(Member member, MemberBill memberBill) {
    memberBill.setAccount(member.getAccount());
    memberBill.setMemberPath(member.getSuperPath());
    memberBill.setMemberId(member.getId());
    this.save(memberBill);
  }

  @Override
  public List<TranTypeBean> findTranTypes() {
    return TranTypes.getAllTranList();
  }

  @Override
  public IPage<MemberBillVO> queryPage(PageDTO<MemberBill> page, MemberBillDTO dto) {
    return this.builderQuery(dto).page(page).convert(memberBillConvert::toVo);
  }

  @Override
  public List<MemberBillVO> queryList(MemberBillDTO dto) {
    return this.builderQuery(dto).list().stream()
        .map(memberBillConvert::toVo)
        .collect(Collectors.toList());
  }

  @Override
  public MemberBill queryGameMemberBill(Long memberId, String orderNo, int tranType) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(memberId), MemberBill::getMemberId, memberId)
        .eq(ObjectUtils.isNotEmpty(orderNo), MemberBill::getOrderNo, orderNo)
        .eq(ObjectUtils.isNotEmpty(tranType), MemberBill::getTranType, tranType)
        .orderByDesc(MemberBill::getId)
        .one();
  }

  private LambdaQueryChainWrapper<MemberBill> builderQuery(MemberBillDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(dto.getAccount()), MemberBill::getAccount, dto.getAccount())
        .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), MemberBill::getOrderNo, dto.getOrderNo())
        .in(ObjectUtils.isNotEmpty(dto.getTranTypes()), MemberBill::getTranType, dto.getTranTypes())
        .between(
            ObjectUtils.isNotEmpty(dto.getBeginTime()),
            MemberBill::getCreateTime,
            dto.getBeginTime(),
            dto.getEndTime())
        .orderByDesc(MemberBill::getCreateTime);
  }
}
