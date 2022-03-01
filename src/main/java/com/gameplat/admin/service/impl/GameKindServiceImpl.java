package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameKindConvert;
import com.gameplat.admin.mapper.GameKindMapper;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.GameDemoEnableEnum;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameKindServiceImpl extends ServiceImpl<GameKindMapper, GameKind>
        implements GameKindService {

    @Autowired
    private GameKindConvert gameKindConvert;

    @Resource
    private GameKindMapper gameKindMapper;

    @Override
    public IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(dto.getGameType()), GameKind::getGameType, dto.getGameType())
                .eq(
                        ObjectUtils.isNotEmpty(dto.getPlatformCode()),
                        GameKind::getPlatformCode,
                        dto.getPlatformCode())
                .eq(ObjectUtils.isNotEmpty(dto.getHot()), GameKind::getHot, dto.getHot())
                .orderByAsc(Lists.newArrayList(GameKind::getPlatformCode, GameKind::getSort))
                .page(page).convert(gameKindConvert::toVo);
    }

    @Override
    public void updateGameKind(OperGameKindDTO operGameKindDTO) {
        GameKind gameKind = gameKindConvert.toEntity(operGameKindDTO);
        if (!this.updateById(gameKind)) {
            throw new ServiceException("更新游戏分类信息失败!");
        }
    }

    @Override
    public void updateEnable(OperGameKindDTO operGameKindDTO) {
        LambdaUpdateWrapper<GameKind> update = Wrappers.lambdaUpdate();
        update.set(ObjectUtils.isNotNull(operGameKindDTO.getEnable()), GameKind::getEnable, operGameKindDTO.getEnable());
        update.set(GameKind::getUpdateTime, new Date());
        update.ne(GameKind::getEnable, operGameKindDTO.getEnable());
        gameKindMapper.update(null, update);
    }

    @Override
    public void updateDemoEnable(OperGameKindDTO operGameKindDTO) {
        LambdaUpdateWrapper<GameKind> update = Wrappers.lambdaUpdate();
        update.set(ObjectUtils.isNotNull(operGameKindDTO.getDemoEnable()), GameKind::getDemoEnable, operGameKindDTO.getDemoEnable());
        update.set(GameKind::getUpdateTime, new Date());
        //仅修改支持试玩的
        update.ne(GameKind::getDemoEnable, GameDemoEnableEnum.NOT_SUPPORT.getCode());
        gameKindMapper.update(null, update);
    }

    /**
     * 根据gameType(LIVE，CHESS 。。。) 获取游戏平台
     */
    @Override
    @SentinelResource(value = "getGameKindInBanner")
    public List<GameKindVO> getGameKindInBanner(String gameType) {
        return this.lambdaQuery().eq(StringUtils.isNotBlank(gameType), GameKind::getGameType, gameType)
                .eq(GameKind::getStatus,2)
                .eq(GameKind::getEnable, 1)
                .list().stream().map(gameKindConvert::toVo)
                .collect(Collectors.toList());
    }


}
