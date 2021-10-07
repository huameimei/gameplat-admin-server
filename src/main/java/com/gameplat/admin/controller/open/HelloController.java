package com.gameplat.admin.controller.open;

import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

/**
 * @Description:
 * @Author: Hoover
 * @Date: 6/10/2021 下午 2:49
 **/
@RestController
@RequestMapping(ServiceApi.API + "/hello")
public class HelloController {

    //   /api/hello/hi
    @GetMapping("/hi")
    @Log(module = ADMIN_SERVICE, desc = "'Say hi，你好'")
    public void remove(String s) {
        System.out.println("=======================> " + s);
    }

}
