package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.NoticeAddDTO;
import com.gameplat.admin.model.dto.NoticeEditDTO;
import com.gameplat.admin.model.dto.NoticeQueryDTO;
import com.gameplat.admin.model.dto.NoticeUpdateStatusDTO;
import com.gameplat.admin.model.vo.NoticeDictDataVO;
import com.gameplat.admin.model.vo.NoticeVO;
import com.gameplat.admin.service.NoticeService;
import com.gameplat.base.common.context.GlobalContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lily
 * @description 公告信息
 * @date 2021/11/16
 */

@Api(tags = "公告消息")
@Slf4j
@RestController
@RequestMapping("/api/admin/notice/official-news")
public class OpenNoticeController {

    @Autowired private NoticeService noticeService;

    @ApiOperation(value = "公告消息和个人弹窗消息字典数据列表")
    @GetMapping("/getTypes")
    public NoticeDictDataVO getDictData() {
        return noticeService.getDictData();
    }

    @ApiOperation(value = "公告消息列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('notice:official-news:list')")
    public IPage<NoticeVO> findAccountBlacklist(PageDTO<Notice> page, NoticeQueryDTO noticeQueryDTO) {
        return noticeService.selectNoticeList(page, noticeQueryDTO);
    }

    @ApiOperation(value = "新增公告消息")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('notice:official-news:add')")
    public void add(@RequestBody NoticeAddDTO noticeAddDTO) {
        noticeAddDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        noticeAddDTO.setStatus(0);
        noticeService.insertNotice(noticeAddDTO);
    }

    @ApiOperation(value = "编辑公告消息")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('notice:official-news:edit')")
    public void edit(@RequestBody NoticeEditDTO noticeEditDTO) {
        noticeEditDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        noticeService.updateNotice(noticeEditDTO);
    }

    @ApiOperation(value = "删除公告消息")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('notice:official-news:remove')")
    public void remove(@PathVariable("id") Integer id) {
        noticeService.deleteNotice(id);
    }

    @ApiOperation(value = "禁用")
    @PostMapping("/disable")
    @PreAuthorize("hasAuthority('notice:official-news:disable')")
    public void disableStatus(@RequestBody NoticeUpdateStatusDTO updateStatusDTO) {
        updateStatusDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        updateStatusDTO.setStatus(1);
        noticeService.disableStatus(updateStatusDTO);
    }

    @ApiOperation(value = "开启")
    @PostMapping("/enable")
    @PreAuthorize("hasAuthority('notice:official-news:enable')")
    public void enableStatus(@RequestBody NoticeUpdateStatusDTO updateStatusDTO) {
        updateStatusDTO.setOperator(GlobalContextHolder.getContext().getUsername());
        updateStatusDTO.setStatus(0);
        noticeService.enableStatus(updateStatusDTO);
    }
}
