package com.gameplat.admin.controller.open.report;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AgentReportQueryDTO;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.MemberDayReportService;
import com.gameplat.admin.util.JxlsExcelUtils;
import com.gameplat.base.common.util.UUIDUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.model.entity.member.MemberDayReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 代理报表 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "代理报表")
@RestController
@RequestMapping("/api/admin/proxy/report")
public class ProxyMemberReportController {

  @Autowired private MemberDayReportService memberDayReportService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('agent:report:view')")
  public PageDtoVO<MemberDayReportVo> list(PageDTO<MemberDayReport> page, AgentReportQueryDTO dto) {
    return memberDayReportService.agentReportList(page, dto);
  }

  @GetMapping("/export")
  @Operation(summary = "代理报表导出")
  @PreAuthorize("hasAuthority('agent:report:export')")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "'代理报表导出'")
  public void export2(AgentReportQueryDTO dto, HttpServletResponse response) {
    memberDayReportService.exportAgentReport(dto, response);
  }

  @GetMapping("/export2")
  public void export(AgentReportQueryDTO dto, HttpServletResponse response) throws IOException {
    String s =
        System.getProperty("java.io.tmpdir") + File.separator + "excel-" + UUIDUtils.getUUID32();
    System.out.println(s);
    File file = new File(s);
    if (!file.exists()) {
      file.mkdirs();
    }

    Map<String, Object> map = new HashMap<>();
    map.put("dataList", 1);
    map.put("totalData", 2);
    map.put("startDate", dto.getStartDate());
    map.put("endDate", dto.getEndDate());
    // 单个Excel文件的fileName
    String fileName = dto.getStartDate() + "~~" + dto.getEndDate() + "代理报表" + ".xlsx";
    FileOutputStream fo = null;
    try {
      fo = new FileOutputStream(new File(file + File.separator + fileName));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    try {
      JxlsExcelUtils.downLoadExcel(map, "agentReportRemakeTemplate.xlsx", fo);
    } catch (InvalidFormatException | IOException e1) {
      e1.printStackTrace();
    } finally {
      if (fo != null) {
        try {
          fo.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    //    File file = new File("D://opt//kgs//cloud//platform");
    // 生成的压缩文件
    ZipFile zipFile = new ZipFile(s.concat(".zip"));
    ZipParameters parameters = new ZipParameters();
    // 压缩方式
    parameters.setCompressionMethod(CompressionMethod.DEFLATE);
    // 压缩级别
    parameters.setCompressionLevel(CompressionLevel.NORMAL);
    // 是否设置加密文件
    parameters.setEncryptFiles(true);
    // 设置加密算法
    parameters.setEncryptionMethod(EncryptionMethod.AES);
    // 设置AES加密密钥的密钥强度
    parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
    // 设置密码
    if (StrUtil.isNotBlank("112233")) {
      zipFile.setPassword("112233".toCharArray());
    }
    // 要打包的文件夹
    File[] fList = file.listFiles();

    // 遍历test文件夹下所有的文件、文件夹
    for (File f : fList) {
      if (f.isDirectory()) {
        zipFile.addFolder(f, parameters);
      } else {
        zipFile.addFile(f, parameters);
      }
    }
    response.setHeader(
        "Content-Disposition",
        "attachment;fileName=" + URLEncoder.encode("代理报表" + ".zip", "UTF-8"));
    response.setContentType("application/zip");
    OutputStream out = response.getOutputStream();
    //        File zipFile = ZipUtil.zip(dir);
    //        out.write(FileUtil.readBytes(zipFile));
    out.write(FileUtil.readBytes(zipFile.getFile()));
    out.flush();
    FileUtil.del(file);
    FileUtil.del(s.concat(".zip"));
  }
}
