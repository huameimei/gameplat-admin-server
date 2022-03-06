package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysMessageDTO;
import com.gameplat.admin.service.SysMessageService;
import com.gameplat.model.entity.sys.SysMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统消息
 *
 * @author gray
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/system/sysmessage")
public class OperMessageController {

    @Autowired
    private SysMessageService sysMessageService;

    @GetMapping("/list")
    public IPage<SysMessage> list(PageDTO<SysMessage> page, SysMessageDTO dto) {
        return sysMessageService.pageList(page, dto);
    }
}
