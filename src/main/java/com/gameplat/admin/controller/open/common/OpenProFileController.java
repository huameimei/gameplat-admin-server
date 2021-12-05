package com.gameplat.admin.controller.open.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.dto.ChangePasswordDTO;
import com.gameplat.admin.model.dto.UserSettingDTO;
import com.gameplat.admin.model.vo.ProFileVo;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 个人信息
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/proFile")
public class OpenProFileController {

  @Autowired private UserCenterService userCenterService;

  @Autowired private PermissionService permissionService;

  @Autowired private SysLogService logService;

  /**
   * 取用户信息
   *
   * @return ProFileVo
   */
  @GetMapping("/info")
  public ProFileVo userInfo(Authentication authentication) {
    return userCenterService.current(authentication.getName());
  }

  /**
   * 保存用户个性配置
   *
   * @return
   */
  @PostMapping("/setting")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改个人资料'")
  public void userSetting(UserSettingDTO settingDTO) {
    if (StringUtils.isNull(settingDTO.getIndexUrl())) {
      throw new ServiceException("缺少用户页参数");
    }
    if (StringUtils.isNull(settingDTO.getDefaultPageSize())) {
      throw new ServiceException("缺少用户默认页参数");
    }
    if (StringUtils.isNull(settingDTO.getReceiptOrder())) {
      throw new ServiceException("缺少充值订单排序参数");
    }
    if (StringUtils.isNull(settingDTO.getWithdrawOrder())) {
      throw new ServiceException("缺少提现订单排序参数");
    }
    if (StringUtils.isNull(settingDTO.getThousandsSeparator())) {
      throw new ServiceException("缺少金额千分符参数");
    }
    if (StringUtils.isNull(settingDTO.getFractionCount())) {
      throw new ServiceException("缺少金额精度参数");
    }
    if (StringUtils.isNull(settingDTO.getOpenDefaultNavMenu())) {
      throw new ServiceException("缺少切换导航时自动打开菜单参数");
    }
    userCenterService.saveUserSetting(settingDTO);
  }

  /**
   * 系统菜单列表
   *
   * @return
   */
  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList(Authentication authentication) {
    return permissionService.getMenuList(authentication.getName());
  }

  /**
   * 修改密码
   *
   * @param oldPassWord
   * @param newPassWord
   * @return
   */
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

  @GetMapping("/operLogList")
  public IPage<UserLogVO> operLogList(Authentication authentication, LogDTO logDTO) {
    logDTO.setUserName(authentication.getName());
    return logService.getCurrentOperList(logDTO);
  }
}
