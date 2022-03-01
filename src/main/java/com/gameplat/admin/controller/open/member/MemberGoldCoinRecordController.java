package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.model.entity.member.MemberGoldCoinRecord;
import com.gameplat.admin.model.dto.MemberGoldCoinRecordQueryDTO;
import com.gameplat.admin.model.vo.MemberGoldCoinRecordVO;
import com.gameplat.admin.service.MemberGoldCoinRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description
 * @date 2022/3/1
 */
@Api(tags = "VIP金币明细")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/coin")
public class MemberGoldCoinRecordController {

    @Autowired
    private MemberGoldCoinRecordService memberGoldCoinRecordService;

    /** 增 */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询VIP金币明细")
    @PreAuthorize("hasAuthority('member:coin:page')")
    public IPage<MemberGoldCoinRecordVO> page(PageDTO<MemberGoldCoinRecord> page, MemberGoldCoinRecordQueryDTO dto){
        return memberGoldCoinRecordService.page(page, dto);
    }

    /** 增 */
    @PostMapping("/add")
    @ApiOperation(value = "后台添加VIP金币明细")
    @PreAuthorize("hasAuthority('member:coin:add')")
    public void add(Long memberId, Integer amount){
        memberGoldCoinRecordService.add(memberId, amount);
    }
}
