package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberGrowthBanner;
import com.gameplat.admin.model.dto.MemberGrowthBannerDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import com.gameplat.admin.service.MemberGrowthBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiOperation(value = "新增VIPbanner图")
    public void addBanner(@Validated @RequestBody MemberGrowthBannerDTO dto) {
        memberGrowthBannerService.addBanner(dto);
    }

    /** 删 */
    @ApiOperation(value = "删除VIP banner图")
    @DeleteMapping("/remove")
    public void removeBanner(Long id){
        memberGrowthBannerService.remove(id);
    }

    /** 改 */
    @PutMapping("/edit")
    @ApiOperation(value = "修改VIP banner图")
    public void updateBanner(@RequestBody MemberGrowthBannerDTO dto) {
        memberGrowthBannerService.updateBanner(dto);
    }

    /** 查 */
    @GetMapping("/page")
    @ApiOperation(value = "VIP banner图列表")
    public IPage<MemberGrowthBannerVO> findTrendsList(PageDTO<MemberGrowthBanner> page, MemberGrowthBannerDTO dto) {
        return memberGrowthBannerService.getList(page, dto);
    }
}
