package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberRemarkConvert;
import com.gameplat.admin.mapper.MemberRemarkMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberRemark;
import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.admin.service.MemberRemarkService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberRemarkServiceImpl extends ServiceImpl<MemberRemarkMapper, MemberRemark>
    implements MemberRemarkService {

  @Autowired private MemberService memberService;

  @Autowired private MemberRemarkConvert memberRemarkConvert;

  @Override
  public void update(MemberRemarkAddDTO dto) {
    if (!memberService
        .lambdaUpdate()
        .in(Member::getId, dto.getIds())
        .set(Member::getRemark, dto.getRemark())
        .update(new Member())) {
      throw new ServiceException("批量修改会员备注失败!");
    }

    // 批量添加备注
    List<MemberRemark> remarks =
        memberService.listByIds(dto.getIds()).stream()
            .map(member -> this.builderRemarkForAdd(member, dto.getRemark()))
            .collect(Collectors.toList());

    if (!this.saveBatch(remarks)) {
      throw new ServiceException("批量添加备注失败!");
    }
  }

  @Override
  public List<MemberRemarkVO> getByMemberId(Long memberId) {
    return this.lambdaQuery()
        .eq(MemberRemark::getMemberId, memberId)
        .orderByDesc(MemberRemark::getCreateTime)
        .list()
        .stream()
        .map(memberRemarkConvert::toVo)
        .collect(Collectors.toList());
  }

  private MemberRemark builderRemarkForAdd(Member member, String remark) {
    return MemberRemark.builder()
        .memberId(member.getId())
        .account(member.getAccount())
        .content(remark)
        .build();
  }
}
