package com.gameplat.admin.controller.open.report;

import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description ip分析
 * @date 2022/1/18
 */
@Slf4j
@Api(tags = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/ip")
public class IpAnalysisController {

    @Autowired
    private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

}
