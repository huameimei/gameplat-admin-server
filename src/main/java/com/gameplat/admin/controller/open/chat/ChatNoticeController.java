package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ChatNoticeAddDTO;
import com.gameplat.admin.model.dto.ChatNoticeEditDTO;
import com.gameplat.admin.model.dto.ChatNoticeQueryDTO;
import com.gameplat.admin.model.vo.ChatNoticeVO;
import com.gameplat.admin.service.ChatNoticeService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.chart.ChatNotice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lily
 * @description 聊天室公告管理
 * @date 2022/02/09
 */
@Tag(name = "聊天室公告管理")
@RestController
@RequestMapping("/api/admin/chat/notice")
public class ChatNoticeController {

  @Autowired private ChatNoticeService chatNoticeService;

  @Operation(summary = "分页列表")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('chat:notice:view')")
  public IPage<ChatNoticeVO> page(PageDTO<ChatNotice> page, ChatNoticeQueryDTO dto) {
    return chatNoticeService.page(page, dto);
  }

  @Operation(summary = "增")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('chat:notice:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室公告管理->增:' + #dto")
  public void add(@Validated @RequestBody  ChatNoticeAddDTO dto) {
    chatNoticeService.add(dto);
  }

  @Operation(summary = "删")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('chat:notice:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室公告管理->删:' + #id")
  public void remove(@PathVariable Long id) {
    chatNoticeService.remove(id);
  }

  @Operation(summary = "改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('chat:notice:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室公告管理->改:' + #dto")
  public void edit(@RequestBody ChatNoticeEditDTO dto) {
    chatNoticeService.edit(dto);
  }
}
