package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ChatGifEditDTO;
import com.gameplat.admin.model.vo.ChatGifVO;
import com.gameplat.admin.service.ChatGifService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.chart.ChatGif;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lily
 * @description
 * @date 2022/2/13
 */
@Tag(name = "聊天室Gif管理")
@RestController
@RequestMapping("/api/admin/chat/gif")
public class ChatGifController {

  @Autowired private ChatGifService chatGifService;

  @Operation(summary = "分页列表")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('chat:gif:view')")
  public IPage<ChatGifVO> page(PageDTO<ChatGif> page, String name) {
    return chatGifService.page(page, name);
  }

  @Operation(summary = "增/上传图片")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('chat:gif:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室Gif管理->增/上传图片:' + #name")
  public void add(@RequestPart MultipartFile file, String name) throws Exception {
    chatGifService.add(file, name);
  }

  @Operation(summary = "上传图片")
  @PostMapping("/upload")
  @PreAuthorize("hasAuthority('chat:gif:upload')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "聊天室Gif管理->上传图片")
  public String upload(@RequestPart MultipartFile file) throws Exception {
    return chatGifService.upload(file, null);
  }

  @Operation(summary = "删")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('chat:gif:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室Gif管理->删id:' + #id")
  public void remove(@PathVariable Integer id) {
    chatGifService.remove(id);
  }

  @Operation(summary = "改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('chat:gif:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室Gif管理->改:' + #dto")
  public void edit(ChatGifEditDTO dto) {
    chatGifService.edit(dto);
  }
}
