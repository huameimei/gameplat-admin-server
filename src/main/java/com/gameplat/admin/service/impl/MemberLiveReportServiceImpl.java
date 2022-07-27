package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.export.ExcelBatchExportService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberLiveReportMapper;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportExportVo;
import com.gameplat.admin.model.vo.MemberLiveReportVo;
import com.gameplat.admin.service.MemberLiveReportService;
import com.gameplat.admin.util.ExportPoiExcelUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.model.entity.member.MemberDayReport;
import com.gameplat.security.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MemberLiveReportServiceImpl
    extends ServiceImpl<MemberLiveReportMapper, MemberDayReport>
    implements MemberLiveReportService {

  @Autowired private MemberLiveReportMapper memberLiveReportMapper;

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Autowired private AsyncExportService asyncExportService;

  @Override
  public Page<MemberLiveReportVo> queryPage(
      PageDTO<MemberLiveReportDto> page, MemberLiveReportDto dto) {
    if (StrUtil.isBlank(dto.getOrderColumn())) {
      dto.setOrderColumn("final.winAmount");
      // final.winAmount
      // final.lotteryWinAmount
      // final.sportWinAmount
      // realWinAmount
      // final.withdrawMoney
      // final.rechargeMoney
      // final.withdrawCount
      // final.rechargeCount
      // final.lotteryValidAmount
      // final.sportValidAmount
      // realValidAmount
    }
    if (StrUtil.isBlank(dto.getSortType())) {
      dto.setSortType("desc");
      // desc
      // asc
    }
    return memberLiveReportMapper.pageList(page, dto);
  }

  @Override
  public Integer exportCount(MemberLiveReportDto dto) {
    if (StrUtil.isBlank(dto.getOrderColumn())) {
      dto.setOrderColumn("final.winAmount");
      // final.winAmount
      // final.lotteryWinAmount
      // final.sportWinAmount
      // realWinAmount
      // final.withdrawMoney
      // final.rechargeMoney
      // final.withdrawCount
      // final.rechargeCount
      // final.lotteryValidAmount
      // final.sportValidAmount
      // realValidAmount
    }
    if (StrUtil.isBlank(dto.getSortType())) {
      dto.setSortType("desc");
      // desc
      // asc
    }
    return memberLiveReportMapper.exportCount(dto);
  }

  @Override
  public void export(MemberLiveReportDto dto, Integer memberSum, HttpServletResponse response) {
    Integer count = memberSum / 10000;
    if (memberSum > count * 10000) {
      count++;
    }
    if (memberSum <= 50000) {
      ExcelBatchExportService batchService = new ExcelBatchExportService();
      Workbook workbook = null;
      ExportParams exportParams = new ExportParams(null, "Sheet1");
      exportParams.setMaxNum(50005);
      log.info("导出会员活跃度渲染开始:{}", System.currentTimeMillis());
      batchService.init(exportParams, MemberLiveReportExportVo.class);
      workbook = export(batchService, dto, count);
      try {
        ExportPoiExcelUtils.exportData(response, workbook);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        batchService.closeExportBigExcel();
      }
    } else {
      String rediskey = "member:live:report:".concat(SecurityUserHolder.getUsername());
      dto.setExportKey(rediskey);
      log.info("异步导出会员活跃用户开始{}", System.currentTimeMillis());
      // 防止重复点击会员导出按钮，默认过期时间为30分钟，异步导出执行完毕后手动删除
      Boolean result =
          redisTemplate.opsForValue().setIfAbsent(dto.getExportKey(), "0", 5, TimeUnit.MINUTES);
      if (Boolean.FALSE.equals(result)) {
        throw new ServiceException("正在后台导出，请耐心等待");
      }
      try {
        // 深拷贝一份请求体的数据对象，防止出现请求响应超时，断开连接导致数据对象被销毁
        MemberLiveReportDto newDto = new MemberLiveReportDto();
        BeanUtils.copyBeanProp(newDto, dto);
        asyncExportService.memberAsyncExport(newDto, count, SecurityUserHolder.getUsername());
      } catch (Exception e) {
        log.error("会员活跃用户数据导出异常,原因{}", e.getMessage());
        redisTemplate.delete(dto.getExportKey());
        throw new ServiceException("会员活跃用户数据导出失败");
      }
    }
  }

  public Workbook export(
      ExcelBatchExportService batchService, MemberLiveReportDto newDto, Integer count) {
    // 查到子账号下对应的虚拟会员账号（共5个）
    Workbook workbook = null;
    for (int a = 1; a <= count; a++) {
      log.info("会员导出全部第{}次", a);
      newDto.setPSize(10000);
      newDto.setFrom(newDto.getPSize() * (a - 1));
      List<MemberLiveReportExportVo> list = memberLiveReportMapper.list(newDto);
      log.info("会员导出第{}次数据量{}", a, list.size());
      workbook = batchService.appendData(list);
    }
    return workbook;
  }
}
