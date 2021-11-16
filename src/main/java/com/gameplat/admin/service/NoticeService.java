package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.NoticeQueryDTO;

public interface NoticeService extends IService<Notice> {

    PageDTO<Notice> selectGameList(PageDTO<Notice> page, NoticeQueryDTO noticeQueryDTO);
}
