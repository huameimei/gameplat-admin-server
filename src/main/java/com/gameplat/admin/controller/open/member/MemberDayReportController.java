package com.gameplat.admin.controller.open.member;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.mapper.MemberBusDayReportMapper;
import com.gameplat.admin.model.dto.DayReportDTO;
import com.gameplat.admin.model.dto.MemberReportDto;
import com.gameplat.admin.model.vo.DayReportVO;
import com.gameplat.admin.model.vo.MemberGameDayReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author lily
 * @description 会员日报表
 * @date 2022/1/8
 */

@Api(tags = "会员日报表")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/member/dayReport")
public class MemberDayReportController {


    @Autowired(required = false)
    private MemberBusDayReportMapper memberBusDayReportMapper;

    private final GameMemberReportService gameMemberReportService;

    @GetMapping("/list")
    @ApiOperation(value = "查询会员日报列表")
    @PreAuthorize("hasAuthority('member:dayReport:list')")
    public List<DayReportVO> findList(DayReportDTO dto){
        return memberBusDayReportMapper.findList(dto);
    }





    @ApiOperation(value = "会员日报表")
    @GetMapping(value = "memberGameDayReport")
    public PageDtoVO<MemberGameDayReportVo> queryBetReport(Page<MemberGameDayReportVo> page, MemberReportDto dto) {
        log.info("会员日报表入参：{}", JSON.toJSONString(dto));
        if (StringUtils.isEmpty(dto.getStartTime())) {
            String beginTime = DateUtil.getDateToString(new Date());
            dto.setStartTime(beginTime);
        }
        if (StringUtils.isEmpty(dto.getEndTime())) {
            String endTime = DateUtil.getDateToString(new Date());
            dto.setEndTime(endTime);
        }
        return gameMemberReportService.findSumMemberGameDayReport(page,dto);
    }


}
