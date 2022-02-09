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
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.lang.Assert;
import com.google.common.collect.Lists;
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
  @Cached(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberId", expire = 7200)
  public List<MemberRemarkVO> getByMemberId(Long memberId) {
    return this.lambdaQuery()
        .eq(MemberRemark::getDeleteFlag, BooleanEnum.NO.value())
        .eq(MemberRemark::getMemberId, memberId)
        .orderByDesc(MemberRemark::getCreateTime)
        .list()
        .stream()
        .map(memberRemarkConvert::toVo)
        .collect(Collectors.toList());
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberId")
  public void update(Long memberId, String remark) {
    this.batchUpdateRemark(Lists.newArrayList(memberId), remark);
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#dto.ids", multi = true)
  public void batchAdd(MemberRemarkAddDTO dto) {
    this.batchUpdateRemark(dto.getIds(), dto.getRemark());
  }

  @Override
  public void deleteById(Long id) {
    MemberRemark memberRemark = Assert.notNull(this.getById(id), "备注不存在!");
    if (!this.lambdaUpdate()
        .eq(MemberRemark::getId, id)
        .set(MemberRemark::getDeleteFlag, BooleanEnum.YES.value())
        .update(new MemberRemark())) {
      throw new ServiceException("删除备注历史失败");
    }

    memberService.updateRemark(memberRemark.getMemberId(), null);
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberId")
  public void cleanByMemberId(Long memberId) {
    if (!this.lambdaUpdate()
        .eq(MemberRemark::getMemberId, memberId)
        .set(MemberRemark::getDeleteFlag, BooleanEnum.YES.value())
        .update(new MemberRemark())) {
      throw new ServiceException("清空备注历史失败");
    }

    memberService.updateRemark(memberId, null);
  }

  private void batchUpdateRemark(List<Long> memberIds, String remark) {
    // 修改当前会员备注
    memberService.updateRemark(memberIds, remark);

    // 批量添加备注
    List<MemberRemark> remarks =
        memberService.listByIds(memberIds).stream()
            .map(member -> this.builderRemarkForAdd(member, remark))
            .collect(Collectors.toList());

    Assert.isTrue(this.saveBatch(remarks), "批量添加备注失败!");
  }

  private MemberRemark builderRemarkForAdd(Member member, String remark) {
    return MemberRemark.builder()
        .memberId(member.getId())
        .account(member.getAccount())
        .content(remark)
        .build();
  }
}
