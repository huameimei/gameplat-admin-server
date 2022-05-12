package com.gameplat.admin.controller.open.system;

import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.model.entity.limit.LimitInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "限制信息配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/system/limit")
public class OpenLimitInfoController {

  @Autowired private LimitInfoService limitInfoService;

  @ApiOperation("添加/修改登录后台限制")
  @PostMapping(value = "/add/adminLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:add:adminLoginLimit')")
  public void saveAdminLoginLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("根据名称获取配置登录后台限制")
  @GetMapping(value = "/get/adminLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:view:adminLoginLimit')")
  public LimitInfo getAdminLoginLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @ApiOperation("添加/修改完善资料")
  @PostMapping(value = "/add/editUserInfoLimit")
  @PreAuthorize("hasAuthority('system:limit:addUserInfoLimit')")
  public void saveEditUserInfoLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("根据名称获取配置完善资料")
  @GetMapping(value = "/get/editUserInfoLimit")
  @PreAuthorize("hasAuthority('system:limit:viewUserInfoLimit')")
  public LimitInfo getEditUserInfoLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @ApiOperation("添加/修改游戏额度转换限制")
  @PostMapping(value = "/add/liveTransferLimit")
  @PreAuthorize("hasAuthority('system:limit:addTransferLimit')")
  public void saveLiveTransferLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("根据名称获取游戏额度转换限制")
  @GetMapping(value = "/get/liveTransferLimit")
  @PreAuthorize("hasAuthority('system:limit:viewTransferLimit')")
  public LimitInfo getLiveTransferLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @ApiOperation("添加/修改会员登录限制")
  @PostMapping(value = "/add/memberLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:addLoginLimit')")
  public void saveMemberLoginLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("获取会员登录限制")
  @GetMapping(value = "/get/memberLoginLimit")
  @PreAuthorize("hasAuthority('system:limit:viewLoginLimit')")
  public LimitInfo getMemberLoginLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @ApiOperation("添加/修改会员充值限制")
  @PostMapping(value = "/add/memberRechargeLimit")
  @PreAuthorize("hasAuthority('system:limit:addRechargeLimit')")
  public void saveMemberRechargeLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("获取会员充值限制")
  @GetMapping(value = "/get/memberRechargeLimit")
  @PreAuthorize("hasAuthority('system:limit:viewRechargeLimit')")
  public LimitInfo getMemberRechargeLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @ApiOperation("添加/修改会员注册限制")
  @PostMapping(value = "/add/memberRegistryLimit")
  @PreAuthorize("hasAuthority('system:limit:addRegistryLimit')")
  public void saveMemberRegistryLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("获取会员注册限制")
  @GetMapping(value = "/get/memberRegistryLimit")
  @PreAuthorize("hasAuthority('system:limit:viewRegistryLimit')")
  public LimitInfo getMemberRegistryLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }

  @ApiOperation("添加/修改会员提现限制")
  @PostMapping(value = "/add/memberWithdrawLimit")
  @PreAuthorize("hasAuthority('system:limit:addWithdrawLimit')")
  public void saveMemberWithdrawLimit(@RequestBody LimitInfoDTO dto) {
    limitInfoService.insertLimitInfo(dto);
  }

  @ApiOperation("获取会员提现限制")
  @GetMapping(value = "/get/memberWithdrawLimit")
  @PreAuthorize("hasAuthority('system:limit:viewWithdrawLimit')")
  public LimitInfo getMemberWithdrawLimit(String name) {
    return limitInfoService.getLimitInfo(name);
  }
}
