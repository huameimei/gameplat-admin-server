package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.MemberDeviceService;
import com.gameplat.model.entity.member.MemberDevice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lily
 * @description 会员设备管理
 * @date 2022/1/8
 */
@Api(tags = "会员设备管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/device")
public class MemberDeviceController {

  @Autowired
  private MemberDeviceService memberDeviceService;

  @ApiOperation("查找设备号集合")
  @GetMapping(value = "/findList")
  @PreAuthorize("hasAuthority('member:device:view')")
  public IPage<MemberDeviceVO> findList(@Validated Page<MemberDevice> page, MemberDeviceQueryDTO queryDTO) {
    return memberDeviceService.findList(page, queryDTO);
  }

}
