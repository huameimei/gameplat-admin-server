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
 * @author lily
 * @description 聊天室侧滑菜单管理
 * @date 2022/2/15
 */

@Api(tags = "聊天室侧滑菜单管理")
@RestController
@RequestMapping("/api/admin/chat/menu")
public class ChatSideMenuController {

    @Autowired
    private ChatSideMenuService chatSideMenuService;

    private final String lottUrl = "/api-manage/chatRoom/updateChatRoomStatus";

    @ApiOperation(value = "聊天室侧滑菜单列表")
    @PreAuthorize("hasAuthority('chat:menu:list')")
    @GetMapping("/list")
    public List<ChatSideMenuVO> queryAllSideMenu() {
        return chatSideMenuService.queryAllSideMenu();
    }

    @ApiOperation(value = "编辑")
    @PreAuthorize("hasAuthority('chat:menu:edit')")
    @PutMapping("/edit")
    public void edit(String config) {
        chatSideMenuService.edit(config);
    }

}
