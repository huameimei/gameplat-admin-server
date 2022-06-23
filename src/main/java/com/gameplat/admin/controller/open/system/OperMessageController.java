package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysMessageDTO;
import com.gameplat.admin.service.SysMessageService;
import com.gameplat.model.entity.sys.SysMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "系统消息")
@RestController
@RequestMapping("/api/admin/system/sysmessage")
public class OperMessageController {

  @Autowired private SysMessageService sysMessageService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  public IPage<SysMessage> list(PageDTO<SysMessage> page, SysMessageDTO dto) {
    return sysMessageService.pageList(page, dto);
  }

  @Operation(summary = "获取最新消息")
  @GetMapping("/lastList")
  public List<SysMessage> lastList(SysMessageDTO dto) {
    return sysMessageService.lastList(dto);
  }
}
