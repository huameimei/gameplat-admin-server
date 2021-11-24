package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberGrowthRecord;
import com.gameplat.admin.model.dto.MemberGrowthRecordDTO;
import com.gameplat.admin.model.vo.MemberGrowthRecordVO;
import com.gameplat.admin.service.MemberGrowthRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired private MemberGrowthRecordService recordService;

    @GetMapping("/list")
    @ApiOperation(value = "查询成长值记录列表")
    @PreAuthorize("hasAuthority('member:growthRecord:list')")
    public IPage<MemberGrowthRecordVO> listWealGrowthRecord(PageDTO<MemberGrowthRecord> page, MemberGrowthRecordDTO dto,  @RequestHeader(value = "country", required = false, defaultValue = "zh-CN") String language) {
        dto.setLanguage(language);
        return recordService.findRecordList(page, dto);
    }
}
