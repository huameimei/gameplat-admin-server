package com.gameplat.admin.controller.open.account;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.OperUserDTO;
import com.gameplat.admin.model.dto.UserDTO;
import com.gameplat.admin.model.dto.UserResetPasswordDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.model.vo.UserVo;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "子账号管理")
@RestController
@RequestMapping("/api/admin/account/subUser")
@Log4j2
public class OpenSysUserController {

  private static final String KEY_2FA_RETRY_COUNT = "KEY_2FA_RETRY_COUNT";

  @Autowired private SysUserService userService;

  @Autowired(required = false)
  private RedisTemplate<String, Integer> redisTemplate;


  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:subUser:view')")
  public IPage<UserVo> list(PageDTO<SysUser> page, UserDTO dto) {
    if (ObjectUtil.isNotEmpty(dto.getBeginTime())) {
      dto.setBeginTime(dto.getBeginTime() + DateUtil.TIME_START);
    }
    if (ObjectUtil.isNotEmpty(dto.getEndTime())) {
      dto.setEndTime(dto.getEndTime() + DateUtil.TIME_END);
    }
    return userService.selectUserList(page, dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加后台账号【'+#dto.account+'】'")
  @PreAuthorize("hasAuthority('account:subUser:add')")
  public void add(@Validated(Groups.INSERT.class) @RequestBody OperUserDTO dto) {
    userService.insertUser(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:subUser:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改后台账号【'+#dto.account+'】'")
  public void edit(@Validated(Groups.UPDATE.class) @RequestBody OperUserDTO dto) {
    userService.updateUser(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('account:subUser:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除后台账号id='+#id")
  public void remove(@PathVariable Long id) {
    userService.deleteUserById(id);
  }

  @Operation(summary = "获取角色列表")
  @GetMapping("/roleList")
  public List<RoleVo> roleList() {
    return userService.getRoleList();
  }

  @Operation(summary = "重置密码")
  @PostMapping("/resetPassword")
  @PreAuthorize("hasAuthority('account:subUser:changePassword')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'重置后台账号密码,id='+#dto.id")
  public void resetPassword(@Validated @RequestBody UserResetPasswordDTO dto) {
    userService.resetUserPassword(dto);
  }

  @Operation(summary = "重置安全码")
  @PostMapping("/resetSafeCode/{id}")
  @PreAuthorize("hasAuthority('account.subUser:restAuth')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'重置谷歌验证器,id='+#id")
  public void resetAuth(@PathVariable Long id) {
    userService.resetGoogleSecret(id);
  }

  @Operation(summary = "修改状态")
  @PostMapping("/changeStatus/{id}/{status}")
  @PreAuthorize("hasAuthority('account.subUser.enable')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改后台账号状态,id='+#id +',状态='+#status")
  public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
    userService.changeStatus(id, status);
  }

  @Operation(summary = "解除登录密码限制")
  @PostMapping("releaseSysUserLimit/{id}")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.MEMBER, desc = "'解除登录密码限制'+#{id}")
  @PreAuthorize("hasAuthority('account:subUser:releaseSysUserLimit')")
  public void releaseWithLimit(@PathVariable(required = true) long id) {
    SysUser byId = userService.getById(id);
    String retryKey = String.format("%s_%s", KEY_2FA_RETRY_COUNT, id);
    log.info("操作人：{}", SecurityUserHolder.getUsername());
    boolean remove = redisTemplate.delete(retryKey);
    log.info("解除登录密码限制：{}", remove);
    String keyUsername = String.format(CachedKeys.ADMIN_PWD_ERROR_COUNT, byId.getUserName());
    boolean removeUsername = redisTemplate.delete(keyUsername);
    log.info("解除登录密码限制Username：{}", removeUsername);
  }
}
