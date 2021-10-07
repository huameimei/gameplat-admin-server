package com.gameplat.admin.controller.open;

import com.gameplat.admin.model.dto.SysPayBankAddDTO;
import com.gameplat.admin.model.dto.SysPayBankEditDTO;
import com.gameplat.admin.model.entity.SysPayBank;
import com.gameplat.admin.service.SysPayBankService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

@RestController
@RequestMapping(ServiceApi.API + "/sysPayBanks")
public class SysPayBankController {

    @Autowired
    private SysPayBankService sysPayBankService;

    @DeleteMapping("/remove")
    @Log(module = ADMIN_SERVICE, desc = "'后台管理删除银行'")
    public void remove(@RequestBody Long id) {
        sysPayBankService.delete(id);
    }

    @PostMapping("/list")
    public List<SysPayBank> findSysPayBanks() {
        return sysPayBankService.queryList();
    }

    @PostMapping("/add")
    @Log(module = ADMIN_SERVICE, desc = "'银行新增'")
    public void add(@RequestBody SysPayBankAddDTO dto) {
        sysPayBankService.save(dto);
    }

    @PostMapping("/edit")
    @Log(module = ADMIN_SERVICE, desc = "'银行更新'")
    public void edit(@RequestBody SysPayBankEditDTO dto) {
        sysPayBankService.update(dto);
    }

    @PostMapping("/editStatus")
    @Log(module = ADMIN_SERVICE, desc = "'银行状态更新'")
    public void updateStatus(Long id, Integer status) {
        sysPayBankService.updateStatus(id, status);
    }
}
