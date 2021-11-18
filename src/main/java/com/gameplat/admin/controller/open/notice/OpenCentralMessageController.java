package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.CentralMessage;
import com.gameplat.admin.model.vo.CentralMessageVO;
import com.gameplat.admin.service.CentralMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description 中心通知
 * @date 2021/11/18
 */

@Slf4j
@RestController
@RequestMapping("/api/notice/centralMessage")
public class OpenCentralMessageController {

    @Autowired private CentralMessageService centralMessageService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('notice:centralMessage:list')")
    public IPage<CentralMessageVO> findCentralMessageList(PageDTO<CentralMessage> page) {
        return centralMessageService.selectCentralMessageList(page);
    }
}
