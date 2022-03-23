package com.gameplat.admin.controller.open.member;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import com.gameplat.admin.service.MemberWealRewordService;
import com.gameplat.model.entity.member.MemberWealReword;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
  public IPage<MemberWealRewordVO> listWealReword(
      PageDTO<MemberWealReword> page, MemberWealRewordDTO dto) {
    return rewordService.findWealRewordList(page, dto);
  }

  @PutMapping("/updateRemark")
  @ApiOperation(value = "修改vip福利记录备注")
  public void updateRemark(Long id, String remark) {
    rewordService.updateRemark(id, remark);
  }

  @SneakyThrows
  @ApiOperation(value = "导出VIP福利记录列表")
  @PutMapping(value = "/exportReword", produces = "application/vnd.ms-excel")
  @PreAuthorize("hasAuthority('member:wealReword:export')")
  public void exportWealReword(MemberWealRewordDTO queryDTO, HttpServletResponse response) {
    List<MemberWealReword> list = rewordService.findList(queryDTO);
    List<MemberWealRewordVO> newList = BeanUtil.copyToList(list, MemberWealRewordVO.class);
    ExportParams exportParams = new ExportParams("VIP福利记录列表", "VIP福利记录列表");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");

    try (Workbook workbook =
        ExcelExportUtil.exportExcel(exportParams, MemberWealRewordVO.class, newList)) {
      workbook.write(response.getOutputStream());
    }
  }
}
