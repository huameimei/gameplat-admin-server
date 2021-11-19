package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.commpent.MemberQueryCondition;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.convert.MemberConvert;
import com.gameplat.admin.enums.MemberEnums;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.domain.PushMessage;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.common.exception.ServiceException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberConvert memberConvert;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberQueryCondition memberQueryCondition;

  @Override
  public IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto) {
    return memberMapper.queryPage(page, memberQueryCondition.builderQueryWrapper(dto));
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
    if (MemberEnums.Type.TEST.match(dto.getUserType())) {
      // 测试账号挂在testRoot下
      Member testRoot =
          this.getByAccount(SystemConstant.DEFAULT_TEST_ROOT)
              .orElseThrow(() -> new ServiceException("根代理账号不存在!"));

      member.setParentId(testRoot.getId());
      member.setParentName(testRoot.getAccount());
      member.setSuperPath(testRoot.getSuperPath().concat(dto.getAccount()).concat("/"));

      // 更新下级人数
      memberMapper.updateLowerNumByAccount(testRoot.getAccount(), 1);
    } else {
      // 推广账号和普通会员挂在webRoot下
      Member webRoot =
          this.getByAccount(SystemConstant.DEFAULT_WEB_ROOT)
              .orElseThrow(() -> new ServiceException("根代理账号不存在!"));

      if (MemberEnums.Type.AGENT.match(dto.getUserType())) {
        // 设置代理账号代理等级
        member.setAgentLevel(webRoot.getAgentLevel() + 1);
      }

      member.setParentId(webRoot.getId());
      member.setParentName(webRoot.getAccount());
      member.setSuperPath(webRoot.getSuperPath().concat(dto.getAccount()).concat("/"));

      // 更新下级人数
      memberMapper.updateLowerNumByAccount(webRoot.getAccount(), 1);
    }

    // 保存会员和会员详情
    if (this.save(member)) {
      MemberInfo memberInfo =
          MemberInfo.builder().memberId(member.getId()).rebate(dto.getRebate()).build();
      if (!memberInfoService.save(memberInfo)) {
        throw new ServiceException("新增会员失败!");
      }
    }
  }

  @Override
  public void update(MemberEditDTO dto) {
    if (this.updateById(memberConvert.toEntity(dto))) {
      MemberInfo memberInfo =
          MemberInfo.builder().memberId(dto.getId()).rebate(dto.getRebate()).build();
      if (!memberInfoService.updateById(memberInfo)) {
        throw new ServiceException("修改会员信息失败!");
      }
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

  /**
   * 根据账号获取会员信息
   *
   * @param account String
   * @return Member
   */
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
    return this.lambdaQuery().select(Member::getAccount)
        .in(Member::getUserLevel,levelsLists)
        .list().stream().map(Member::getAccount).collect(Collectors.toList());
  }

  @Override
  public void updateContact(MemberContactUpdateDTO dto) {
    if (!this.updateById(memberConvert.toEntity(dto))) {
      throw new ServiceException("更新会员联系方式失败!");
    }
  }

    @Override
    public List<Member> getListByUserLevel(List<String> userLevelList) {
        if (CollectionUtil.isEmpty(userLevelList)) {
            return null;
        }
        LambdaQueryChainWrapper<Member> queryWrapper = this.lambdaQuery();
        queryWrapper.in(Member::getUserLevel, userLevelList);
        return queryWrapper.list();
    }

    @Override
    public List<Member> getListByAgentAccout(String agentAccout) {
        return memberMapper.getListByAgentAccout(agentAccout);
    }

    @Override
    public List<Member> findListByAccountList(List<String> accountList) {
        return memberMapper.findListByAccountList(accountList);
    }

    /**
     * 批量修改会员状态
     *
     * @param ids    List
     * @param status int
     */
    private void changeStatus(List<Long> ids, int status) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("会员ID不能为空");
        }

        if (!this.lambdaUpdate()
                .set(Member::getStatus, status)
                .in(Member::getId, ids)
                .update(new Member())) {
            throw new ServiceException("批量启用失败");
        }
    }
}
