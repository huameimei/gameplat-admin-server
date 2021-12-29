package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.IpStatisticsDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.service.MemberStatusService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lily
 * @description 会员vip统计
 * @date 2021/12/28
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/member/report")
public class MemberStatusController {

    @Autowired
    private MemberStatusService memberStatusService;

    @ApiOperation(value = "会员ip统计列表")
    @GetMapping("/ip_statistics")
    public List<IpStatisticsVO> findStatisticsList(IpStatisticsDTO ipStatisticsDTO){
        return memberStatusService.findStatisticsList(ipStatisticsDTO);
    }

    @ApiOperation(value = "ip分析列表")
    @GetMapping("/ip_analysis")
    public List<IpAnalysisVO> findIpAnalysisList(IpAnalysisDTO ipAnalysisDTO){
       return memberStatusService.findIpAnalysisList(ipAnalysisDTO);
    }
}
