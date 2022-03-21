package com.gameplat.admin.controller.open.member;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.common.model.bean.TranTypeBean;
import com.gameplat.model.entity.member.MemberBill;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 现金流水
 *
 * @author lily
 * @date 2021/12/2
 */
@Api(tags = "现金流水")
@Slf4j
@RestController
@RequestMapping("/api/admin/funds/cash")
public class OpenMemberBillController {

  @Autowired private MemberBillService memberBillService;

  @ApiOperation(value = "现金流水")
  @GetMapping("/pageList")
  @PreAuthorize("hasAuthority('funds:cash:list')")
  public IPage<MemberBillVO> queryPage(PageDTO<MemberBill> page, MemberBillDTO dto) {
    return memberBillService.queryPage(page, dto);
  }

  @SneakyThrows
  @GetMapping(value = "/exportBill", produces = "application/vnd.ms-excel")
  @ApiOperation(value = "导出现金流水列表")
  @PreAuthorize("hasAuthority('funds:cash:export')")
  public void exportSign(MemberBillDTO dto, HttpServletResponse response) {
    List<MemberBillVO> list = memberBillService.queryList(dto);
    ExportParams exportParams = new ExportParams("现金流水列表", "现金流水列表");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = myExcel.xls");
    try (Workbook workbook = ExcelExportUtil.exportExcel(exportParams, MemberBillVO.class, list)) {
      workbook.write(response.getOutputStream());
    }
  }

  @GetMapping("/tranTypes")
  @ApiOperation(value = "账变类型")
  public List<TranTypeBean> findTranTypes() {
    return memberBillService.findTranTypes();
  }
}
