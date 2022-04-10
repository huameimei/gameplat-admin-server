package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.mapper.MemberGrowthStatisMapper;
import com.gameplat.admin.model.dto.MemberGrowthStatisDTO;
import com.gameplat.admin.model.vo.MemberGrowthStatisVO;
import com.gameplat.admin.service.MemberGrowthStatisService;
import com.gameplat.model.entity.member.MemberGrowthStatis;
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
 * @description
 * @date 2022/3/25
 */

@Api(tags = "VIP成长值汇总数据")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthStatis")
public class MemberGrowthStatisController {

    @Autowired
    private MemberGrowthStatisService memberGrowthStatisService;

    @Autowired
    private MemberGrowthStatisMapper mapper;


    @ApiOperation(value = "查询")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('member:growthStatis:view')")
    public IPage<MemberGrowthStatisVO> page(PageDTO<MemberGrowthStatis> page, MemberGrowthStatisDTO dto) {
        return memberGrowthStatisService.findStatisList(page, dto);
    }
}
