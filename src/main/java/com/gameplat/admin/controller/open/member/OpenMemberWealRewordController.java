package com.gameplat.admin.controller.open.member;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import com.gameplat.admin.service.MemberGrowthConfigService;
import com.gameplat.admin.service.MemberWealRewordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "VIP福利记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/wealReword")
public class OpenMemberWealRewordController {

    @Autowired private MemberWealRewordService rewordService;

    @GetMapping("/list")
    @ApiOperation(value = "vip福利记录列表")
    @PreAuthorize("hasAuthority('member:wealReword:list')")
    public IPage<MemberWealRewordVO> listWealReword(PageDTO<MemberWealReword> page, MemberWealRewordDTO dto) {
        return rewordService.findWealRewordList(page, dto);
    }

    @SneakyThrows
    @PutMapping(value = "/exportReword", produces = "application/vnd.ms-excel")
    @ApiOperation(value = "导出VIP福利记录列表")
    @PreAuthorize("hasAuthority('member:wealReword:export')")
    public void exportWealReword(@RequestBody MemberWealRewordDTO queryDTO, HttpServletResponse response){
        List<MemberWealReword> list = rewordService.findList(queryDTO);
        ExportParams exportParams = new ExportParams("VIP福利记录列表", "VIP福利记录列表");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");

        try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, MemberWealReword.class, list)) {
            workbook.write(response.getOutputStream());
        }
    }
}
