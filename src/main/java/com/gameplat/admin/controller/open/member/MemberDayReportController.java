package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.mapper.MemberBusDayReportMapper;
import com.gameplat.admin.model.dto.DayReportDTO;
import com.gameplat.admin.model.vo.DayReportVO;
import com.gameplat.admin.service.MemberGrowthRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lily
 * @description 会员日报表
 * @date 2022/1/8
 */

@Api(tags = "会员日报表")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/dayReport")
public class MemberDayReportController {


    @Autowired
    private MemberBusDayReportMapper memberBusDayReportMapper;

    @GetMapping("/list")
    @ApiOperation(value = "查询会员日报列表")
    @PreAuthorize("hasAuthority('member:dayReport:list')")
    public List<DayReportVO> findList(DayReportDTO dto){
        return memberBusDayReportMapper.findList(dto);
    }


}
