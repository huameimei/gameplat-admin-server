package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.OperUserDTO;
import com.gameplat.admin.model.dto.UserDTO;
import com.gameplat.admin.model.vo.RoleVo;
import com.gameplat.admin.model.vo.UserVo;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 子账号管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/account/subUser")
public class OpenSubUserController {

  private final SysUserService userService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:subUser:view')")
  public IPage<UserVo> list(PageDTO<SysUser> page, UserDTO userDTO) {
    return userService.selectUserList(page, userDTO);
  }

  @PostMapping("/add")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加后台账号【'+#userDTO.account+'】'")
  @PreAuthorize("hasAuthority('account:subUser:add')")
  public void add(@RequestBody OperUserDTO userDTO) {
    if (StringUtils.isBlank(userDTO.getAccount())) {
      throw new ServiceException("账号不能为空");
    }
    if (StringUtils.isBlank(userDTO.getPassword())) {
      throw new ServiceException("密码不能为空");
    }
    if (StringUtils.isBlank(userDTO.getUserType())) {
      throw new ServiceException("账号类型不能为空");
    }
    userService.insertUser(userDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:subUser:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改后台账号【'+#userDTO.account+'】'")
  public void edit(@RequestBody OperUserDTO userDTO) {
    if (StringUtils.isBlank(userDTO.getAccount())) {
      throw new ServiceException("账号不能为空");
    }

    if (StringUtils.isBlank(userDTO.getUserType())) {
      throw new ServiceException("账号类型不能为空");
    }

    userService.updateUser(userDTO);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('account:subUser:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除后台账号id='+#ids")
  public void remove(@RequestBody String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("参数不全");
    }
    userService.deleteUserByIds(ids);
  }

  /**
   * 权限列表
   *
   * @return
   */
  @GetMapping("/roleList")
  public List<RoleVo> roleList() {
    return userService.getRoleList();
  }

  /**
   * 重置用户密码
   *
   * @param userDTO
   * @return
   */
  @PostMapping("/resetPassword")
  @PreAuthorize("hasAuthority('account:subUser:changePassword')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'重置后台账号密码,id='+#userDTO.id")
  public void resetPassword(OperUserDTO userDTO) {
    if (StringUtils.isNull(userDTO.getId())) {
      throw new ServiceException("缺少参数");
    }
    if (StringUtils.isBlank(userDTO.getPassword())) {
      throw new ServiceException("缺少参数");
    }
    userService.resetUserPassword(userDTO);
  }

  /**
   * 重置用户安全码
   *
   * @param userDTO
   * @return
   */
  @PostMapping("/resetSafeCode")
  @PreAuthorize("hasAuthority('account.subUser:restAuth')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'重置后台账号谷歌验证器,id='+#userDTO.id")
  public void resetAuth(OperUserDTO userDTO) {
    if (StringUtils.isNull(userDTO.getId())) {
      throw new ServiceException("缺少参数");
    }
    userService.resetGoogleSecret(userDTO);
  }

  @PostMapping("/changeStatus")
  @PreAuthorize("hasAuthority('account.subUser.enable')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改后台账号状态,id='+#userDTO.id +',状态='+#userDTO.status")
  public void changeStatus(OperUserDTO userDTO) {
    if (StringUtils.isNull(userDTO.getId())) {
      throw new ServiceException("缺少参数");
    }
    if (StringUtils.isNull(userDTO.getStatus())) {
      throw new ServiceException("缺少参数");
    }
    userService.changeStatus(userDTO);
  }

  @GetMapping("/checkUserNameUnique/{username}")
  public boolean checkUserNameUnique(@RequestParam String username) {
    return userService.checkLoginNameUnique(username);
  }
}
