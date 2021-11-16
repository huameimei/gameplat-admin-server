package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.mapper.NoticeMapper;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.NoticeQueryDTO;
import com.gameplat.admin.service.NoticeService;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lily
 * @description 公告信息业务处理层
 * @date 2021/11/16
 */

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl  extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public PageDTO<Notice> selectGameList(PageDTO<Notice> page, NoticeQueryDTO noticeQueryDTO) {
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(noticeQueryDTO.getNoticeType()), Notice::getNoticeType, noticeQueryDTO.getNoticeType())
                .eq(ObjectUtils.isNotEmpty(noticeQueryDTO.getStatus()), Notice::getStatus, noticeQueryDTO.getStatus())
                .eq(ObjectUtils.isNotEmpty(noticeQueryDTO.getNoticeTitle()), Notice::getNoticeTitle, noticeQueryDTO.getNoticeTitle())
                .page(page);
    }

}
