package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberGrowthStatis;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.admin.service.MemberGrowthStatisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description vip等级汇总
 * @date 2021/11/24
 */

@Api(tags = "vip等级汇总")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthStatis")
public class OpenMemberGrowthStatisController {

    @Autowired private MemberGrowthStatisService statisService;

    @GetMapping("/list")
    @ApiOperation(value = "查询vip等级汇总列表")
    @PreAuthorize("hasAuthority('member:growthStatis:list')")
    public IPage<MemberGrowthStatisVO> getGrowthRecordList(PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto) {
        return statisService.findStatisList(page, dto);
    }
}
