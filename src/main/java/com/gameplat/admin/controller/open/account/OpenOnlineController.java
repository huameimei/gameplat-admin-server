package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 在线会员管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/account/online")
public class OpenOnlineController {

  private final OnlineUserService onlineUserService;

  @GetMapping("/list")
  public IPage<OnlineUserVo> onlineList(PageDTO<OnlineUserVo> page, OnlineUserDTO dto) {
    return onlineUserService.selectOnlineList(page, dto);
  }

  @PutMapping("/kick/{username}/{uuid}")
  @PreAuthorize("hasAuthority('account:online:kick')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'将【'+#username+'】踢下线' ")
  public void kick(@PathVariable String uuid, @PathVariable String username) {
    onlineUserService.kick(uuid);
  }

  @PutMapping("/kickAll")
  @PreAuthorize("hasAuthority('account:online:kickAll')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "踢出所有在线账号")
  public void kickAll() {
    onlineUserService.kickAll();
  }
}
