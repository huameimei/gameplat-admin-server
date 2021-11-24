package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.dto.MemberWealEditDTO;
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


/**
 * @author lily
 * @description 会员等级福利
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
    @ApiOperation(value = "查询福利发放记录列表")
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

    @PutMapping("/edit")
    @ApiOperation(value = "修改福利发放")
    @PreAuthorize("hasAuthority('member:weal:edit')")
    public void updateWeal(@RequestBody MemberWealEditDTO dto) {
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
           throw new ServiceException("未开启VIP功能");
        }
        wealService.updateMemberWeal(dto);
    }

    @ApiOperation(value = "删除福利发放")
    @DeleteMapping("/remove")
    @PreAuthorize("hasAuthority('member:weal:remove')")
    public void removeWeal(Long id){
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
            throw new ServiceException("未开启VIP功能");
        }
        wealService.deleteMemberWeal(id);
    }

//    @PutMapping("/settle")
//    @ApiOperation(value = "福利结算")
//    public void settleWeal(Long id) {
//
//    }
//
//    @PostMapping("/distribute")
//    @ApiOperation(value = "福利派发")
//    public void distributeSalary(@RequestBody UserWealDetail dto, HttpServletRequest request) {
//    }
//
//    @PostMapping("/recycle")
//    @ApiOperation(value = "福利回收")
//    public void recycleSalary(@RequestBody UserWealDetail dto, HttpServletRequest request) {
//    }
//
//    @PostMapping("/details")
//    @ApiOperation(value = "详情")
//    @LogAnnotation(module = "user-center", recordRequestParam = false, desc = "详情")
//    public void exportDetails(@RequestBody UserWealDto userWealDto, HttpServletResponse response) {
//
//    }

}
