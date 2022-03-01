package com.gameplat.admin.controller.open.member;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberVipSignHistoryDTO;
import com.gameplat.admin.model.dto.MemberVipSignStatisDTO;
import com.gameplat.admin.model.vo.MemberVipSignHistoryVO;
import com.gameplat.admin.model.vo.MemberVipSignStatisVO;
import com.gameplat.admin.service.MemberVipSignHistoryService;
import com.gameplat.admin.service.MemberVipSignStatisService;
import com.gameplat.model.entity.member.MemberVipSignHistory;
import com.gameplat.model.entity.member.MemberVipSignStatis;
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
 * @author lily
 * @description VIP会员签到汇总
 * @date 2021/11/24
 */
@Api(tags = "VIP会员签到汇总")
@Slf4j
@RestController
@RequestMapping("/api/admin/member/sign")
public class OpenMemberVipSignStatisController {

  @Autowired private MemberVipSignStatisService signStatisService;

  @Autowired private MemberVipSignHistoryService memberVipSignHistoryService;

  @GetMapping("/list")
  @ApiOperation(value = "查询VIP会员签到记录列表")
  @PreAuthorize("hasAuthority('member:sign:list')")
  public IPage<MemberVipSignStatisVO> querySignList(
      PageDTO<MemberVipSignStatis> page, MemberVipSignStatisDTO dto) {
    return signStatisService.findSignListPage(page, dto);
  }

  @SneakyThrows
  @GetMapping(value = "/exportSign", produces = "application/vnd.ms-excel")
  @ApiOperation(value = "导出VIP签到记录列表")
  @PreAuthorize("hasAuthority('member:sign:export')")
  public void exportSign(MemberVipSignStatisDTO queryDTO, HttpServletResponse response) {
    List<MemberVipSignStatis> list = signStatisService.findSignList(queryDTO);
    ExportParams exportParams = new ExportParams("VIP签到记录", "VIP签到记录");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = myExcel.xls");

    try (Workbook workbook =
        ExcelExportUtil.exportExcel(exportParams, MemberVipSignStatisVO.class, list)) {
      workbook.write(response.getOutputStream());
    }
  }

  @GetMapping("/history")
  @ApiOperation(value = "会员签到记录")
  @PreAuthorize("hasAuthority('member:sign:history')")
  public IPage<MemberVipSignHistoryVO> getHistoryList(
      PageDTO<MemberVipSignHistory> page, MemberVipSignHistoryDTO dto) {
    return memberVipSignHistoryService.findPageList(page, dto);
  }
}
