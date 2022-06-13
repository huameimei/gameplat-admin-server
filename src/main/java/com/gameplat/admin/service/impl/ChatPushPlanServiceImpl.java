package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatPushPlanConvert;
import com.gameplat.admin.mapper.ChatPushPlanMapper;
import com.gameplat.admin.model.dto.ChatPushPlanAddOrEditDTO;
import com.gameplat.admin.model.dto.ChatPushPlanQueryDTO;
import com.gameplat.admin.model.vo.ChatPushPlanVO;
import com.gameplat.admin.service.ChatPushPlanService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.model.entity.chart.ChatPushPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author lily
 * @date 2022/2/16
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatPushPlanServiceImpl extends ServiceImpl<ChatPushPlanMapper, ChatPushPlan>
    implements ChatPushPlanService {

  @Autowired private ChatPushPlanConvert chatPushPlanConvert;

  @Override
  public void updatePushPlan(String gameId, int gameStatus) {
    Integer state = null;
    // 如果游戏没开启就设置自动计划为禁用
    if (gameStatus != 1) {
      state = 0;
    }
    LambdaUpdateWrapper<ChatPushPlan> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .set(ObjectUtil.isNotEmpty(gameStatus), ChatPushPlan::getGameStatus, gameStatus)
        .set(ObjectUtil.isNotEmpty(state), ChatPushPlan::getState, state)
        .eq(ChatPushPlan::getGameId, gameId);
    update(updateWrapper);
  }

  @Override
  public IPage<ChatPushPlanVO> page(PageDTO<ChatPushPlan> page, ChatPushPlanQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtil.isNotEmpty(dto.getAccount()), ChatPushPlan::getAccount, dto.getAccount())
        .eq(ObjectUtil.isNotEmpty(dto.getRoomId()), ChatPushPlan::getRoomId, dto.getRoomId())
        .eq(ObjectUtil.isNotEmpty(dto.getGameId()), ChatPushPlan::getGameId, dto.getGameId())
        .orderByDesc(ChatPushPlan::getCreateTime)
        .page(page)
        .convert(chatPushPlanConvert::toVo);
  }

  @Override
  public ChatPushPlan one(ChatPushPlan dto) {
    return this.lambdaQuery().eq(ChatPushPlan::getId, dto.getId()).one();
  }

  @Override
  public void add(ChatPushPlanAddOrEditDTO dto) {
    if (dto.getMinWinMoney() > dto.getMaxWinMoney()) {
      throw new ServiceException("最小推送金额不能大于最大推送金额");
    }
    dto.setState(1);
    dto.setGameStatus(1);
    dto.setLastPushTime(dto.getBeginTime());
    dto.setCreateTime(Long.parseLong(DateUtil.getCurrentTime()));
    save(chatPushPlanConvert.toEntity(dto));
  }

  @Override
  public void edit(ChatPushPlanAddOrEditDTO dto) {
    if (null == dto.getId()) {
      throw new ServiceException("id不能为空！");
    }
    ChatPushPlan one =
        one(
            new ChatPushPlan() {
              {
                setId(dto.getId());
              }
            });
    if (Objects.isNull(one)) {
      throw new ServiceException("此id不存在！");
    }
    if (one.getGameStatus() != 1) {
      throw new ServiceException("此彩种维护中");
    }
    dto.setUpdateTime(Long.parseLong(DateUtil.getCurrentTime()));
    updateById(chatPushPlanConvert.toEntity(dto));
  }

  @Override
  public void remove(Long id) {
    if (null == id) {
      throw new ServiceException("id不能为空！");
    }
    removeById(id);
  }
}
