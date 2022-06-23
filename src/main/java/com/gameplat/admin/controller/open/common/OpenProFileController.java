package com.gameplat.admin.controller.open.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.dto.ChangePasswordDTO;
import com.gameplat.admin.model.dto.UserInfoDTO;
import com.gameplat.admin.model.vo.ProfileVO;
import com.gameplat.admin.service.PermissionService;
import com.gameplat.admin.service.SysLogService;
import com.gameplat.admin.service.UserCenterService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.dto.LogDTO;
import com.gameplat.common.model.vo.UserLogVO;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysMenu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 个人信息
 *
 * @author three
 */
@Tag(name = "个人信息")
@RestController
@RequestMapping("/api/admin/profile")
public class OpenProFileController {

  @Autowired private UserCenterService userCenterService;

  @Autowired private PermissionService permissionService;

  @Autowired private SysLogService logService;

  @Operation(summary = "获取用户信息")
  @GetMapping("/info")
  public ProfileVO userInfo(Authentication authentication) {
    return userCenterService.current(authentication.getName());
  }

  @Operation(summary = "保存用户个性配置")
  @PostMapping("/update")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改个人资料'")
  public void update(@Validated @RequestBody UserInfoDTO dto) {
    userCenterService.update(dto);
  }

  @Operation(summary = "系统菜单列表")
  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList(Authentication authentication) {
    return permissionService.getMenuList(authentication.getName());
  }

  @Operation(summary = "修改密码")
  @PostMapping("/changePassword")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改个人密码'")
  public void changePassword(String oldPassWord, String newPassWord) {
    if (StringUtils.isBlank(oldPassWord)) {
      throw new ServiceException("旧密码不能为空");
    }

    if (StringUtils.isBlank(newPassWord)) {
      throw new ServiceException("新密码不能为空");
    }

    ChangePasswordDTO changePassword = new ChangePasswordDTO();
    changePassword.setOldPassWord(oldPassWord);
    changePassword.setNewPassWord(newPassWord);
    userCenterService.changePassword(changePassword);
  }

  @Operation(summary = "获取日志")
  @GetMapping("/operLogList")
  public IPage<UserLogVO> operLogList(Authentication authentication, LogDTO logDTO) {
    logDTO.setUserName(authentication.getName());
    return logService.getCurrentOperList(logDTO);
  }
}
