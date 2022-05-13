package com.gameplat.admin.controller.open.chat;

import com.gameplat.admin.model.vo.ChatSideMenuVO;
import com.gameplat.admin.service.ChatSideMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "聊天室侧滑菜单管理")
@RestController
@RequestMapping("/api/admin/chat/menu")
public class ChatSideMenuController {

  @Autowired private ChatSideMenuService chatSideMenuService;

  @ApiOperation(value = "聊天室侧滑菜单列表")
  @PreAuthorize("hasAuthority('chat:menu:view')")
  @GetMapping("/list")
  public List<ChatSideMenuVO> queryAllSideMenu() {
    return chatSideMenuService.queryAllSideMenu();
  }

  @ApiOperation(value = "编辑")
  @PreAuthorize("hasAuthority('chat:menu:edit')")
  @PostMapping("/edit")
  public void edit(@RequestBody String config) {
    chatSideMenuService.edit(config);
  }
}
