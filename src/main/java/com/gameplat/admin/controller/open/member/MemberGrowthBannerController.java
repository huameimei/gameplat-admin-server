package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberGrowthBanner;
import com.gameplat.admin.model.dto.MemberGrowthBannerAddDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerEditDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerQueryDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import com.gameplat.admin.service.MemberGrowthBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author lily
 * @description VIP轮播图配置
 * @date 2022/1/14
 */

@Api(tags = "VIP轮播图配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthBanner")
public class MemberGrowthBannerController {

    @Autowired
    private MemberGrowthBannerService memberGrowthBannerService;

    /** 增 */
    @PostMapping("/add")
    @ApiOperation(value = "新增banner图")
    @PreAuthorize("hasAuthority('member:growthBanner:add')")
    public void addBanner(@Validated MemberGrowthBannerAddDTO dto) {
        memberGrowthBannerService.addBanner(dto);
    }

    /** 删 */
    @ApiOperation(value = "删除VIP banner图")
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('member:growthBanner:remove')")
    public void removeBanner(@PathVariable Long id){
        memberGrowthBannerService.remove(id);
    }

    /** 改 */
    @PutMapping("/edit")
    @ApiOperation(value = "修改VIP banner图")
    @PreAuthorize("hasAuthority('member:growthBanner:edit')")
    public void updateBanner(@Validated MemberGrowthBannerEditDTO dto) {
        memberGrowthBannerService.updateBanner(dto);
    }

    /** 查 */
    @GetMapping("/page")
    @ApiOperation(value = "VIP banner图列表")
    @PreAuthorize("hasAuthority('member:growthBanner:page')")
    public IPage<MemberGrowthBannerVO> findTrendsList(PageDTO<MemberGrowthBanner> page, MemberGrowthBannerQueryDTO dto) {
        return memberGrowthBannerService.getList(page, dto);
    }
}
