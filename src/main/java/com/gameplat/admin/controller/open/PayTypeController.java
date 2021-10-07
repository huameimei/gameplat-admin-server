package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PayTypeAddDTO;
import com.gameplat.admin.model.dto.PayTypeEditDTO;
import com.gameplat.admin.model.entity.PayType;
import com.gameplat.admin.model.vo.PayTypeVO;
import com.gameplat.admin.service.PayTypeService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

@RestController
@RequestMapping(ServiceApi.API + "/payTypes")
public class PayTypeController {

    @Autowired
    private PayTypeService payTypeService;

    @DeleteMapping("/remove")
    @Log(module = ADMIN_SERVICE, desc = "'后台管理删除支付方式'")
    public void remove(@RequestBody Long id) {
        payTypeService.delete(id);
    }

    @PostMapping("/list")
    public List<PayTypeVO> findPayTypes() {
        return payTypeService.queryList();
    }

    @PostMapping("/add")
    @Log(module = ADMIN_SERVICE, desc = "'支付方式新增'")
    public void add(@RequestBody PayTypeAddDTO dto) {
        payTypeService.save(dto);
    }

    @PostMapping("/edit")
    @Log(module = ADMIN_SERVICE, desc = "'支付方式更新'")
    public void edit(@RequestBody PayTypeEditDTO dto) {
        payTypeService.update(dto);
    }

    @PostMapping("/page")
    public IPage<PayType> queryPage(Page<PayType> page) {
        return payTypeService.queryPage(page);
    }
}
