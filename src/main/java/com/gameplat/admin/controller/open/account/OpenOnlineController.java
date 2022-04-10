package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线会员管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/account/online")
public class OpenOnlineController {

  @Autowired private OnlineUserService onlineUserService;

  @Autowired
  private GameAdminService gameAdminService;

  @Autowired
  private MemberService memberService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:online:view')")
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

  /**
   * 查询会员总余额
   *
   * @param account
   * @return
   */
  @GetMapping("/allBalance/{account}")
  @PreAuthorize("hasAuthority('account:online:balance')")
  public Result allBalance(@PathVariable(value = "account", required = true) String account) {
    Assert.notNull(account, "请输入会员！");
    gameAdminService.recyclingAmountByAccount(account);
    MemberInfoVO memberInfo = memberService.getMemberInfo(account);
    Assert.notNull(memberInfo, "会员不存在！");
    Map<String, BigDecimal> map = new HashMap<>();
    map.put("allBalance", memberInfo.getBalance());
    return Result.succeed(map);
  }
}
