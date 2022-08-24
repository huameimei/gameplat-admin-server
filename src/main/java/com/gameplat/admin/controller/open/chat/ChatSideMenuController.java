package com.gameplat.admin.controller.open.chat;

import com.gameplat.admin.model.vo.ChatSideMenuVO;
import com.gameplat.admin.service.ChatSideMenuService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天室侧滑菜单管理
 *
 * @author lily
 * @date 2022/2/15
 */
@Tag(name = "聊天室侧滑菜单管理")
@RestController
@RequestMapping("/api/admin/chat/menu")
public class ChatSideMenuController {

  @Autowired private ChatSideMenuService chatSideMenuService;

  @Operation(summary = "聊天室侧滑菜单列表")
  @PreAuthorize("hasAuthority('chat:menu:view')")
  @GetMapping("/list")
  public List<ChatSideMenuVO> queryAllSideMenu() {
    return chatSideMenuService.queryAllSideMenu();
  }

  @Operation(summary = "编辑")
  @PreAuthorize("hasAuthority('chat:menu:edit')")
  @PostMapping("/edit")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室侧滑菜单管理->编辑:' + #config")
  public void edit(@RequestBody String config) {
    chatSideMenuService.edit(config);
  }
}
