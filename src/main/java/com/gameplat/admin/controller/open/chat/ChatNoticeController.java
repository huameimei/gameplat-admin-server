package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ChatNoticeAddDTO;
import com.gameplat.admin.model.dto.ChatNoticeEditDTO;
import com.gameplat.admin.model.dto.ChatNoticeQueryDTO;
import com.gameplat.admin.model.vo.ChatNoticeVO;
import com.gameplat.admin.service.ChatNoticeService;
import com.gameplat.model.entity.chart.ChatNotice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lily
 * @description 聊天室公告管理
 * @date 2022/02/09
 */
@Api(tags = "聊天室公告管理")
@RestController
@RequestMapping("/api/admin/chat/notice")
public class ChatNoticeController {

  @Autowired private ChatNoticeService chatNoticeService;

  @ApiOperation(value = "分页列表")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('chat:notice:view')")
  public IPage<ChatNoticeVO> page(PageDTO<ChatNotice> page, ChatNoticeQueryDTO dto) {
    return chatNoticeService.page(page, dto);
  }

  @ApiOperation(value = "增")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('chat:notice:add')")
  public void add(@Validated ChatNoticeAddDTO dto) {
    chatNoticeService.add(dto);
  }

  @ApiOperation(value = "删")
  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('chat:notice:remove')")
  public void remove(@PathVariable Long id) {
    chatNoticeService.remove(id);
  }

  @ApiOperation(value = "改")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('chat:notice:edit')")
  public void edit(ChatNoticeEditDTO dto) {
    chatNoticeService.edit(dto);
  }
}
