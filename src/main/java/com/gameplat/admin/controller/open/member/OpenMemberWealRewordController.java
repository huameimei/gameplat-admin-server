package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberWealRewordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Api(tags = "VIP福利记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/wealReword")
public class OpenMemberWealRewordController {

    @Autowired private MemberWealRewordService rewordService;
    @Autowired private MemberGrowthConfigService configService;

    @GetMapping("/list")
    @ApiOperation(value = "vip福利记录列表")
    @PreAuthorize("hasAuthority('member:wealReword:list')")
    public IPage<MemberWealRewordVO> listWealReword(PageDTO<MemberWealReword> page, MemberWealRewordDTO dto) {

        if(ObjectUtils.isNotEmpty(dto.getStartTime()) && ObjectUtils.isEmpty(dto.getEndTime())){
            dto.setEndTime(dto.getStartTime());
        }
        return rewordService.findWealRewordList(page, dto);
    }

    @PutMapping("/exportReword")
    @ApiOperation(value = "导出VIP福利记录列表")
    @PreAuthorize("hasAuthority('member:wealReword:export')")
    public void exportWealReword(@RequestBody MemberWealRewordDTO queryDTO, HttpServletResponse response){
        rewordService.exportWealReword(queryDTO, response);
    }

//    @PutMapping("/check")
//    @ApiOperation(value = "审核升级奖励")
//    @PreAuthorize("hasAuthority('member:wealReword:check')")
//    public void check(@RequestBody MemberWealRewordCheckDTO dto, HttpServletRequest request){
//
//        MemberGrowthConfigVO config = configService.findOneConfig(LanguageEnum.app_zh_CN.getCode());
//
//        if (config.getIsEnableVip() == 0) {
//            throw new ServiceException("未开启VIP功能");
//        }
//        if (dto.getId() == null) {
//            throw new ServiceException("参数不全");
//        }
//        if (dto.getStatus() == 3 && StrUtil.isBlank(dto.getRemark())) {
//            throw new ServiceException("参数不全");
//        }
//        rewordService.check(dto, request);
//    }

}
