package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberWealService;
import com.gameplat.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lily
 * @description 福利发放
 * @date 2021/11/22
 */

@Api(tags = "vip福利发放")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/weal")
public class OpenMemberWealController {

    @Autowired
    private MemberWealService wealService;
    @Autowired
    private MemberGrowthConfigService configService;

    @GetMapping("/list")
    @ApiOperation(value = "福利发放记录")
    @PreAuthorize("hasAuthority('member:weal:list')")
    public IPage<MemberWealVO> listWeal(PageDTO<MemberWeal> page, MemberWealDTO dto) {
        return wealService.findMemberWealList(page, dto);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增福利发放")
    @PreAuthorize("hasAuthority('member:weal:add')")
    public void addMemberWeal(@RequestBody MemberWealAddDTO dto) {
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
            throw new ServiceException("未开启VIP功能");
        }
        wealService.addMemberWeal(dto);
    }

}
