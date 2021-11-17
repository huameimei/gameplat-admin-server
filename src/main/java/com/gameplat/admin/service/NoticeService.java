package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.NoticeAddDTO;
import com.gameplat.admin.model.dto.NoticeEditDTO;
import com.gameplat.admin.model.dto.NoticeQueryDTO;
import com.gameplat.admin.model.dto.NoticeUpdateStatusDTO;
import com.gameplat.admin.model.vo.NoticeVO;

public interface NoticeService extends IService<Notice> {

    IPage<NoticeVO> selectGameList(IPage<Notice> page, NoticeQueryDTO noticeQueryDTO);

    void insertNotice(NoticeAddDTO noticeAddDTO);

    void updateNotice(NoticeEditDTO noticeEditDTO);

    void deleteNotice(Integer id);

    void disableStatus(NoticeUpdateStatusDTO noticeUpdateStatusDTO);

    void enableStatus(NoticeUpdateStatusDTO noticeUpdateStatusDTO);
}
