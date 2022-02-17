package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatRedEnvelopeDraw;
import com.gameplat.admin.model.vo.ChatRedEnvelopeDrawVO;

public interface ChatRedEnvelopeDrawService extends IService<ChatRedEnvelopeDraw> {

    /** 红包领取记录 */
    IPage<ChatRedEnvelopeDrawVO> page(PageDTO<ChatRedEnvelopeDraw>page, String id, Integer sort);
}
