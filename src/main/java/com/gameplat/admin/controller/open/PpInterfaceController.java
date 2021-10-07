package com.gameplat.admin.controller.open;

import com.gameplat.admin.model.entity.PpInterface;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ServiceApi.API + "/ppInterfaces")
public class PpInterfaceController {

    @Autowired
    private PpInterfaceService ppInterfaceService;

    @GetMapping("/queryAll")
    public List<PpInterfaceVO> getAll() {
        return ppInterfaceService.queryAll();
    }

    @GetMapping("/queryList")
    public List<PpInterface> getList() {
        return ppInterfaceService.list();
    }
}
