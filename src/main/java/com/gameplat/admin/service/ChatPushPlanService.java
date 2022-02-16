package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatPushPlan;
import com.gameplat.admin.model.dto.ChatPushPlanAddOrEditDTO;
import com.gameplat.admin.model.dto.ChatPushPlanQueryDTO;
import com.gameplat.admin.model.vo.ChatPushPlanVO;

public interface ChatPushPlanService extends IService<ChatPushPlan> {

    /** 游戏维护更新自定义中奖推送 */
    void updatePushPlan(String gameId, int gameStatus);

    /** 自定义中奖推送分页列表 */
    IPage<ChatPushPlanVO> page(PageDTO<ChatPushPlan> page, ChatPushPlanQueryDTO dto);

    /** 根据id查询 */
    ChatPushPlan one(ChatPushPlan dto);

    /** 新增自定义中奖推送 */
    void add(ChatPushPlanAddOrEditDTO dto);

    /** 编辑自定义中奖推送 */
    void edit(ChatPushPlanAddOrEditDTO dto);

    /** 根据Id删除中奖推送 */
    void remove(Long id);
}
