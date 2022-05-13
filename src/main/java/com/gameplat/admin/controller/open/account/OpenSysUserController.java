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
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "子账号管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/account/subUser")
public class OpenSysUserController {

  @Autowired private SysUserService userService;

  @ApiOperation("查询")
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

  @ApiOperation("添加")
  @PostMapping("/add")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加后台账号【'+#dto.account+'】'")
  @PreAuthorize("hasAuthority('account:subUser:add')")
  public void add(@Validated(Groups.INSERT.class) @RequestBody OperUserDTO dto) {
    userService.insertUser(dto);
  }

  @ApiOperation("编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:subUser:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改后台账号【'+#dto.account+'】'")
  public void edit(@Validated(Groups.UPDATE.class) @RequestBody OperUserDTO dto) {
    userService.updateUser(dto);
  }

  @ApiOperation("删除")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('account:subUser:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除后台账号id='+#id")
  public void remove(@PathVariable Long id) {
    userService.deleteUserById(id);
  }

  @ApiOperation("获取角色列表")
  @GetMapping("/roleList")
  public List<RoleVo> roleList() {
    return userService.getRoleList();
  }

  @ApiOperation("重置密码")
  @PostMapping("/resetPassword")
  @PreAuthorize("hasAuthority('account:subUser:changePassword')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'重置后台账号密码,id='+#dto.id")
  public void resetPassword(@Validated @RequestBody UserResetPasswordDTO dto) {
    userService.resetUserPassword(dto);
  }

  @ApiOperation("重置安全码")
  @PostMapping("/resetSafeCode/{id}")
  @PreAuthorize("hasAuthority('account.subUser:restAuth')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'重置谷歌验证器,id='+#id")
  public void resetAuth(@PathVariable Long id) {
    userService.resetGoogleSecret(id);
  }

  @ApiOperation("修改状态")
  @PostMapping("/changeStatus/{id}/{status}")
  @PreAuthorize("hasAuthority('account.subUser.enable')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改后台账号状态,id='+#id +',状态='+#status")
  public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
    userService.changeStatus(id, status);
  }
}
