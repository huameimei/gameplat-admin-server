package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberLevelConvert;
import com.gameplat.admin.enums.MemberLevelEnums;
import com.gameplat.admin.mapper.MemberLevelMapper;
import com.gameplat.admin.model.dto.MemberLevelAddDTO;
import com.gameplat.admin.model.dto.MemberLevelAllocateDTO;
import com.gameplat.admin.model.dto.MemberLevelEditDTO;
import com.gameplat.admin.model.vo.MemberLevelVO;
import com.gameplat.admin.service.MemberLevelService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.member.MemberLevel;
import lombok.extern.slf4j.Slf4j;
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
  @Cached(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'", expire = 3600)
  public List<MemberLevelVO> getList() {
    return this.list().stream().map(memberLevelConvert::toVo).collect(Collectors.toList());
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

    if (level.getMemberNum() > 0) {
      throw new ServiceException("层级内存在会员，无法删除!");
    }

    this.removeById(id);
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void lock(Long id) {
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getLocked, MemberLevelEnums.Locked.Y.value())
        .update();
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_LEVEL_CACHE, key = "'all'")
  public void unlock(Long id) {
    this.lambdaUpdate()
        .eq(MemberLevel::getId, id)
        .set(MemberLevel::getLocked, MemberLevelEnums.Locked.N.value())
        .update();
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
  public void batchAllocate(List<MemberLevelAllocateDTO> dtos) {
    List<MemberLevel> memberLevels = this.getEnabledLevels();
    dtos.forEach(dto -> this.allocateLevel(memberLevels, dto));
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

    log.info("层级{}，共{}条需要重新分配层级的会员", dto.getLevelName(), updateMembers.size());

    // 批量更新会员层级
    memberService.updateBatchById(updateMembers);

    // 批量修改层级会员数
    this.updateLevelMemberNum(levelValue, updateMembers);
  }

  /**
   * 修改会员层级会员数和锁定会员数
   *
   * @param oldLevel Integer
   * @param members List
   */
  private void updateLevelMemberNum(Integer oldLevel, List<Member> members) {
    Map<Integer, Integer> map = new HashMap<>(members.size());
    members.stream()
        .map(Member::getUserLevel)
        .forEach(
            level -> {
              // 新的层级会员数量增加
              map.merge(level, 1, Integer::sum);
              // 旧的层级会员数量减少
              map.merge(oldLevel, -1, Integer::sum);
            });

    if (MapUtil.isNotEmpty(map)) {
      memberLevelMapper.batchUpdateMemberNum(map);
    }
  }

  private List<Member> builderMembersForUpdate(
      List<MemberLevel> levels, List<MemberInfo> memberInfos) {
    return memberInfos.stream()
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
}
