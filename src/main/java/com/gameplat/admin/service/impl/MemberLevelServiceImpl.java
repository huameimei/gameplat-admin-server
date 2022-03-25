package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberLevelConvert;
import com.gameplat.admin.enums.MemberLevelEnums;
import com.gameplat.admin.mapper.MemberLevelMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.service.MemberLevelService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.DefaultEnums;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.member.MemberLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel>
    implements MemberLevelService {

  @Autowired private MemberLevelConvert memberLevelConvert;

  @Autowired private MemberService memberService;

  @Autowired private MemberLevelMapper memberLevelMapper;

  @Override
  public List<MemberLevelVO> getList() {
    List<MemberLevelVO> memberLevelVOList =
        this.list().stream().map(memberLevelConvert::toVo).collect(Collectors.toList());
    // 如果有层级值配置
    if (CollectionUtil.isNotEmpty(memberLevelVOList)) {
      // 查询会员表所有层级下的会员数量和锁定会员数量
      List<MemberLevelVO> userLevelAccountNumList = memberService.getUserLevelAccountNum();
      Map<Integer, List<MemberLevelVO>> map =
          userLevelAccountNumList.stream()
              .collect(Collectors.groupingBy(MemberLevelVO::getLevelValue));
      // 匹配对应的层级数据并赋值
      memberLevelVOList.forEach(
          vo -> {
            if (map.get(vo.getLevelValue()) != null) {
              vo.setMemberNum(map.get(vo.getLevelValue()).get(0).getMemberNum());
              vo.setMemberLockNum(map.get(vo.getLevelValue()).get(0).getMemberLockNum());
            } else {
              vo.setMemberNum(0);
              vo.setMemberLockNum(0);
            }
          });
    }

    return memberLevelVOList;
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void add(MemberLevelAddDTO dto) {
    // 检查层级名称和值是否存在
    if (this.lambdaQuery().eq(MemberLevel::getLevelName, dto.getLevelName()).count() > 0) {
      throw new ServiceException("层级名称\"" + dto.getLevelName() + "\"已存在！");
    }

    if (this.lambdaQuery().eq(MemberLevel::getLevelValue, dto.getLevelValue()).count() > 0) {
      throw new ServiceException("层级值\"" + dto.getLevelValue() + "\"已存在！");
    }

    MemberLevel memberLevel = memberLevelConvert.toEntity(dto);
    memberLevel.setIsDefault(MemberLevelEnums.Default.Y.value());
    memberLevel.setLocked(MemberLevelEnums.Locked.N.value());

    memberLevel.setIsWithdraw(MemberLevelEnums.Withdraw.Y.value());
    memberLevel.setStatus(MemberLevelEnums.Status.Y.value());

    this.save(memberLevel);
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void update(MemberLevelEditDTO dto) {
    this.updateById(memberLevelConvert.toEntity(dto));
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void delete(Long id) {
    MemberLevel level =
        Optional.ofNullable(this.getById(id)).orElseThrow(() -> new ServiceException("层级不存在!"));

    if (MemberLevelEnums.Locked.isLocked(level.getLocked())) {
      throw new ServiceException("层级已被锁定，无法删除!");
    }

    Integer totalAccountNum = memberService.getUserLevelTotalAccountNum(level.getLevelValue());
    if (totalAccountNum > 0) {
      throw new ServiceException("层级内存在会员，无法删除!");
    }

    this.removeById(id);
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void lock(Long id) {
    this.lockOrUnlockMember(id, DefaultEnums.YES);
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getLocked, MemberLevelEnums.Locked.Y.value())
        .update();
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void unlock(Long id) {
    this.lockOrUnlockMember(id, DefaultEnums.NO);
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getLocked, MemberLevelEnums.Locked.N.value())
        .update();
  }

  void lockOrUnlockMember(Long levelId, DefaultEnums status) {
    // 锁定/解锁层级时，将该层级下所有会员的层级锁定状态改为yes/no
    MemberLevel byId = getById(levelId);
    List<String> userLevels = new ArrayList<>();
    userLevels.add(String.valueOf(byId.getLevelValue()));
    List<String> accountByUserLevelIn = memberService.findAccountByUserLevelIn(userLevels);
    if (CollectionUtil.isNotEmpty(accountByUserLevelIn)) {
      memberService
          .lambdaUpdate()
          .set(Member::getLevelLockFlag, status.value())
          .in(Member::getAccount, accountByUserLevelIn)
          .update();
    }
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void enable(Long id) {
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getStatus, MemberLevelEnums.Status.Y.value())
        .update();
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void disable(Long id) {
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getStatus, MemberLevelEnums.Status.N.value())
        .update();
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void enableWithdraw(Long id) {
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getIsWithdraw, MemberLevelEnums.Withdraw.Y.value())
        .update();
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void disableWithdraw(Long id) {
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getIsWithdraw, MemberLevelEnums.Withdraw.N.value())
        .update();
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void batchAllocate(List<MemberLevelAllocateDTO> dtos) {
    List<MemberLevel> memberLevels = this.getEnabledLevels();
    dtos.forEach(dto -> this.allocateLevel(memberLevels, dto));
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void allocateByUserNames(MemberLevelAllocateByUserNameDTO dto) {
    String[] userNameArray = dto.getUserNames().split(",");
    List<String> accountList = Arrays.asList(userNameArray);

    allocateAccountIsExist(accountList);
    List<Member> memberList = memberService.getListByAccountList(accountList);
    // 过滤层级无变化的会员
    memberList.removeIf(member -> member.getUserLevel().equals(dto.getLevelValue()));

    if (CollectionUtil.isNotEmpty(memberList)) {
      memberList.forEach(
          member -> {
            member.setUserLevel(dto.getLevelValue());
            // 手动调整的会员默认是未锁定状态
            member.setLevelLockFlag(MemberLevelEnums.Locked.N.name());
          });

      // 批量更新会员层级
      memberService.updateBatchById(memberList);
    }
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void allocateByFile(Integer levelValue, List<MemberLevelFileDTO> list) {
    if (CollectionUtil.isEmpty(list)) {
      throw new ServiceException("上传文件不能为空！");
    }

    List<String> accountList =
        list.stream().map(MemberLevelFileDTO::getAccount).collect(Collectors.toList());
    allocateAccountIsExist(accountList);

    List<Member> memberList = memberService.getListByAccountList(accountList);
    if (CollectionUtil.isNotEmpty(memberList)) {
      // 过滤层级无变化的会员
      memberList.removeIf(member -> member.getUserLevel().equals(levelValue));
      memberList.forEach(
          member -> {
            member.setUserLevel(levelValue);
            // 手动调整的会员默认是未锁定状态
            member.setLevelLockFlag(MemberLevelEnums.Locked.N.name());
          });

      // 批量更新会员层级
      memberService.updateBatchById(memberList);
    }
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void allocateByCondition(MemberLevelAllocateByConditionDTO dto) {
    // 代理账号集合
    List<String> parentNameList = Arrays.asList(dto.getParentNames().split(","));
    allocateAccountIsExist(parentNameList);
    // 筛选层级集合
    List<String> levelList = null;
    if (StringUtils.isNotEmpty(dto.getLevelValues())) {
      levelList = Arrays.asList(dto.getLevelValues().split(","));
    }

    // 输入的代理账号下所有的下级会员
    List<Member> allMemberList = Collections.synchronizedList(new ArrayList<>());
    List<String> finalLevelList = levelList;
    parentNameList.parallelStream()
        .forEach(
            parentName -> {
              MemberQueryDTO queryDTO = new MemberQueryDTO();
              queryDTO.setParentName(parentName);
              queryDTO.setLevelList(finalLevelList);
              queryDTO.setRechTimesFrom(dto.getMinRechargeNum());
              queryDTO.setRechTimesTo(dto.getMaxRechargeNum());
              queryDTO.setRechAmountFrom(dto.getMinRechargeAmount());
              queryDTO.setRechAmountTo(dto.getMaxRechargeAmount());
              queryDTO.setLastRechTimeFrom(dto.getLastRechargeTime());
              queryDTO.setUserType(dto.getUserType());
              queryDTO.setSubordinateOnly(dto.getSubordinateOnly());
              queryDTO.setItself(dto.getItself());
              List<Member> memberList = memberService.getMemberListByAgentAccount(queryDTO);
              if (CollectionUtil.isNotEmpty(memberList)) {
                allMemberList.addAll(memberList);
              }
            });

    if (CollectionUtil.isNotEmpty(allMemberList)) {
      allMemberList.removeIf(member -> member.getUserLevel().equals(dto.getLevelValue()));
      allMemberList.forEach(
          member -> {
            member.setUserLevel(dto.getLevelValue());
            // 手动调整的会员默认是未锁定状态
            member.setLevelLockFlag(MemberLevelEnums.Locked.N.name());
          });

      // 批量更新会员层级
      memberService.updateBatchById(allMemberList);
    }
  }

  /**
   * 分配层级
   *
   * @param levelList List
   * @param dto MemberLevelAllocateDTO
   */
  private void allocateLevel(List<MemberLevel> levelList, MemberLevelAllocateDTO dto) {
    if (CollectionUtil.isEmpty(levelList)) {
      log.info("层级{}没有可用于分配的层级", dto.getLevelName());
      return;
    }

    MemberLevel level = this.getById(dto.getId());
    if (null == level) {
      log.info("层级{}不存在", dto.getLevelName());
      return;
    }

    // 检查层级是否被锁定了
    if (MemberLevelEnums.Locked.isLocked(level.getLocked())) {
      log.info("层级{}已被锁定!", dto.getLevelName());
      return;
    }

    // 根据层级获取会员详细信息
    Integer levelValue = level.getLevelValue();
    List<MemberInfo> memberInfos = memberLevelMapper.getMemberInfoByLevel(levelValue);
    if (CollectionUtil.isEmpty(memberInfos)) {
      log.info("层级{}下没有会员!", dto.getLevelName());
      return;
    }

    // 需要修改层级的会员
    List<Member> updateMembers = this.builderMembersForUpdate(levelList, memberInfos);
    if (CollectionUtil.isEmpty(updateMembers)) {
      log.info("层级{}，没有满足分配条件的会员", dto.getLevelName());
      return;
    }

    // 过滤层级无变化的会员
    updateMembers.removeIf(member -> member.getUserLevel().equals(levelValue));

    // 如果要转入的层级是锁定状态, 则不允许转入
    updateMembers =
        updateMembers.stream()
            .filter(
                m ->
                    !MemberLevelEnums.Locked.isLocked(
                        levelList.stream()
                            .filter(l -> l.getLevelValue().equals(m.getUserLevel()))
                            .findAny()
                            .get()
                            .getLocked()))
            .collect(Collectors.toList());
    log.info("层级{}，共{}条需要重新分配层级的会员", dto.getLevelName(), updateMembers.size());

    // 批量更新会员层级
    memberService.updateBatchById(updateMembers);
  }

  private List<Member> builderMembersForUpdate(
      List<MemberLevel> levels, List<MemberInfo> memberInfos) {
    return memberInfos.stream()
        .filter(Objects::nonNull)
        .map(
            memberInfo ->
                Member.builder()
                    .id(memberInfo.getMemberId())
                    .userLevel(this.getMatchedLevel(levels, memberInfo))
                    .build())
        .collect(Collectors.toList());
  }

  private List<MemberLevel> getEnabledLevels() {
    return this.lambdaQuery().eq(MemberLevel::getStatus, MemberLevelEnums.Status.Y.value()).list();
  }

  private Integer getMatchedLevel(List<MemberLevel> levelList, MemberInfo memberInfo) {
    // 先根据充值金额倒序排列，再根据层级值倒序排列，如果有两条层级配置的充值金额和充值次数一样，会员将被分配到层级值较大的那一层
    return levelList.stream()
        .sorted(
            Comparator.comparing(MemberLevel::getTotalRechAmount, Comparator.reverseOrder())
                .thenComparing(MemberLevel::getLevelValue, Comparator.reverseOrder()))
        .filter(level -> this.isMatchLevel(level, memberInfo))
        .map(MemberLevel::getLevelValue)
        .findFirst()
        .orElse(0);
  }

  /**
   * 分层时需同时满足充值金额和充值次数
   *
   * @param level MemberLevel
   * @param memberInfo MemberInfo
   * @return boolean
   */
  private boolean isMatchLevel(MemberLevel level, MemberInfo memberInfo) {
    return memberInfo.getTotalRechAmount().compareTo(level.getTotalRechAmount()) >= 0
        && memberInfo.getTotalRechTimes() >= level.getTotalRechTimes();
  }

  /**
   * 校验输入的会员(代理)是否存在
   *
   * @param accountList List
   */
  private void allocateAccountIsExist(List<String> accountList) {
    List<String> nonentityAccount = new ArrayList<>();
    accountList.forEach(
        account -> {
          Member member = memberService.getByAccount(account).orElse(null);
          if (member == null) {
            nonentityAccount.add(account);
          }
        });

    if (CollectionUtil.isNotEmpty(nonentityAccount)) {
      throw new ServiceException("你有输入不存在的账号，不存在的账号：" + nonentityAccount.toString());
    }
  }
}
