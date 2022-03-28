package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ChatRedEnvelopeRecordMapper;
import com.gameplat.admin.model.dto.ChatRedEnvelopeRecordQueryDTO;
import com.gameplat.admin.service.ChatRedEnvelopeRecordService;
import com.gameplat.model.entity.chart.ChatRedEnvelopeRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 红包记录
 *
 * @author lily
 * @date 2022/2/15
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatRedEnvelopeRecordServiceImpl
    extends ServiceImpl<ChatRedEnvelopeRecordMapper, ChatRedEnvelopeRecord>
    implements ChatRedEnvelopeRecordService {

  @Override
  public IPage<ChatRedEnvelopeRecord> page(
      PageDTO<ChatRedEnvelopeRecord> page, ChatRedEnvelopeRecordQueryDTO dto) {
    return lambdaQuery()
        .eq(
            ObjectUtil.isNotEmpty(dto.getRedConfigId()),
            ChatRedEnvelopeRecord::getRedConfigId,
            dto.getRedConfigId())
        .orderByDesc(ChatRedEnvelopeRecord::getCreateTime)
        .page(page);
  }

  @Override
  public void add(ChatRedEnvelopeRecord redEnvelopeRecord) {
    redEnvelopeRecord.setCreateTime(System.currentTimeMillis());
    save(redEnvelopeRecord);
  }
}
