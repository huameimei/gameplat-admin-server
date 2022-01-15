package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.component.MemberQueryCondition;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.convert.MemberConvert;
import com.gameplat.admin.enums.MemberEnums;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberRemarkService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.PasswordService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberConvert memberConvert;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberQueryCondition memberQueryCondition;

  @Autowired private PasswordService passwordService;

  @Autowired private MemberRemarkService memberRemarkService;

  @Override
  public IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto) {
    return memberMapper.queryPage(page, memberQueryCondition.builderQueryWrapper(dto));
  }

  @Override
  public List<MemberVO> queryList(MemberQueryDTO dto) {
    return memberMapper.queryList(memberQueryCondition.builderQueryWrapper(dto));
  }

  @Override
  public MemberInfoVO getInfo(Long id) {
    return memberMapper.getMemberInfo(id);
  }

  @Override
  public MemberInfoVO getMemberInfo(String account) {
    return memberMapper.getMemberInfoByAccount(account);
  }

  @Override
  public void add(MemberAddDTO dto) {
    Member member = memberConvert.toEntity(dto);
    member.setRegisterSource(MemberEnums.RegisterSource.BACKEND.value());
    member.setPassword(passwordService.encrypt(member.getPassword(), dto.getAccount()));

    // 设置上级
    this.setMemberParent(member);

    // 保存会员和会员详情
    Assert.isTrue(this.save(member), () -> new ServiceException("新增会员失败!"));
    Assert.isTrue(
        memberInfoService.save(
            MemberInfo.builder().memberId(member.getId()).rebate(dto.getRebate()).build()),
        () -> new ServiceException("新增会员失败!"));
  }

  @Override
  public void update(MemberEditDTO dto) {
    MemberInfo memberInfo =
        MemberInfo.builder().memberId(dto.getId()).rebate(dto.getRebate()).build();

    // 更新会员信息和会员详情
    if (!this.updateById(memberConvert.toEntity(dto))
        || !memberInfoService.updateById(memberInfo)) {
      throw new ServiceException("修改会员信息失败!");
    }
  }

  @Override
  public void enable(List<Long> ids) {
    this.changeStatus(ids, MemberEnums.Status.ENABlED.value());
  }

  @Override
  public void disable(List<Long> ids) {
    this.changeStatus(ids, MemberEnums.Status.DISABLED.value());
  }

  @Override
  public void clearContact(MemberContactCleanDTO dto) {
    this.update()
        .func(query -> dto.getFields().forEach(field -> query.set(field, null)))
        .between("create_time", dto.getStartTime(), dto.getEndTime())
        .update(new Member());
  }

  @Override
  public Member getById(Long id) {
    return this.lambdaQuery()
        .eq(Member::getId, id)
        .oneOpt()
        .orElseThrow(() -> new ServiceException("会员信息不存在!"));
  }

  @Override
  public Optional<Member> getByAccount(String account) {
    return this.lambdaQuery().eq(Member::getAccount, account).oneOpt();
  }

  @Override
  public Optional<Member> getAgentByAccount(String account) {
    return this.lambdaQuery()
        .eq(Member::getAccount, account)
        .eq(Member::getUserType, MemberEnums.Type.AGENT.value())
        .oneOpt();
  }

  @Override
  public List<Member> getByParentName(String parentName) {
    return this.lambdaQuery().eq(Member::getParentName, parentName).list();
  }

  @Override
  public List<String> findAccountByUserLevelIn(List<String> levelsLists) {
    return this.lambdaQuery()
        .select(Member::getAccount)
        .in(Member::getUserLevel, levelsLists)
        .list()
        .stream()
        .map(Member::getAccount)
        .collect(Collectors.toList());
  }

  @Override
  public void updateContact(MemberContactUpdateDTO dto) {
    if (!this.updateById(memberConvert.toEntity(dto))) {
      throw new ServiceException("更新会员联系方式失败!");
    }
  }

  @Override
  public void resetPassword(MemberPwdUpdateDTO dto) {
    Member member = this.getById(dto.getId());
    String password = passwordService.encrypt(dto.getPassword(), member.getAccount());
    if (!this.lambdaUpdate()
        .set(Member::getPassword, password)
        .eq(Member::getId, member.getId())
        .update(new Member())) {
      throw new ServiceException("重置会员密码失败!");
    }

    // 更新会员备注
    memberRemarkService.update(dto.getId(), dto.getRemark());
  }

  @Override
  public void resetWithdrawPassword(MemberWithdrawPwdUpdateDTO dto) {
    Member member = this.getById(dto.getId());
    String password = passwordService.encrypt(dto.getPassword(), member.getAccount());
    Assert.isTrue(
        memberInfoService
            .lambdaUpdate()
            .set(MemberInfo::getCashPassword, password)
            .eq(MemberInfo::getMemberId, member.getId())
            .update(new MemberInfo()),
        () -> new ServiceException("重置会员提现密码失败!"));
  }

  @Override
  public void changeWithdrawFlag(Long id, String flag) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getWithdrawFlag, flag)
            .eq(Member::getId, id)
            .update(new Member()),
        () -> new ServiceException("修改会员提现状态失败!"));
  }

  @Override
  public void resetRealName(MemberResetRealNameDTO dto) {
    Member member = this.getById(dto.getId());
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getRealName, dto.getRealName())
            .eq(Member::getId, member.getId())
            .update(new Member()),
        () -> new ServiceException("重置会员真实姓名失败!"));
  }

  @Override
  public List<Member> getListByUserLevel(List<String> userLevelList) {
    return Optional.ofNullable(userLevelList)
        .filter(CollectionUtil::isNotEmpty)
        .map(e -> this.lambdaQuery().in(Member::getUserLevel, e).list())
        .orElse(null);
  }

  @Override
  public List<Member> getListByAgentAccount(String agentAccout) {
    return memberMapper.getListByAgentAccout(agentAccout);
  }

  @Override
  public void updateRealName(Long memberId, String realName) {
    Assert.isTrue(
        this.lambdaUpdate().set(Member::getRealName, realName).eq(Member::getId, memberId).update(),
        () -> new ServiceException("修改会员真实姓名失败!"));
  }

  /**
   * 设置会员上级
   *
   * @param member Member
   */
  private void setMemberParent(Member member) {
    String parentName =
        StringUtils.getIfEmpty(
            member.getParentName(), () -> this.getMemberRoot(member.getUserType()));

    Member parent =
        this.getByAccount(parentName).orElseThrow(() -> new ServiceException("代理账号不存在!"));

    member.setParentId(parent.getId());
    member.setParentName(parent.getAccount());
    member.setSuperPath(parent.getSuperPath().concat(member.getAccount()).concat("/"));
    if (MemberEnums.Type.AGENT.match(member.getUserType())) {
      member.setAgentLevel(parent.getAgentLevel() + 1);
    }

    // 更新下级人数
    memberMapper.updateLowerNumByAccount(parent.getAccount(), 1);
  }

  /**
   * 根据会员类型获取根代理
   *
   * @param userType String
   * @return String
   */
  private String getMemberRoot(String userType) {
    return MemberEnums.Type.TEST.match(userType)
        ? SystemConstant.DEFAULT_TEST_ROOT
        : SystemConstant.DEFAULT_WEB_ROOT;
  }

  /**
   * 批量修改会员状态
   *
   * @param ids List
   * @param status int
   */
  private void changeStatus(List<Long> ids, int status) {
    Assert.notEmpty(ids, () -> new ServiceException("会员ID不能为空"));

    if (!this.lambdaUpdate()
        .set(Member::getStatus, status)
        .in(Member::getId, ids)
        .update(new Member())) {
      throw new ServiceException("批量启用失败");
    }
  }
}
