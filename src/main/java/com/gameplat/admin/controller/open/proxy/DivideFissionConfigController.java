package com.gameplat.admin.controller.open.proxy;

import com.gameplat.admin.service.DivideFissionConfigService;
import com.gameplat.admin.service.DivideFixConfigService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "裂变分红模式")
@RestController
@RequestMapping("/api/admin/divide/fission")
@SuppressWarnings("all")
public class DivideFissionConfigController {
    @Autowired
    private DivideFissionConfigService fissionConfigService;
}
