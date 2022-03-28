package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.BizBlacklistConvert;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistStatus;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistTargetType;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.mapper.BizBlacklistMapper;
import com.gameplat.admin.model.dto.BizBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBizBlacklistDTO;
import com.gameplat.admin.service.BizBlacklistService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.blacklist.BizBlacklist;
import com.gameplat.model.entity.member.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class BizBlacklistServiceImpl extends ServiceImpl<BizBlacklistMapper, BizBlacklist>
    implements BizBlacklistService {

  @Autowired private BizBlacklistConvert bizBlacklistConvert;

  @Autowired private BizBlacklistMapper bizBlacklistMapper;

  @Autowired private MemberService memberService;

  @Override
  public IPage<BizBlacklist> queryBizBlacklistList(
      PageDTO<BizBlacklist> page, BizBlacklistQueryDTO dto) {
    LambdaQueryWrapper<BizBlacklist> queryWrapper = Wrappers.lambdaQuery();
    if (ObjectUtils.isNotEmpty(dto.getUserAccount())) {
      queryWrapper
          .eq(BizBlacklist::getTarget, dto.getUserAccount())
          .eq(BizBlacklist::getTargetType, BizBlacklistTargetType.USER.getValue());
    }
    if (ObjectUtils.isNotEmpty(dto.getUserLevel())) {
      queryWrapper
          .eq(BizBlacklist::getTarget, dto.getUserLevel())
          .eq(BizBlacklist::getTargetType, BizBlacklistTargetType.USER_LEVEL.getValue());
    }
    if (ObjectUtils.isNotEmpty(dto.getStatus())) {
      queryWrapper.eq(BizBlacklist::getStatus, dto.getStatus());
    }
    return bizBlacklistMapper.selectPage(page, queryWrapper);
  }

  @Override
  public void update(OperBizBlacklistDTO dto) {
    validateBizBlacklist(dto);
    Optional.ofNullable(dto.getId())
        .map(bizBlacklistMapper::selectById)
        .orElseThrow(() -> new ServiceException("无效的ID"));
    BizBlacklist bizBlacklist = bizBlacklistConvert.toEntity(dto);
    bizBlacklist.setId(dto.getId());
    if (!this.updateById(bizBlacklist)) {
      throw new ServiceException("更新业务黑名单失败!");
    }
  }

  @Override
  public void save(OperBizBlacklistDTO dto) {
    validateBizBlacklist(dto);
    BizBlacklist exists =
        this.lambdaQuery()
            .eq(ObjectUtils.isNotEmpty(dto.getTarget()), BizBlacklist::getTarget, dto.getTarget())
            .eq(
                ObjectUtils.isNotEmpty(dto.getTargetType()),
                BizBlacklist::getTargetType,
                dto.getTargetType())
            .one();
    if (exists != null) {
      BizBlacklist update = bizBlacklistConvert.toEntity(dto);
      update.setId(exists.getId());
      if (dto.isReplaceExists()) {
        update.setTypes(dto.getTypes());
      } else {
        Set<String> types = Stream.of(exists.getTypes().split(",")).collect(Collectors.toSet());
        types.addAll(Arrays.asList(dto.getTypes().split(",")));
        update.setTypes(StringUtils.join(types, ","));
      }
      if (!this.updateById(update)) {
        throw new ServiceException("添加业务黑名单失败!");
      }
    } else {
      if (!this.save(bizBlacklistConvert.toEntity(dto))) {
        throw new ServiceException("添加业务黑名单失败!");
      }
    }
  }

  @Override
  public void delete(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除业务黑名单失败!");
    }
  }

  private void validateBizBlacklist(OperBizBlacklistDTO bizBlacklist) {
    if (bizBlacklist.getTypes() != null) {
      String[] types = bizBlacklist.getTypes().split(",");
      for (String type : types) {
        Optional.ofNullable(BizBlacklistType.matches(type))
            .orElseThrow(() -> new ServiceException("无效的业务类型"));
      }
    }
    if (bizBlacklist.getStatus() != null) {
      Optional.ofNullable(BizBlacklistStatus.matches(bizBlacklist.getStatus()))
          .orElseThrow(() -> new ServiceException("无效的状态"));
    }
  }

  @Override
  public Set<String> getBizBlacklistTypesByUserId(Long userId) {
    return getBizBlacklistTypesByUser(
        Optional.ofNullable(userId)
            .map(memberService::getById)
            .orElseThrow(() -> new ServiceException("无效的用户ID")));
  }

  @Override
  public Set<String> getBizBlacklistTypesByUser(Member member) {
    return Optional.ofNullable(member)
        .map(
            u -> {
              // 会员黑名单
              BizBlacklist userBizBlacklist =
                  getByTargetAndTargetTypeAndStatus(
                      u.getAccount(),
                      BizBlacklistTargetType.USER.getValue(),
                      BizBlacklistStatus.ENABLED.getValue());
              // 层级黑名单
              BizBlacklist userLevelBizBlacklist =
                  getByTargetAndTargetTypeAndStatus(
                      u.getUserLevel().toString(),
                      BizBlacklistTargetType.USER_LEVEL.getValue(),
                      BizBlacklistStatus.ENABLED.getValue());
              return combineAllTypes(userBizBlacklist, userLevelBizBlacklist);
            })
        .orElseThrow(() -> new ServiceException("无效的用户信息"));
  }

  private BizBlacklist getByTargetAndTargetTypeAndStatus(
      String target, Integer targetType, Integer status) {
    return this.lambdaQuery()
        .eq(BizBlacklist::getTarget, target)
        .eq(BizBlacklist::getTargetType, targetType)
        .eq(BizBlacklist::getStatus, status)
        .one();
  }

  private Set<String> combineAllTypes(BizBlacklist... bizBlacklists) {
    Set<String> types = new HashSet<>();
    Optional.ofNullable(bizBlacklists)
        .ifPresent(
            bls ->
                Stream.of(bls)
                    .filter(Objects::nonNull)
                    .forEach(bl -> types.addAll(Arrays.asList(bl.getTypes().split(",")))));
    return types;
  }
}
