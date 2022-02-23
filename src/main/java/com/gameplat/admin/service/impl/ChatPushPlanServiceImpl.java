package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatPushPlanConvert;
import com.gameplat.admin.mapper.ChatPushPlanMapper;
import com.gameplat.admin.model.domain.ChatPushPlan;
import com.gameplat.admin.model.dto.ChatPushPlanAddOrEditDTO;
import com.gameplat.admin.model.dto.ChatPushPlanQueryDTO;
import com.gameplat.admin.model.vo.ChatPushPlanVO;
import com.gameplat.admin.service.ChatPushPlanService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ChatPushPlanConvert chatPushPlanConvert;

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

    /** 自定义中奖推送分页列表 */
    @Override
    public IPage<ChatPushPlanVO> page(PageDTO<ChatPushPlan> page, ChatPushPlanQueryDTO dto) {
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(dto.getAccount()), ChatPushPlan::getAccount, dto.getAccount())
                .eq(ObjectUtil.isNotEmpty(dto.getRoomId()), ChatPushPlan::getRoomId, dto.getRoomId())
                .eq(ObjectUtil.isNotEmpty(dto.getGameId()), ChatPushPlan::getGameId, dto.getGameId())
                .orderByDesc(ChatPushPlan::getCreateTime)
                .page(page)
                .convert(chatPushPlanConvert::toVo)
                ;
    }

    /** 根据id查询 */
    @Override
    public ChatPushPlan one(ChatPushPlan dto) {
        return this.lambdaQuery()
                .eq(ChatPushPlan::getId, dto.getId())
                .one();
    }

    /** 新增自定义中奖推送 */
    @Override
    public void add(ChatPushPlanAddOrEditDTO dto) {
        if (dto.getMinWinMoney()>dto.getMaxWinMoney()) {
            throw new ServiceException("最小推送金额不能大于最大推送金额");
        }
        dto.setState(1);
        dto.setGameStatus(1);
        dto.setLastPushTime(dto.getBeginTime());
        dto.setCreateTime(Long.parseLong(DateUtil.getCurrentTime()));
        save(chatPushPlanConvert.toEntity(dto));
    }

    /** 编辑自定义中奖推送 */
    @Override
    public void edit(ChatPushPlanAddOrEditDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new ServiceException("id不能为空！");
        }
        ChatPushPlan one = one(new ChatPushPlan() {{
            setId(dto.getId());
        }});
        if (StringUtils.isEmpty(one)) {
            throw new ServiceException("此id不存在！");
        }
        if (one.getGameStatus()!=1){
        throw new ServiceException("此彩种维护中");
        }
        dto.setUpdateTime(Long.parseLong(DateUtil.getCurrentTime()));
        updateById(chatPushPlanConvert.toEntity(dto));
    }

    /** 根据Id删除中奖推送 */
    @Override
    public void remove(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空！");
        }
        removeById(id);
    }
}
