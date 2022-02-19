package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.VersionControl;
import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.admin.service.VersionControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/operator/package")
public class OperAppVersionController {


    private final VersionControlService sysService;

    /**
     * 获取发版信息列表
     */
    @GetMapping("/getVersionInfo")
    public IPage<VersionControl> packageInfo(PageDTO<VersionControl> page, VersionControlDTO dto){
        return sysService.getSysPackageInfo(page,dto);
    }


    /**
     * 新增发版
     */
    @PostMapping("/createVersionInfo")
    public boolean createPackageInfo(@RequestBody VersionControlDTO dto){
        return sysService.createSysPackageInfo(dto);
    }

    /**
     * 修改发版信息
     */
    @PostMapping("/editVersionInfo")
    public int editVersionInfo(@RequestBody VersionControlDTO dto){
        return sysService.editSysPackageInfo(dto);
    }

    /**
     * 删除发版信息
     */
    @DeleteMapping("/removeVersionInfo")
    public boolean removeVersionInfo(Integer id){
        return sysService.removeSysPackageInfo(id);
    }
}
