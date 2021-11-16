package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.NoticeQueryDTO;
import com.gameplat.admin.model.vo.NoticeVO;
import com.gameplat.admin.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description 公告信息
 * @date 2021/11/16
 */

@Slf4j
@RestController
@RequestMapping("/api/notice/announcement/information")
public class OpenAnnouncementController {

    @Autowired private NoticeService noticeService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('notice:announcement:list')")
    public IPage<Notice> findAccountBlacklist(PageDTO<Notice> page, NoticeQueryDTO noticeQueryDTO) {
        return noticeService.selectGameList(page, noticeQueryDTO);
    }
}
