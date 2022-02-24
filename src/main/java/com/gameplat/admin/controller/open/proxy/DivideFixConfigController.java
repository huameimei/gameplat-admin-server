package com.gameplat.admin.controller.open.proxy;

import com.gameplat.admin.service.DivideFixConfigService;
import com.gameplat.admin.service.DivideLayerConfigService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "固定比例分红模式")
@RestController
@RequestMapping("/api/admin/divide/fix")
@SuppressWarnings("all")
public class DivideFixConfigController {
    @Autowired
    private DivideFixConfigService fixConfigService;
}
