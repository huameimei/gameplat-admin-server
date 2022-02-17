package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatRedEnvelope;
import com.gameplat.admin.model.dto.ChatRedEnvelopeAddDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeEditDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeQueryDTO;
import com.gameplat.admin.model.vo.ChatRedEnvelopeVO;

public interface ChatRedEnvelopeService extends IService<ChatRedEnvelope> {

    /** 分页列表 */
    IPage<ChatRedEnvelopeVO> page(PageDTO<ChatRedEnvelope>page, ChatRedEnvelopeQueryDTO dto);

    /** 增 */
    void add(ChatRedEnvelopeAddDTO dto);

    /** 删 */
    void remove(Integer id);

    /** 启用禁用 */
    void update(ChatRedEnvelopeEditDTO dto);
}
