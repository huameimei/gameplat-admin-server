package com.gameplat.admin.controller.open.setting;

import com.gameplat.admin.model.dto.SysUserAuthDto;
import com.gameplat.admin.model.vo.SysUserAuthVo;
import com.gameplat.admin.service.SysUserAuthService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author kb
 * @Date 2022/3/12 17:12
 * @Version 1.0
 */
@Api("用户认证设置")
@Slf4j
@RestController
@RequestMapping("/api/admin/userAuth")
public class SysUserAuthController {


    @Autowired(required = false)
    private SysUserAuthService sysUserAuthService;

    @ApiOperation("保存用户认证")
    @PostMapping("edit")
    @PreAuthorize("hasAuthority('system:userAuth:edit')")
    @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "设置认证信息")
    public void save(@RequestBody SysUserAuthDto dto) {
        sysUserAuthService.save(dto);
    }


    @ApiOperation("保存用户认证")
    @GetMapping("lsit")
    public List<SysUserAuthVo> lsit() {
        return sysUserAuthService.findAuth();
    }

}
