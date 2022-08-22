package com.gameplat.admin.controller.open.system;

import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.limit.LimitInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "限制信息配置")
@RestController
@RequestMapping("/api/admin/system/limit")
public class OpenLimitInfoController {

  @Autowired private LimitInfoService limitInfoService;

  @Operation(summary = "添加/修改登录后台限制")
  @PostMapping(value = "/add/adminLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:add:adminLoginLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改登录后台限制:' + #dto")
  public void saveAdminLoginLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "根据名称获取配置登录后台限制")
  @GetMapping(value = "/get/adminLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:view:adminLoginLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->根据名称获取配置登录后台限制:' + #name")
  public LimitInfo getAdminLoginLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改完善资料")
  @PostMapping(value = "/add/editUserInfoLimit")
  @PreAuthorize("hasAuthority('system:limit:addUserInfoLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改完善资料:' + #dto")
  public void saveEditUserInfoLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "根据名称获取配置完善资料")
  @GetMapping(value = "/get/editUserInfoLimit")
  @PreAuthorize("hasAuthority('system:limit:viewUserInfoLimit')")
  public LimitInfo getEditUserInfoLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改游戏额度转换限制")
  @PostMapping(value = "/add/liveTransferLimit")
  @PreAuthorize("hasAuthority('system:limit:addTransferLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改游戏额度转换限制:' + #dto")
  public void saveLiveTransferLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "根据名称获取游戏额度转换限制")
  @GetMapping(value = "/get/liveTransferLimit")
  @PreAuthorize("hasAuthority('system:limit:viewTransferLimit')")
  public LimitInfo getLiveTransferLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改会员登录限制")
  @PostMapping(value = "/add/memberLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:addLoginLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改会员登录限制:' + #dto")
  public void saveMemberLoginLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "获取会员登录限制")
  @GetMapping(value = "/get/memberLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:viewLoginLimit')")
  public LimitInfo getMemberLoginLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改会员充值限制")
  @PostMapping(value = "/add/memberRechargeLimit")
  @PreAuthorize("hasAuthority('system:limit:addRechargeLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改会员充值限制:' + #dto")
  public void saveMemberRechargeLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "获取会员充值限制")
  @GetMapping(value = "/get/memberRechargeLimit")
  //@PreAuthorize("hasAuthority('system:limit:viewRechargeLimit')")
  public LimitInfo getMemberRechargeLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改会员注册限制")
  @PostMapping(value = "/add/memberRegistryLimit")
  @PreAuthorize("hasAuthority('system:limit:addRegistryLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改会员注册限制:' + #dto")
  public void saveMemberRegistryLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "获取会员注册限制")
  @GetMapping(value = "/get/memberRegistryLimit")
  @PreAuthorize("hasAuthority('system:limit:viewRegistryLimit')")
  public LimitInfo getMemberRegistryLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改会员提现限制")
  @PostMapping(value = "/add/memberWithdrawLimit")
  @PreAuthorize("hasAuthority('system:limit:addWithdrawLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改会员提现限制:' + #dto")
  public void saveMemberWithdrawLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "获取会员提现限制")
  @GetMapping(value = "/get/memberWithdrawLimit")
  //@PreAuthorize("hasAuthority('system:limit:viewWithdrawLimit')")
  public LimitInfo getMemberWithdrawLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @Operation(summary = "添加/修改余额宝限制")
  @PostMapping(value = "/add/yubaoLimit")
  @PreAuthorize("hasAuthority('system:limit:addYubaoLimit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'限制信息配置-->添加/修改余额宝限制:' + #dto")
  public void saveYubaoLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @Operation(summary = "获取余额宝限制")
  @GetMapping(value = "/get/yubaoLimit")
  @PreAuthorize("hasAuthority('system:limit:viewYubaoLimit')")
  public LimitInfo getYubaoLimit() {
    return limitInfoService.getLimitInfo("yubaoLimit");
  }
}
