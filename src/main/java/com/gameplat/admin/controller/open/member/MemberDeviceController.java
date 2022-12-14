package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.MemberDeviceQueryDTO;
import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.admin.service.MemberDeviceService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.MemberDevice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员设备管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/device")
public class MemberDeviceController {

  @Autowired private MemberDeviceService memberDeviceService;

  @Operation(summary = "获取用户登录设备")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('member:device:view')")
  public IPage<MemberDeviceVO> page(@Validated Page<MemberDevice> page, MemberDeviceQueryDTO dto) {
    return memberDeviceService.page(page, dto);
  }

  @Operation(summary = "删除设备")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('member:device:delete')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'会员设备管理-->删除设备' + #id" )
  public void delete(@PathVariable Long id) {
    memberDeviceService.delete(id);
  }
}
