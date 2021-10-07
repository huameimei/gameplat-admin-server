package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.TpMerchantAddDTO;
import com.gameplat.admin.model.dto.TpMerchantEditDTO;
import com.gameplat.admin.model.vo.TpMerchantPayTypeVO;
import com.gameplat.admin.model.vo.TpMerchantVO;
import com.gameplat.admin.service.TpMerchantService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

@RestController
@RequestMapping(ServiceApi.API + "/tpMerchants")
public class TpMerchantController {

    @Autowired
    private TpMerchantService tpMerchantService;

    @DeleteMapping("/remove")
    @Log(module = ADMIN_SERVICE, desc = "'后台管理删除商户'")
    public void remove(@RequestBody Long id) {
        tpMerchantService.delete(id);
    }

    @GetMapping("/queryMerchant")
    public TpMerchantPayTypeVO getTpMerchantById(Long id) {
        return tpMerchantService.getTpMerchantById(id);
    }

    @PostMapping("/add")
    @Log(module = ADMIN_SERVICE, desc = "'商户新增'")
    public void add(@RequestBody TpMerchantAddDTO dto) {
        tpMerchantService.save(dto);
    }

    @PostMapping("/edit")
    @Log(module = ADMIN_SERVICE, desc = "'商户更新'")
    public void edit(@RequestBody TpMerchantEditDTO dto) {
        tpMerchantService.update(dto);
    }

    @PostMapping("/editStatus")
    @Log(module = ADMIN_SERVICE, desc = "'商户更新状态'")
    public void updateStatus(Long id, Integer status) {
        tpMerchantService.updateStatus(id, status);
    }

    @PostMapping("/page")
    public IPage<TpMerchantVO> getPage(Page<TpMerchantVO> page, Integer status, String name) {
        return tpMerchantService.queryPage(page, status, name);
    }

    @GetMapping("/queryAllMerchant")
    public List<TpMerchantVO> getAllMerchant() {
        Integer status = 0; // 获取可用商户
        return tpMerchantService.queryAllMerchant(status);
    }
}
