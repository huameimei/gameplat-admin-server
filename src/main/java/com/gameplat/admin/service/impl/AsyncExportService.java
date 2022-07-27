package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.export.ExcelBatchExportService;
import com.gameplat.admin.mapper.MemberLiveReportMapper;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportExportVo;
import com.gameplat.admin.service.ChatGifService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** @Description : 异步会员活跃度导出 @Author : cc @Date : 2022/7/27 */
@Service
@Slf4j
public class AsyncExportService {
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Autowired private MemberLiveReportMapper memberLiveReportMapper;

  @Autowired private ChatGifService chatGifService;

  @Async
  public void memberAsyncExport(MemberLiveReportDto newDto, Integer count, String bypassAccount) {
    log.info("异步会员活跃度导出开始:{}", System.currentTimeMillis());
    ExcelBatchExportService batchService = new ExcelBatchExportService();
    Workbook workbook = null;
    ByteArrayOutputStream bos = null;
    try {
      ExportParams exportParams = new ExportParams(null, "Sheet1");
      exportParams.setMaxNum(500005);
      log.info("异步会员活跃度渲染开始:{}", System.currentTimeMillis());
      // 导出会员完整信息列表(包含真实姓名，联系方式，银行卡等敏感信息)
      batchService.init(exportParams, MemberLiveReportExportVo.class);
      workbook = export(batchService, newDto, count, bypassAccount);
      log.info("异步会员活跃度渲染结束:{}", System.currentTimeMillis());

      // 将Workbook转成MultipartFile
      bos = new ByteArrayOutputStream();
      workbook.write(bos);
      byte[] barray = bos.toByteArray();
      InputStream is = new ByteArrayInputStream(barray);
      MultipartFile file = new MockMultipartFile("file", "用户活跃度导出.xlsx", "xlsx", is);

      // 上传到文件服务器供租户下载
      log.info("上传会员列表开始{}", System.currentTimeMillis());
      chatGifService.upload(file, null);
      log.info("上传会员列表结束{}", System.currentTimeMillis());
    } catch (Exception e) {
      log.info("会员导出异常,原因{}", e.getMessage());
    } finally {
      batchService.closeExportBigExcel();
      // 导出结束，不删除redis键值，而是覆盖一个30S的过期时间，便于JVM回收上次导出占用的内存资源
      redisTemplate.opsForValue().set(newDto.getExportKey(), "0", 30, TimeUnit.SECONDS);
      if (bos != null) {
        try {
          bos.flush();
          bos.close();
        } catch (IOException e) {
          log.info("IO异常,原因{}", e.getMessage());
        }
      }
      log.info("异步导出会员列表结束:{}", System.currentTimeMillis());
    }
  }

  public Workbook export(
      ExcelBatchExportService batchService,
      MemberLiveReportDto newDto,
      Integer count,
      String bypassAccount) {
    Workbook workbook = null;
    for (int a = 1; a <= count; a++) {
      log.info("会员导出全部第{}次", a);
      newDto.setPSize(10000);
      newDto.setFrom(newDto.getPSize() * (a - 1));
      List<MemberLiveReportExportVo> list = memberLiveReportMapper.list(newDto);
      // 异步导出每50000插入一个虚拟账号
      log.info("会员导出第{}次数据量{}", a, list.size());
      workbook = batchService.appendData(list);
    }
    return workbook;
  }
}
