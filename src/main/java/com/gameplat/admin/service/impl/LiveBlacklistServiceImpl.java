package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.LiveBlacklistConvert;
import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistTargetType;
import com.gameplat.admin.mapper.LiveBlacklistMapper;
import com.gameplat.admin.model.domain.LiveBlacklist;
import com.gameplat.admin.model.dto.LiveBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperLiveBlacklistDTO;
import com.gameplat.admin.service.LiveBlacklistService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.security.SecurityUserHolder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveBlacklistServiceImpl extends ServiceImpl<LiveBlacklistMapper, LiveBlacklist>
    implements LiveBlacklistService {

  @Autowired private LiveBlacklistConvert liveBlacklistConvert;

  @Autowired private LiveBlacklistMapper liveBlacklistMapper;

  @Override
  public IPage<LiveBlacklist> queryLiveBlacklistList(
      PageDTO<LiveBlacklist> page, LiveBlacklistQueryDTO dto) {
    LambdaQueryWrapper<LiveBlacklist> queryWrapper = Wrappers.lambdaQuery();
    if (ObjectUtils.isNotEmpty(dto.getBlackType())) {
      queryWrapper.eq(LiveBlacklist::getBlackType, dto.getBlackType());
    }
    if (ObjectUtils.isNotEmpty(dto.getLiveCategory())) {
      queryWrapper.like(LiveBlacklist::getLiveCategory, "," + dto.getLiveCategory() + ",");
    }
    if (ObjectUtils.isNotEmpty(dto.getUserAccount())) {
      queryWrapper
          .eq(LiveBlacklist::getTarget, dto.getUserAccount())
          .eq(LiveBlacklist::getTargetType, BizBlacklistTargetType.USER.getValue());
    }
    if (ObjectUtils.isNotEmpty(dto.getUserLevel())) {
      queryWrapper
          .eq(LiveBlacklist::getTarget, dto.getUserLevel())
          .eq(LiveBlacklist::getTargetType, BizBlacklistTargetType.USER_LEVEL.getValue());
    }
    return liveBlacklistMapper.selectPage(page, queryWrapper);
  }

  @Override
  public void update(OperLiveBlacklistDTO dto) {
    LiveBlacklist liveBlacklist = liveBlacklistConvert.toEntity(dto);
    liveBlacklist.setUpdateBy(SecurityUserHolder.getUsername());
    liveBlacklist.setUpdateTime(new Date());
    liveBlacklist.setLiveCategory("," + dto.getLiveCategory() + ",");
    if (!this.updateById(liveBlacklist)) {
      throw new ServiceException("更新真人黑名单失败!");
    }
  }

  @Override
  public void save(OperLiveBlacklistDTO dto) {
    LiveBlacklist exists =
        this.lambdaQuery()
            .eq(ObjectUtils.isNotEmpty(dto.getTarget()), LiveBlacklist::getTarget, dto.getTarget())
            .eq(
                ObjectUtils.isNotEmpty(dto.getTargetType()),
                LiveBlacklist::getTargetType,
                dto.getTargetType())
            .eq(
                ObjectUtils.isNotEmpty(dto.getBlackType()),
                LiveBlacklist::getBlackType,
                dto.getBlackType())
            .one();
    Date now = new Date();
    String username = SecurityUserHolder.getUsername();
    if (exists != null) {
      LiveBlacklist update = liveBlacklistConvert.toEntity(dto);
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
      LiveBlacklist create = liveBlacklistConvert.toEntity(dto);
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
  public List<LiveBlacklist> selectLiveBlackList(LiveBlacklist black){
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(black.getBlackType()),LiveBlacklist::getBlackType,black.getBlackType())
        .like(ObjectUtils.isNotEmpty(black.getLiveCategory()),LiveBlacklist::getLiveCategory,","+black.getLiveCategory()+",")
        .eq(ObjectUtils.isNotEmpty(black.getTargetType()),LiveBlacklist::getTargetType,black.getTargetType())
        .eq(ObjectUtils.isNotEmpty(black.getTarget()),LiveBlacklist::getTarget, black.getTarget())
        .list();
  }

}
