package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.SysAuthIpAddDTO;
import com.gameplat.admin.model.dto.SysAuthIpQueryDTO;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVO;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ServiceApi.API + "/authIp")
public class SysAuthIpController {

    @Autowired
    private SysAuthIpService sysAuthIpService;

    /**
     * 查询所有IP白名单
     */
    @GetMapping(value = "/queryAll")
    public IPage<SysAuthIpVO> queryAll(Page<SysAuthIp> sysAuthIp, SysAuthIpQueryDTO queryDto) {
        return sysAuthIpService.queryPage(sysAuthIp, queryDto);
    }

    @PostMapping(value = "/save")
    public void save(SysAuthIpAddDTO sysAuthIpAddDto) {
        sysAuthIpService.save(sysAuthIpAddDto);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        sysAuthIpService.delete(id);
    }
}
