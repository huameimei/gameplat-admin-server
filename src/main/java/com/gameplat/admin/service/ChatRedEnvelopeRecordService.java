package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.ChatRedEnvelopeRecordQueryDTO;
import com.gameplat.model.entity.chart.ChatRedEnvelopeRecord;

public interface ChatRedEnvelopeRecordService extends IService<ChatRedEnvelopeRecord> {

  /** 红包领取记录 */
  IPage<ChatRedEnvelopeRecord> page(
      PageDTO<ChatRedEnvelopeRecord> page, ChatRedEnvelopeRecordQueryDTO dto);

  void add(ChatRedEnvelopeRecord redEnvelopeRecord);
}
