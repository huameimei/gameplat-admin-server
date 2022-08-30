package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameMemberReportMapper;
import com.gameplat.admin.mapper.MemberDayReportMapper;
import com.gameplat.admin.model.dto.AgentReportQueryDTO;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.AgentConfigService;
import com.gameplat.admin.service.MemberDayReportService;
import com.gameplat.admin.util.JxlsExcelUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.UUIDUtils;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.model.entity.member.MemberDayReport;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description : 会员日报表 @Author : cc @Date : 2022/3/11
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberDayReportServiceImpl extends ServiceImpl<MemberDayReportMapper, MemberDayReport>
    implements MemberDayReportService {

  /** 代理财务报表模板(重制) */
  private final String AGENT_REPORT_REMAKE = "agentReportRemakeTemplate.xlsx";

  @Autowired private AgentConfigService agentConfigService;

  @Autowired private GameMemberReportMapper memberReportMapper;

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @Override
  public PageDtoVO<MemberDayReportVo> agentReportList(
      PageDTO<MemberDayReport> page, AgentReportQueryDTO dto) {
    PageDtoVO<MemberDayReportVo> pageDtoVO = new PageDtoVO();
    Map<String, Object> otherData = new HashMap<>();
    if (StrUtil.isEmpty(dto.getStartDate()) || StrUtil.isEmpty(dto.getEndDate())) {
      dto.setStartDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
      dto.setEndDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
    }
    if (dto.getIsIncludeProxy() == null) {
      dto.setIsIncludeProxy(true);
    }
    // 获取有效会员配置
    AgentConfig agentConfig = agentConfigService.getAgentConfig();
    // 充值额
    BigDecimal rechargeAmountLimit = agentConfig.getRechargeAmountLimit();
    // 有效投注额
    BigDecimal validAmountLimit = agentConfig.getValidAmountLimit();
    // 总计
    MemberDayReportVo total =
        memberReportMapper.agentReportSummary(
            dto.getAgentName(),
            dto.getStartDate(),
            dto.getEndDate(),
            dto.getIsIncludeProxy(),
            rechargeAmountLimit,
            validAmountLimit);
    MemberDayReportVo totalMemberAndProxy =
        memberReportMapper.getTotalMemberAndProxy(dto.getAgentName());
    MemberDayReportVo registerSum =
        memberReportMapper.getMemberAndProxySum(
            dto.getAgentName(), dto.getStartDate(), dto.getEndDate());
    if (totalMemberAndProxy == null) {
      pageDtoVO.setPage(new Page<>());
      otherData.put("totalData", new MemberDayReportVo());
      pageDtoVO.setOtherData(otherData);
      return pageDtoVO;
    }
    total.setAgentTotalNum(totalMemberAndProxy.getAgentTotalNum());
    total.setMemberTotalNum(totalMemberAndProxy.getMemberTotalNum());
    total.setRegisterNum(registerSum.getRegisterNum());
    total.setRegisterAgentNum(registerSum.getRegisterAgentNum());
    // 明细
    Page<MemberDayReportVo> returnPage =
        memberReportMapper.pageList(
            page,
            dto.getAgentName(),
            dto.getStartDate(),
            dto.getEndDate(),
            dto.getIsIncludeProxy(),
            rechargeAmountLimit,
            validAmountLimit);
    // 查询出所有代理
    List<MemberDayReportVo> list =
        memberReportMapper.getMemberAndProxy(
            dto.getAgentName(), dto.getStartDate(), dto.getEndDate());
    if (CollectionUtil.isNotEmpty(returnPage.getRecords())) {
      Map<String, MemberDayReportVo> poMap =
          list.stream().collect(Collectors.toMap(MemberDayReportVo::getParentName, e -> e));
      for (MemberDayReportVo po : returnPage.getRecords()) {
        MemberDayReportVo obj = poMap.get(po.getParentName());
        if (BeanUtil.isEmpty(obj)) {
          continue;
        }
        po.setMemberTotalNum(obj.getMemberTotalNum());
        po.setAgentTotalNum(obj.getAgentTotalNum());
        po.setRegisterNum(obj.getRegisterNum());
        po.setRegisterAgentNum(obj.getRegisterAgentNum());
        po.setAgentLevel(obj.getAgentLevel());
      }
    }
    pageDtoVO.setPage(returnPage);
    otherData.put("totalData", total);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  /**
   * 导出
   *
   * @param dto
   * @param response
   */
  @Override
  public void exportAgentReport(AgentReportQueryDTO dto, HttpServletResponse response) {
    try {
      if (StrUtil.isEmpty(dto.getStartDate()) || StrUtil.isEmpty(dto.getEndDate())) {
        dto.setStartDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
        dto.setEndDate(DateUtil.format(new Date(), "YYYY-MM-dd"));
      }
      if (dto.getIsIncludeProxy() == null) {
        dto.setIsIncludeProxy(true);
      }
      // 获取有效会员配置
      AgentConfig agentConfig = agentConfigService.getAgentConfig();
      // 充值额
      BigDecimal rechargeAmountLimit = agentConfig.getRechargeAmountLimit();
      // 有效投注额
      BigDecimal validAmountLimit = agentConfig.getValidAmountLimit();
      // 总计
      MemberDayReportVo total =
          memberReportMapper.agentReportSummary(
              dto.getAgentName(),
              dto.getStartDate(),
              dto.getEndDate(),
              dto.getIsIncludeProxy(),
              rechargeAmountLimit,
              validAmountLimit);
      MemberDayReportVo totalMemberAndProxy =
          memberReportMapper.getTotalMemberAndProxy(dto.getAgentName());
      MemberDayReportVo registerSum =
          memberReportMapper.getMemberAndProxySum(
              dto.getAgentName(), dto.getStartDate(), dto.getEndDate());
      if (totalMemberAndProxy == null) {}

      total.setAgentTotalNum(totalMemberAndProxy.getAgentTotalNum());
      total.setMemberTotalNum(totalMemberAndProxy.getMemberTotalNum());
      total.setRegisterNum(registerSum.getRegisterNum());
      total.setRegisterAgentNum(registerSum.getRegisterAgentNum());
      // 明细
      List<MemberDayReportVo> returnPage =
          memberReportMapper.agentReport(
              dto.getAgentName(),
              dto.getStartDate(),
              dto.getEndDate(),
              dto.getIsIncludeProxy(),
              rechargeAmountLimit,
              validAmountLimit);
      // 查询出所有代理
      List<MemberDayReportVo> list =
          memberReportMapper.getMemberAndProxy(
              dto.getAgentName(), dto.getStartDate(), dto.getEndDate());
      // fixme 这里暂时返回有数据的代理
      if (CollectionUtil.isNotEmpty(returnPage)) {
        Map<String, MemberDayReportVo> poMap =
            list.stream().collect(Collectors.toMap(MemberDayReportVo::getParentName, e -> e));
        for (MemberDayReportVo po : returnPage) {
          MemberDayReportVo obj = poMap.get(po.getParentName());
          if (BeanUtil.isEmpty(obj)) {
            continue;
          }
          po.setMemberTotalNum(obj.getMemberTotalNum());
          po.setAgentTotalNum(obj.getAgentTotalNum());
          po.setRegisterNum(obj.getRegisterNum());
          po.setRegisterAgentNum(obj.getRegisterAgentNum());
          po.setAgentLevel(obj.getAgentLevel());
        }
      }
      // 定义ZIP包的包名
      String zipFileName = "代理报表";
      response.setHeader(
          "Content-Disposition",
          "attachment;fileName=" + URLEncoder.encode(zipFileName + ".zip", "UTF-8"));
      response.setContentType("application/zip");
      String tmpUrl =
          System.getProperty("java.io.tmpdir") + File.separator + "excel-" + UUIDUtils.getUUID32();
      final File dir = new File(tmpUrl);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      Map<String, Object> map = new HashMap<>();
      map.put("dataList", returnPage);
      map.put("totalData", total);
      map.put("startDate", dto.getStartDate());
      map.put("endDate", dto.getEndDate());
      // 单个Excel文件的fileName
      String fileName = dto.getStartDate() + "~~" + dto.getEndDate() + "代理报表" + ".xlsx";
      FileOutputStream fo = null;
      try {
        fo = new FileOutputStream(new File(dir + File.separator + fileName));
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }
      try {
        JxlsExcelUtils.downLoadExcel(map, AGENT_REPORT_REMAKE, fo);
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
      ZipFile zipFile = new ZipFile(tmpUrl.concat(".zip"));
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
      if (StrUtil.isNotBlank(dto.getZipPswd())) {
        zipFile.setPassword(dto.getZipPswd().toCharArray());
      }
      // 要打包的文件夹
      File[] fList = dir.listFiles();

      // 遍历test文件夹下所有的文件、文件夹
      for (File f : fList) {
        if (f.isDirectory()) {
          zipFile.addFolder(f, parameters);
        } else {
          zipFile.addFile(f, parameters);
        }
      }

      OutputStream out = response.getOutputStream();
      out.write(FileUtil.readBytes(zipFile.getFile()));
      out.flush();
      FileUtil.del(dir);
      FileUtil.del(tmpUrl.concat(".zip"));
    } catch (Exception e) {
      throw new ServiceException("代理报表导出IO错误:{}", e);
    }
  }

  @Override
  public void addUpdateWaterAmount(
      MemberInfoVO member, BigDecimal realRebateMoney, String statTime) {
    String toDateStr = StrUtil.isNotBlank(statTime) ? statTime : DateTime.now().toDateStr();
    String account = member.getAccount();
    MemberDayReport memberDayReport = memberReportMapper.getMemberDayReport(account, toDateStr);
    if (BeanUtil.isNotEmpty(memberDayReport)) {
      BigDecimal waterAmount = memberDayReport.getWaterAmount();
      waterAmount = waterAmount == null ? BigDecimal.ZERO : waterAmount;
      memberReportMapper.updateWaterAmount(account, toDateStr, realRebateMoney.add(waterAmount));
    } else {
      MemberDayReport saveObj = new MemberDayReport();
      saveObj.setCountDate(DateUtil.parse(toDateStr, "yyyy-MM-dd"));
      saveObj.setUserId(member.getId());
      saveObj.setUserName(member.getAccount());
      saveObj.setParentId(member.getParentId() == null ? 1L : member.getParentId().longValue());
      saveObj.setParentName(member.getParentName());
      String userType = member.getUserType();
      saveObj.setUserType(userType.equalsIgnoreCase("A") ? 5 : 2);
      saveObj.setAgentPath(member.getSuperPath());
      saveObj.setWaterAmount(realRebateMoney);
      saveObj.setUserLevel(member.getUserLevel() == null ? "1" : member.getUserLevel().toString());
      saveObj.setVipLevel(member.getVipLevel() == null ? "0" : member.getVipLevel().toString());
      memberReportMapper.insert(saveObj);
    }
  }
}
