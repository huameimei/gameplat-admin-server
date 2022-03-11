package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameBlacklistConvert;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistTargetType;
import com.gameplat.admin.mapper.GameBlacklistMapper;
import com.gameplat.admin.model.dto.GameBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import com.gameplat.admin.service.GameBlacklistService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.game.GameBlacklist;
import com.gameplat.security.SecurityUserHolder;
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
public class GameBlacklistServiceImpl extends ServiceImpl<GameBlacklistMapper, GameBlacklist>
    implements GameBlacklistService {

  @Autowired private GameBlacklistConvert gameBlacklistConvert;

  @Autowired private GameBlacklistMapper gameBlacklistMapper;

  @Override
  public IPage<GameBlacklist> queryGameBlacklistList(
      PageDTO<GameBlacklist> page, GameBlacklistQueryDTO dto) {
    LambdaQueryWrapper<GameBlacklist> queryWrapper = Wrappers.lambdaQuery();
    if (ObjectUtils.isNotEmpty(dto.getBlackType())) {
      queryWrapper.eq(GameBlacklist::getBlackType, dto.getBlackType());
    }
    if (ObjectUtils.isNotEmpty(dto.getLiveCategory())) {
      queryWrapper.like(GameBlacklist::getLiveCategory, "," + dto.getLiveCategory() + ",");
    }
    if (ObjectUtils.isNotEmpty(dto.getUserAccount())) {
      queryWrapper
          .eq(GameBlacklist::getTarget, dto.getUserAccount())
          .eq(GameBlacklist::getTargetType, BizBlacklistTargetType.USER.getValue());
    }
    if (ObjectUtils.isNotEmpty(dto.getUserLevel())) {
      queryWrapper
          .eq(GameBlacklist::getTarget, dto.getUserLevel())
          .eq(GameBlacklist::getTargetType, BizBlacklistTargetType.USER_LEVEL.getValue());
    }
    return gameBlacklistMapper.selectPage(page, queryWrapper);
  }

  @Override
  public void update(OperGameBlacklistDTO dto) {
    GameBlacklist liveBlacklist = gameBlacklistConvert.toEntity(dto);
    liveBlacklist.setUpdateBy(SecurityUserHolder.getUsername());
    liveBlacklist.setUpdateTime(new Date());
    liveBlacklist.setLiveCategory("," + dto.getLiveCategory() + ",");
    if (!this.updateById(liveBlacklist)) {
      throw new ServiceException("更新真人黑名单失败!");
    }
  }

  @Override
  public void save(OperGameBlacklistDTO dto) {
    if (StringUtils.isEmpty(dto.getTarget())) {
      // 会员
      String msg = Objects.equals(dto.getTargetType(), BizBlacklistTargetType.USER.getValue()) ?
              "请选择要加入黑名单的会员" : "请选择要加入黑名单的会员层级";
      throw new ServiceException(msg);
    }
    GameBlacklist exists =
        this.lambdaQuery()
            .eq(ObjectUtils.isNotEmpty(dto.getTarget()), GameBlacklist::getTarget, dto.getTarget())
            .eq(
                ObjectUtils.isNotEmpty(dto.getTargetType()),
                GameBlacklist::getTargetType,
                dto.getTargetType())
            .eq(
                ObjectUtils.isNotEmpty(dto.getBlackType()),
                GameBlacklist::getBlackType,
                dto.getBlackType())
            .one();
    Date now = new Date();
    String username = SecurityUserHolder.getUsername();
    if (exists != null) {
      GameBlacklist update = gameBlacklistConvert.toEntity(dto);
      update.setId(exists.getId());
      update.setUpdateBy(username);
      update.setUpdateTime(now);
      Set<String> liveCategorys =
          Stream.of(exists.getLiveCategory().split(",")).collect(Collectors.toSet());
      liveCategorys.addAll(Arrays.asList(dto.getLiveCategory().split(",")));
      update.setLiveCategory("," + StringUtils.join(liveCategorys, ",") + ",");
      if (!this.updateById(update)) {
        throw new ServiceException("更新真人黑名单失败!");
      }
    } else {
      GameBlacklist create = gameBlacklistConvert.toEntity(dto);
      create.setCreateTime(now);
      create.setCreateBy(username);
      create.setLiveCategory("," + dto.getLiveCategory() + ",");
      create.setUpdateTime(now);
      create.setUpdateBy(username);
      if (!this.save(create)) {
        throw new ServiceException("添加真人黑名单失败!");
      }
    }
  }

  @Override
  public void delete(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除真人黑名单失败!");
    }
  }

  @Override
  public List<GameBlacklist> selectGameBlackList(GameBlacklist black) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(black.getBlackType()),
            GameBlacklist::getBlackType,
            black.getBlackType())
        .like(
            ObjectUtils.isNotEmpty(black.getLiveCategory()),
            GameBlacklist::getLiveCategory,
            "," + black.getLiveCategory() + ",")
        .eq(
            ObjectUtils.isNotEmpty(black.getTargetType()),
            GameBlacklist::getTargetType,
            black.getTargetType())
        .eq(ObjectUtils.isNotEmpty(black.getTarget()), GameBlacklist::getTarget, black.getTarget())
        .list();
  }
}
