package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatRedEnvelopeConvert;
import com.gameplat.admin.mapper.ChatRedEnvelopeMapper;
import com.gameplat.admin.model.domain.ChatRedEnvelope;
import com.gameplat.admin.model.dto.ChatRedEnvelopeAddDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeEditDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeQueryDTO;
import com.gameplat.admin.model.vo.ChatRedEnvelopeVO;
import com.gameplat.admin.service.ChatRedEnvelopeService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.BooleanEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author lily
 * @description 聊天室紅包管理
 * @date 2022/2/14
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatRedEnvelopeServiceImpl extends ServiceImpl<ChatRedEnvelopeMapper, ChatRedEnvelope> implements ChatRedEnvelopeService {

    @Autowired
    private ChatRedEnvelopeConvert chatRedEnvelopeConvert;

    /** 分页列表 */
    @Override
    public IPage<ChatRedEnvelopeVO> page(PageDTO<ChatRedEnvelope> page, ChatRedEnvelopeQueryDTO dto) {
        return lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(dto.getOpen()), ChatRedEnvelope::getOpen, dto.getOpen())
                .like(ObjectUtil.isNotEmpty(dto.getName()), ChatRedEnvelope::getName, dto.getName())
                .orderByDesc(ChatRedEnvelope::getLastTime)
                .page(page)
                .convert(chatRedEnvelopeConvert::toVo);
    }

    /** 增 */
    @Override
    public void add(ChatRedEnvelopeAddDTO dto) {
        if (dto.getStartTime() <System.currentTimeMillis()) {
            throw new ServiceException("开始发送时间不能早于当前时间");
        }
        save(chatRedEnvelopeConvert.toEntity(dto));
    }

    /** 删 */
    @Override
    public void remove(Integer id) {
        removeById(id);
    }

    /** 启用禁用 */
    @Override
    public void update(ChatRedEnvelopeEditDTO dto) {
        ChatRedEnvelope entity = new ChatRedEnvelope();
        entity.setId(dto.getId().longValue());
        entity.setOpen(dto.getOpen());
//        if (dto.getOpen().equals(BooleanEnum.YES.value())){
//            entity.setOpen(BooleanEnum.NO.value());
//        }else if (dto.getOpen().equals(BooleanEnum.NO.value())){
//            entity.setOpen(BooleanEnum.YES.value());
//        }
        updateById(entity);
    }




}
