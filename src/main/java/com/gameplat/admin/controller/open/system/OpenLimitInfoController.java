package com.gameplat.admin.controller.open.system;

import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.model.entity.limit.LimitInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/system/limit")
public class OpenLimitInfoController {

  @Autowired private LimitInfoService limitInfoService;

  /**
   * 登录后台限制添加/修改配置项
   */
  @PutMapping(value = "/add/adminLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:add:adminLoginLimit')")
  public void saveAdminLoginLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置登录后台限制
   */
  @GetMapping(value = "/get/adminLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:view:adminLoginLimit')")
  public LimitInfo getAdminLoginLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  /**
   * 完善资料添加/修改配置项
   */
  @PutMapping(value = "/add/editUserInfoLimit")
  @PreAuthorize("hasAuthority('system:limit:addUserInfoLimit')")
  public void saveEditUserInfoLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置完善资料
   */
  @GetMapping(value = "/get/editUserInfoLimit")
  @PreAuthorize("hasAuthority('system:limit:viewUserInfoLimit')")
  public LimitInfo getEditUserInfoLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  /**
   * 真人额度转换配置限制添加/修改配置项
   */
  @PutMapping(value = "/add/liveTransferLimit")
  @PreAuthorize("hasAuthority('system:limit:addTransferLimit')")
  public void saveLiveTransferLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置真人额度转换配置
   */
  @GetMapping(value = "/get/liveTransferLimit")
  @PreAuthorize("hasAuthority('system:limit:viewTransferLimit')")
  public LimitInfo getLiveTransferLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  /**
   * 会员登录配置限制添加/修改配置项
   */
  @PutMapping(value = "/add/memberLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:addLoginLimit')")
  public void saveMemberLoginLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置会员登录配置限制
   */
  @GetMapping(value = "/get/memberLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:viewLoginLimit')")
  public LimitInfo getMemberLoginLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  /**
   * 会员充值限制添加/修改配置项
   */
  @PutMapping(value = "/add/memberRechargeLimit")
  @PreAuthorize("hasAuthority('system:limit:addRechargeLimit')")
  public void saveMemberRechargeLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置会员充值限制
   */
  @GetMapping(value = "/get/memberRechargeLimit")
  @PreAuthorize("hasAuthority('system:limit:viewRechargeLimit')")
  public LimitInfo getMemberRechargeLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  /**
   * 会员注册配置限制添加/修改配置项
   */
  @PutMapping(value = "/add/memberRegistryLimit")
  @PreAuthorize("hasAuthority('system:limit:addRegistryLimit')")
  public void saveMemberRegistryLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置注册限制
   */
  @GetMapping(value = "/get/memberRegistryLimit")
  @PreAuthorize("hasAuthority('system:limit:viewRegistryLimit')")
  public LimitInfo getMemberRegistryLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  /**
   * 会员提现配置限制添加/修改配置项
   */
  @PutMapping(value = "/add/memberWithdrawLimit")
  @PreAuthorize("hasAuthority('system:limit:addWithdrawLimit')")
  public void saveMemberWithdrawLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  /**
   * 根据名称获取配置会员提现配置限制
   */
  @GetMapping(value = "/get/memberWithdrawLimit")
  @PreAuthorize("hasAuthority('system:limit:viewWithdrawLimit')")
  public LimitInfo getMemberWithdrawLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }
}
