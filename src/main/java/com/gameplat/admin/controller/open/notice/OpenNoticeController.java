package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.NoticeVO;
import com.gameplat.admin.service.NoticeService;
import com.gameplat.common.context.GlobalContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author lily
 * @description 公告信息
 * @date 2021/11/16
 */

@Slf4j
@RestController
@RequestMapping("/api/notice/announcement")
public class OpenNoticeController {

    @Autowired private NoticeService noticeService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('notice:announcement:list')")
    public IPage<NoticeVO> findAccountBlacklist(PageDTO<Notice> page, NoticeQueryDTO noticeQueryDTO) {
        return noticeService.selectNoticeList(page, noticeQueryDTO);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('notice:announcement:add')")
    public void add(@RequestBody NoticeAddDTO noticeAddDTO) {
        noticeAddDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        noticeAddDTO.setStatus(0);
        noticeService.insertNotice(noticeAddDTO);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('notice:announcement:edit')")
    public void edit(@RequestBody NoticeEditDTO noticeEditDTO) {
        noticeEditDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        noticeService.updateNotice(noticeEditDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('notice:announcement:remove')")
    public void remove(@PathVariable("id") Integer id) {
        noticeService.deleteNotice(id);
    }

    @PostMapping("/disable")
    @PreAuthorize("hasAuthority('notice:announcement:disable')")
    public void disableStatus(@RequestBody NoticeUpdateStatusDTO updateStatusDTO) {
        updateStatusDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        updateStatusDTO.setStatus(1);
        noticeService.disableStatus(updateStatusDTO);
    }

    @PostMapping("/enable")
    @PreAuthorize("hasAuthority('notice:announcement:enable')")
    public void enableStatus(@RequestBody NoticeUpdateStatusDTO updateStatusDTO) {
        updateStatusDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        updateStatusDTO.setStatus(0);
        noticeService.enableStatus(updateStatusDTO);
    }
}
