package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ChatNotice;
import com.gameplat.admin.model.dto.ChatNoticeAddDTO;
import com.gameplat.admin.model.dto.ChatNoticeEditDTO;
import com.gameplat.admin.model.dto.ChatNoticeQueryDTO;
import com.gameplat.admin.model.vo.ChatNoticeVO;

/**
 * @Author lily
 * @Date 2022/2/09
 **/
public interface ChatNoticeService extends IService<ChatNotice> {

    /** 分页查询 */
    IPage<ChatNoticeVO> page(PageDTO<ChatNotice> page, ChatNoticeQueryDTO dto);

    /** 增 */
    void add(ChatNoticeAddDTO dto);

    /** 删 */
    void remove(Long id);

    /** 改 */
    void edit(ChatNoticeEditDTO dto);
}
