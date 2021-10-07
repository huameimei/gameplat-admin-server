package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PayAccountAddDTO;
import com.gameplat.admin.model.dto.PayAccountEditDTO;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.entity.PayAccount;
import com.gameplat.admin.model.vo.PayAccountVO;
import com.gameplat.admin.service.PayAccountService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

@RestController
@RequestMapping(ServiceApi.API + "/payAccounts")
public class PayAccountController {

    @Autowired
    private PayAccountService payAccountService;

    @DeleteMapping("/remove")
    @Log(module = ADMIN_SERVICE, desc = "'后台管理删除收款账户'")
    public void remove(@RequestBody Long id) {
        payAccountService.delete(id);
    }

    @PostMapping("/add")
    @Log(module = ADMIN_SERVICE, desc = "'收款账户新增'")
    public void add(@RequestBody PayAccountAddDTO dto) {
        payAccountService.save(dto);
    }

    @PostMapping("/edit")
    @Log(module = ADMIN_SERVICE, desc = "'收款账户更新'")
    public void edit(@RequestBody PayAccountEditDTO dto) {
        payAccountService.update(dto);
    }

    @PostMapping("/editStatus")
    @Log(module = ADMIN_SERVICE, desc = "'收款账户更新状态'")
    public void updateStatus(Long id, Integer status) {
        payAccountService.updateStatus(id, status);
    }

    @PostMapping("/page")
    public IPage<PayAccountVO> queryPage(Page<PayAccountVO> page, PayAccountQueryDTO dto) {
        return payAccountService.findPayAccountPage(page, dto);
    }

    @GetMapping("/get")
    public PayAccount get(Long id) {
        return payAccountService.getById(id);
    }
}
