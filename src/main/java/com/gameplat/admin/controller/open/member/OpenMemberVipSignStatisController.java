package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberVipSignHistory;
import com.gameplat.admin.model.domain.MemberVipSignStatis;
import com.gameplat.admin.model.dto.MemberVipSignHistoryDTO;
import com.gameplat.admin.model.dto.MemberVipSignStatisDTO;
import com.gameplat.admin.model.vo.MemberVipSignHistoryVO;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import com.gameplat.admin.service.MemberVipSignHistoryService;
import com.gameplat.admin.service.MemberVipSignStatisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @author lily
 * @description VIP会员签到汇总
 * @date 2021/11/24
 */

@Api(tags = "VIP会员签到汇总")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/sign")
public class OpenMemberVipSignStatisController {

@Autowired
private MemberVipSignStatisService signStatisService;
@Autowired
private MemberVipSignHistoryService memberVipSignHistoryService;

    @GetMapping("/list")
    @ApiOperation(value = "查询VIP会员签到记录列表")
    @PreAuthorize("hasAuthority('member:sign:list')")
    public IPage<MemberVipSignStatisVO> querySignList(PageDTO<MemberVipSignStatis> page, MemberVipSignStatisDTO dto) {
        return signStatisService.findSignList(page, dto);
    }

    @GetMapping("/exportSign")
    @ApiOperation(value = "导出VIP福利记录列表")
    @PreAuthorize("hasAuthority('member:sign:export')")
    public void exportSign(MemberVipSignStatisDTO queryDTO, HttpServletResponse response){
        signStatisService.exportSignStatis(queryDTO, response);
    }


    @GetMapping("/history")
    @ApiOperation(value = "会员签到记录")
    @PreAuthorize("hasAuthority('member:sign:history')")
    public IPage<MemberVipSignHistoryVO> getHistoryList(PageDTO<MemberVipSignHistory> page, MemberVipSignHistoryDTO dto) {
        return memberVipSignHistoryService.findPageList(page, dto);
    }

}
