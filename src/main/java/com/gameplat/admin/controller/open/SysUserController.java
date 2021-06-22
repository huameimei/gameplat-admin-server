package com.gameplat.admin.controller.open;

import cn.hutool.http.HttpUtil;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.enums.limit.TrueFalseEnum;
import com.gameplat.admin.interceptor.Session;
import com.gameplat.admin.model.bean.AdminRedisBean;
import com.gameplat.admin.model.dto.AdminLoginDto;
import com.gameplat.admin.model.bean.TokenInfo;
import com.gameplat.admin.model.entity.GoogleConfig;
import com.gameplat.admin.model.entity.SysUser;
import com.gameplat.admin.model.vo.SysUserVo;
import com.gameplat.admin.model.vo.UserEquipmentVO;
import com.gameplat.admin.model.bean.AdminLoginLimit;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.utils.HttpUtils;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.GoogleAuth;
import com.gameplat.common.web.Result;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** 管理员 */
@Slf4j
@RestController
@RequestMapping("/gameplat-admin-service/api/open/user")
public class SysUserController {

  @Autowired
  private SysDictDataService sysDictDataService;

  @Autowired
  private SysAuthIpService sysAuthIpService;

  @Autowired
  private SysUserService sysUserService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @ResponseBody
  public Result getUserTokenInfo(AdminLoginDto adminLoginDto,
      HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "userAgent", required = false) String userAgentString, UserAgent clientUserAgent)
      throws ServiceException {
    String requestIp = HttpUtil.getClientIP(request);
    if (StringUtils.isBlank(userAgentString)) {
        userAgentString = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("");
    }
    if (StringUtil.isBlank(adminLoginDto.getAccount())) {
      return Result.failed("账号不能为空");
    }
    if (StringUtil.isBlank(adminLoginDto.getPassword())) {
      return Result.failed( "密码不能为空");
    }

    // 获取管理员登录限制信息
    AdminLoginLimit limit = sysDictDataService.getDictData(DictTypeEnum.ADMIN_LOGIN_CONFIG.getValue(), AdminLoginLimit.class);
    if (limit == null){
      return Result.failed("登录配置信息未配置");
    }
    if (Objects.equals(TrueFalseEnum.YES.getValue(), limit.getVCode())) {
      HttpSession session = request.getSession();
      String checkCode = (String) session.getAttribute(Constants.CHECKCODE);
      session.removeAttribute(Constants.CHECKCODE);
      if (StringUtil.isBlank(adminLoginDto.getValiCode()) || !adminLoginDto.getValiCode().equalsIgnoreCase(checkCode)) {
        return Result.failed("验证码不一致");
      }
    }
    /**
     * 是否开启谷歌检验码
     */
    if(Objects.equals(TrueFalseEnum.YES.getValue(),limit.getOpenGoogleAuth())){
      // 获取谷歌验证器密钥
      GoogleConfig googleConfig = sysDictDataService.getDictData(DictTypeEnum.GOOGLE_CONFIG.getValue(),
          GoogleConfig.class);
      String secret = googleConfig.getGoogleAuthSecret();
      if (StringUtils.isEmpty(secret)) {
        return Result.failed("谷歌验证器密钥获取失败");
      }
      if (Boolean.FALSE.equals(GoogleAuth.authcode(adminLoginDto.getGoogleCode(), secret))) {
        return Result.failed("身份验证码错误");
      }
    }
    /**
     * 是否开启后台白名单
     */
    if(Objects.equals(TrueFalseEnum.YES.getValue(),limit.getOpenIpWhiteList())){
      if (!sysAuthIpService.isPermitted(requestIp)) {
        return Result.failed("当前IP不允许登录：" + requestIp);
      }
    }
    UserEquipmentVO equipment = UserEquipmentVO.create(userAgentString, clientUserAgent, request);
    TokenInfo loginAppUser;
    try {
        loginAppUser = sysUserService.login(adminLoginDto.getAccount(), adminLoginDto.getPassword(), requestIp, equipment, userAgentString);
        HttpUtils.removeCookie(request,response,Constants.ADMIN_TOKEN_NAME);
        HttpUtils.setSessionCookieNotDomain(request, response, Constants.ADMIN_TOKEN_NAME,loginAppUser.getToken());
    }catch (Exception e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }
    return Result.succeed();
  }


  /**
   * 当前登录用户信息
   */
  @RequestMapping(value = "/info", method = RequestMethod.GET)
  @ResponseBody
  public SysUserVo info(@Session Long adminId) throws Exception {
    if (adminId == null) {
      return null;
    }
    SysUserVo adminInfo = new SysUserVo();
    SysUser admin = this.sysUserService.getById(adminId);
    BeanUtils.copyProperties(adminInfo, admin);
    AdminRedisBean adminBean = sysUserService.getPrivilege(adminId);
    if (adminBean != null && adminBean.getRole() != null) {
      adminInfo.setRoleName(adminBean.getRole().getName());
    }
    return adminInfo;
  }


  @RequestMapping("/logout")
  @ResponseBody
  public void logout(@Session Long adminId, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    sysUserService.logout(adminId);
    HttpUtils.removeCookie(request, response, Constants.ADMIN_TOKEN_NAME);
  }

}
