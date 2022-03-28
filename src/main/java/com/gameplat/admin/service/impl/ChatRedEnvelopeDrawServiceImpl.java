package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatRedEnvelopeDrawConvert;
import com.gameplat.admin.mapper.ChatRedEnvelopeDrawMapper;
import com.gameplat.admin.model.vo.ChatRedEnvelopeDrawVO;
import com.gameplat.admin.service.ChatRedEnvelopeDrawService;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.model.entity.chart.ChatRedEnvelopeDraw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 红包领取记录
 *
 * @author lily
 * @date 2022/2/15
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatRedEnvelopeDrawServiceImpl
    extends ServiceImpl<ChatRedEnvelopeDrawMapper, ChatRedEnvelopeDraw>
    implements ChatRedEnvelopeDrawService {

  @Autowired private ChatRedEnvelopeDrawConvert chatRedEnvelopeDrawConvert;

  @Override
  public IPage<ChatRedEnvelopeDrawVO> page(
      PageDTO<ChatRedEnvelopeDraw> page, String id, Integer sort) {
    return lambdaQuery()
        .eq(ChatRedEnvelopeDraw::getStatus, 2)
        .eq(ChatRedEnvelopeDraw::getRedEnvelopeRecordId, id)
        .orderByAsc(sort.equals(BooleanEnum.YES.value()), ChatRedEnvelopeDraw::getDrawMoney)
        .orderByDesc(sort.equals(BooleanEnum.NO.value()), ChatRedEnvelopeDraw::getDrawMoney)
        .page(page)
        .convert(chatRedEnvelopeDrawConvert::toVo);
  }
}
