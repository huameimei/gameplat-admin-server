package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ChatPushPlanMapper;
import com.gameplat.admin.model.domain.ChatPushPlan;
import com.gameplat.admin.service.ChatPushPlanService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatPushPlanServiceImpl extends ServiceImpl<ChatPushPlanMapper, ChatPushPlan> implements ChatPushPlanService {

    /** 游戏维护更新自定义中奖推送 */
    @Override
    public void updatePushPlan(String gameId, int gameStatus) {
        Integer state=null;
        //如果游戏没开启就设置自动计划为禁用
        if (gameStatus!=1) {
            state=0;
        }
        ChatPushPlan chatPushPlan = new ChatPushPlan();
        LambdaUpdateWrapper<ChatPushPlan> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ObjectUtil.isNotEmpty(gameStatus), ChatPushPlan::getGameStatus, gameStatus)
                .set(ObjectUtil.isNotEmpty(state), ChatPushPlan::getState, state)
                .eq(ChatPushPlan::getGameId, gameId);
        update(updateWrapper);
    }
}
