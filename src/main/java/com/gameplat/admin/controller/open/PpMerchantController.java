package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PpMerchantAddDTO;
import com.gameplat.admin.model.dto.PpMerchantEditDTO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.service.PpMerchantService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

@RestController
@RequestMapping(ServiceApi.API + "/ppMerchants")
public class PpMerchantController {

    @Autowired
    private PpMerchantService ppMerchantService;

    @DeleteMapping("/remove")
    @Log(module = ADMIN_SERVICE, desc = "'后台管理删除商户'")
    public void remove(@RequestBody Long id) {
        ppMerchantService.delete(id);
    }

    @GetMapping("/queryMerchant")
    public PpMerchantVO getPpMerchantById(Long id) {
        return ppMerchantService.getPpMerchantById(id);
    }

    @PostMapping("/add")
    @Log(module = ADMIN_SERVICE, desc = "'商户新增'")
    public void add(@RequestBody PpMerchantAddDTO dto) {
        ppMerchantService.save(dto);
    }

    @PostMapping("/edit")
    @Log(module = ADMIN_SERVICE, desc = "'商户更新'")
    public void edit(@RequestBody PpMerchantEditDTO dto) {
        ppMerchantService.update(dto);
    }

    @PostMapping("/editStatus")
    @Log(module = ADMIN_SERVICE, desc = "'商户更新状态'")
    public void updateStatus(Long id, Integer status) {
        ppMerchantService.updateStatus(id, status);
    }

    @PostMapping("/page")
    public IPage<PpMerchantVO> getPage(Page<PpMerchantVO> page, Integer status, String name) {
        return ppMerchantService.queryPage(page, status, name);
    }

    @GetMapping("/queryAllMerchant")
    public List<PpMerchantVO> getAllMerchant() {
        Integer status = 0; // 获取可用商户
        return ppMerchantService.queryAllMerchant(status);
    }
}
