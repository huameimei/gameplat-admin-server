package com.gameplat.admin.controller.open.member;

import com.gameplat.admin.model.domain.MemberGrowthLevel;
import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.vo.LogoConfigVO;
import com.gameplat.admin.service.MemberGrowthLevelService;
import com.gameplat.base.common.util.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lily
 * @description Logo配置
 * @date 2022/2/5
 */

@Api(tags = "Logo配置")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/logo")
public class LogoConfigController {

    @Autowired private MemberGrowthLevelService memberGrowthLevelService;

    @ApiOperation(value = "logo配置列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('member:logo:list')")
    public List<LogoConfigVO> list(){
        List<MemberGrowthLevel> list = memberGrowthLevelService
                .lambdaQuery()
                .select(MemberGrowthLevel::getId, MemberGrowthLevel::getLevel, MemberGrowthLevel::getMobileVipImage, MemberGrowthLevel::getWebVipImage, MemberGrowthLevel::getMobileReachBackImage, MemberGrowthLevel::getMobileUnreachBackImage, MemberGrowthLevel::getMobileReachVipImage, MemberGrowthLevel::getMobileUnreachVipImage, MemberGrowthLevel::getWebReachVipImage, MemberGrowthLevel::getWebUnreachVipImage, MemberGrowthLevel::getUpdateBy, MemberGrowthLevel::getUpdateTime)
                .list();

        List<LogoConfigVO> voList = new ArrayList<>();
        for (MemberGrowthLevel growthLevel : list) {
            LogoConfigVO logoConfigVO = new LogoConfigVO();
            BeanUtils.copyBeanProp(logoConfigVO, growthLevel);
            voList.add(logoConfigVO);
        }
        return voList;
    }

    @ApiOperation(value = "修改logo配置")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('member:logo:edit')")
    public void edit(@Validated GrowthLevelLogoEditDTO dto) {
        memberGrowthLevelService.updateLogo(dto);
    }
}
