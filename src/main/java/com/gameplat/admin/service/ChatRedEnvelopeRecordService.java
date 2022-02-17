package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatRedEnvelopeRecord;
import com.gameplat.admin.model.dto.ChatRedEnvelopeRecordQueryDTO;

public interface ChatRedEnvelopeRecordService extends IService<ChatRedEnvelopeRecord> {

    /** 红包领取记录 */
    IPage<ChatRedEnvelopeRecord> page(PageDTO<ChatRedEnvelopeRecord> page, ChatRedEnvelopeRecordQueryDTO dto);
}
