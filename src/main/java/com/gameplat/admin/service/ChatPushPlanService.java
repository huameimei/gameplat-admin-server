package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatPushPlan;

public interface ChatPushPlanService extends IService<ChatPushPlan> {

    /** 游戏维护更新自定义中奖推送 */
    void updatePushPlan(String gameId, int gameStatus);
}
