package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.admin.service.MemberBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lily
 * @description 现金流水
 * @date 2021/12/2
 */

@Api(tags = "现金流水API")
@Slf4j
@RestController
@RequestMapping("/api/admin/funds/cash")
public class OpenMemberBillContorller {

    @Autowired
    private MemberBillService memberBillService;

    @ApiOperation(value = "现金流水")
    @GetMapping("/pageList")
    @PreAuthorize("hasAuthority('funds:cash:list')")
    public IPage<MemberBillVO> findMemberBilllist(PageDTO<MemberBill> page, MemberBillDTO dto){
        return memberBillService.findMemberBilllist(page,dto);
    }

    @GetMapping("/exportBill")
    @ApiOperation(value = "导出现金流水列表")
    @PreAuthorize("hasAuthority('funds:cash:export')")
    public void exportSign( MemberBillDTO dto, HttpServletResponse response){
        memberBillService.exportList(dto, response);
    }

}
