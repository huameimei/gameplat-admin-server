package com.gameplat.admin.service.impl;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberRemarkConvert;
import com.gameplat.admin.mapper.MemberRemarkMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberRemark;
import com.gameplat.admin.model.dto.MemberRemarkAddDTO;
import com.gameplat.admin.model.vo.MemberRemarkVO;
import com.gameplat.admin.service.MemberRemarkService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.constant.CachedKeys;
import com.gameplat.base.common.exception.ServiceException;
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
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#dto.ids", multi = true)
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
  @Cached(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberId", expire = 7200)
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
