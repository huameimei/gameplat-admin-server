package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.dto.MemberGrowthChangeDto;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.admin.service.MemberGrowthRecordService;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author lily
 * @description 用户成长值记录
 * @date 2021/11/23
 */

@Api(tags = "VIP成长记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/growthRecord")
public class OpenMemberGrowthRecordController {

    @Autowired private MemberGrowthRecordService memberGrowthRecordService;

    @GetMapping("/list")
    @ApiOperation(value = "查询成长值记录列表")
    @PreAuthorize("hasAuthority('member:growthRecord:list')")
    public IPage<MemberGrowthRecordVO> listWealGrowthRecord(PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto,  @RequestHeader(value = "country", required = false, defaultValue = "zh-CN") String language) {
        dto.setLanguage(language);
        return memberGrowthRecordService.findRecordList(page, dto);
    }

    @PutMapping("/editGrowth")
    @ApiOperation(value = "修改单个会员成长值")
    @PreAuthorize("hasAuthority('member:growthRecord:editGrowth')")
    public void editMemberGrowth(@RequestBody MemberGrowthChangeDto dto) {
        log.info("单个会员成长值变动：MemberGrowthRecord={}", dto);
        try {
            if (dto == null || dto.getChangeGrowth() == null || dto.getType() == null) {
                throw new ServiceException("参数不全！");
            }
            if ( dto.getChangeGrowth() == 0 ) {
                throw new ServiceException("扣除/添加成长值不能为0！");
            }
        memberGrowthRecordService.editMemberGrowth(dto, ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        }catch (Exception e) {
            log.error(e.getMessage());
            log.info("异常原因:", e);
            throw new ServiceException("成长值变动失败！");
        }
    }
}
