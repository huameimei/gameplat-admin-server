package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.dto.MemberWealDetailDTO;
import com.gameplat.admin.model.dto.MemberWealDetailEditDTO;
import com.gameplat.admin.model.dto.MemberWealEditDTO;
import com.gameplat.admin.model.dto.MemberWealSettleDTO;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.admin.service.MemberWealService;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lily
 * @description vip福利发放
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
    @Autowired
    private MemberWealDetailService memberWealDetailService;

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

    @PutMapping("/settle")
    @ApiOperation(value = "福利结算")
    @PreAuthorize("hasAuthority('member:weal:settle')")
    public void settleWeal(@RequestBody MemberWealSettleDTO settleDTO) {

        if(ObjectUtils.isEmpty(settleDTO.getId())){
            throw new ServiceException("id不能为空");
        }
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
            throw new ServiceException("未开启VIP功能");
        }
        wealService.settleWeal(settleDTO.getId());
    }

    @PostMapping("/distribute")
    @ApiOperation(value = "福利派发")
    @PreAuthorize("hasAuthority('member:weal:distribute')")
    public void distributeSalary(@RequestBody MemberWeal dto, HttpServletRequest request) {
        if(ObjectUtils.isEmpty(dto.getId())){
            throw new ServiceException("id不能为空");
        }
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
            throw new ServiceException("未开启VIP功能");
        }
        wealService.distributeWeal(dto.getId(), request);
    }

    @PostMapping("/recycle")
    @ApiOperation(value = "福利回收")
    @PreAuthorize("hasAuthority('member:weal:recycle')")
    public void recycleSalary(@RequestBody MemberWeal dto, HttpServletRequest request) {
        if(ObjectUtils.isEmpty(dto.getId())){
            throw new ServiceException("id不能为空");
        }
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
            throw new ServiceException("未开启VIP功能");
        }
        wealService.recycleWeal(dto.getId(), request);
    }

    @GetMapping("/details")
    @ApiOperation(value = "详情")
    @PreAuthorize("hasAuthority('member:weal:details')")
    public IPage<MemberWealDetailVO> getDetails(PageDTO<MemberWealDetail> page, MemberWealDetailDTO dto) {
        //判断是否开启了VIP
        MemberGrowthConfigVO isVip = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
        if (isVip.getIsEnableVip() == 0) {
            throw new ServiceException("未开启VIP功能");
        }
        if (ObjectUtils.isEmpty(dto.getWealId())){
            throw new ServiceException("福利id不能为空");
        }
        return memberWealDetailService.findWealDetailList(page, dto);
    }

    /**
     *删除详情中的用户
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除详情中的用户")
    @PreAuthorize("hasAuthority('member:weal:remove')")
    public void deleteByUserId(Long id){
        memberWealDetailService.deleteById(id);
    }

    /**
     *删除详情中的用户
     */
    @PutMapping("/updateRewordAmount")
    @ApiOperation(value = "编辑详情中的用户信息")
    @PreAuthorize("hasAuthority('member:weal:edit')")
    public void editRewordAmount(@RequestBody MemberWealDetailEditDTO dto) {
        memberWealDetailService.editRewordAmount(dto);
    }

}
