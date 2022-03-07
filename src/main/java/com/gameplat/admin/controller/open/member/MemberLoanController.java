package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLoanQueryDTO;
import com.gameplat.admin.model.vo.MemberLoanVO;
import com.gameplat.admin.service.MemberLoanService;
import com.gameplat.model.entity.member.MemberLoan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description
 * @date 2022/3/7
 */
@Api(tags = "借呗")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/loan")
public class MemberLoanController {

    @Autowired
    private MemberLoanService memberLoanService;

    /** 查 */
    @GetMapping("/page")
    @ApiOperation(value = "查")
    @PreAuthorize("hasAuthority('member:loan:page')")
    public IPage<MemberLoanVO> page(PageDTO<MemberLoan> page, MemberLoanQueryDTO dto){
        return memberLoanService.page(page, dto);
    }
}
