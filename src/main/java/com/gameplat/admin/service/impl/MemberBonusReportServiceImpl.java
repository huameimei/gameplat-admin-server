package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberBonusReportMapper;
import com.gameplat.admin.mapper.MemberWealRewordMapper;
import com.gameplat.admin.model.dto.MemberBonusReportQueryDTO;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.MemberBonusReportService;
import com.gameplat.base.common.util.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aBen
 * @date 2022/3/16 20:40
 * @desc
 */
@Service
public class MemberBonusReportServiceImpl extends ServiceImpl<MemberBonusReportMapper, MemberBonusReportVO>
        implements MemberBonusReportService {

  @Autowired
  private MemberBonusReportMapper memberBonusReportMapper;

  @Autowired
  private MemberWealRewordMapper memberWealRewordMapper;


  @Override
  public PageDtoVO<MemberBonusReportVO> findMemberBonusReportPage(Page<MemberBonusReportVO> page, MemberBonusReportQueryDTO queryDTO) {
    PageDtoVO<MemberBonusReportVO> pageDtoVO = new PageDtoVO<>();
    Page<MemberBonusReportVO> memberBonusReportVOList = memberBonusReportMapper.findMemberBonusReportPage(page, queryDTO);
    if (StringUtils.isNotEmpty(memberBonusReportVOList.getRecords())) {
      // 查询每个会员的VIP福利统计
      findMemberWealData(memberBonusReportVOList.getRecords(), queryDTO);
    }

    // 查询各项红利总计(除了VIP数据)
    TotalMemberBonusReportVO totalBonusData = memberBonusReportMapper.findMemberBonusReportTotal(queryDTO);
    // 查询VIP红利总计
    MemberWealRewordDTO wealDto = new MemberWealRewordDTO();
    wealDto.setUserName(queryDTO.getUserName());
    wealDto.setSuperAccount(queryDTO.getSuperAccount());
    wealDto.setFlag(queryDTO.getFlag());
    wealDto.setStartTime(queryDTO.getStartTime() + " 00:00:00");
    wealDto.setEndTime(queryDTO.getEndTime() + " 23:59:59");
    TotalMemberWealRewordVO totalWealData = memberWealRewordMapper.findMemberWealRewordTotal(wealDto);
    // 组装总计
    totalBonusData.setTotalUpRewardAmount(totalWealData.getTotalUpRewardAmount());
    totalBonusData.setTotalWeekWageAmount(totalWealData.getTotalWeekWageAmount());
    totalBonusData.setTotalMonthWageAmount(totalWealData.getTotalMonthWageAmount());
    totalBonusData.setTotalBirthGiftMoneyAmount(totalWealData.getTotalBirthGiftMoneyAmount());
    totalBonusData.setTotalMonthRedEnvelopeAmount(totalWealData.getTotalMonthRedEnvelopeAmount());
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", totalBonusData);
    pageDtoVO.setPage(memberBonusReportVOList);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public void exportMemberBonusReport(MemberBonusReportQueryDTO queryDTO, HttpServletResponse response) {
    List<MemberBonusReportVO> memberBonusReportVOList = memberBonusReportMapper.findMemberBonusReportList(queryDTO);
    if (StringUtils.isNotEmpty(memberBonusReportVOList)) {
      findMemberWealData(memberBonusReportVOList, queryDTO);
    }
    ExportParams exportParams = new ExportParams("会员红利报表", "会员红利报表");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename = vipWealReword.xls");

    Workbook workbook = ExcelExportUtil.exportExcel(exportParams, MemberBonusReportVO.class, memberBonusReportVOList);
    try {
      workbook.write(response.getOutputStream());
    } catch (IOException e) {
      log.error("会员红利报表导出IO异常", e);
    }
  }


  /**
   * 查询每个会员的VIP福利统计
   *
   * @param memberBonusReportVOList 会员红利集合
   * @param queryDTO                查询DTO
   */
  public void findMemberWealData(List<MemberBonusReportVO> memberBonusReportVOList, MemberBonusReportQueryDTO queryDTO) {
    memberBonusReportVOList.parallelStream().forEach(memberBonusReportVO -> {
      if (memberBonusReportVO.getVipBonusAmount().compareTo(BigDecimal.ZERO) == 1) {
        MemberWealRewordDTO wealDto = new MemberWealRewordDTO();
        wealDto.setUserName(memberBonusReportVO.getUserName());
        wealDto.setStartTime(queryDTO.getStartTime() + " 00:00:00");
        wealDto.setEndTime(queryDTO.getEndTime() + " 23:59:59");
        TotalMemberWealRewordVO memberWealRewordVO = memberWealRewordMapper.findMemberWealRewordTotal(wealDto);
        memberBonusReportVO.setUpRewardAmount(memberWealRewordVO.getTotalUpRewardAmount());
        memberBonusReportVO.setWeekWageAmount(memberWealRewordVO.getTotalWeekWageAmount());
        memberBonusReportVO.setMonthWageAmount(memberWealRewordVO.getTotalMonthWageAmount());
        memberBonusReportVO.setBirthGiftMoneyAmount(memberWealRewordVO.getTotalBirthGiftMoneyAmount());
        memberBonusReportVO.setMonthRedEnvelopeAmount(memberWealRewordVO.getTotalMonthRedEnvelopeAmount());
      } else {
        memberBonusReportVO.setUpRewardAmount(BigDecimal.ZERO);
        memberBonusReportVO.setWeekWageAmount(BigDecimal.ZERO);
        memberBonusReportVO.setMonthWageAmount(BigDecimal.ZERO);
        memberBonusReportVO.setBirthGiftMoneyAmount(BigDecimal.ZERO);
        memberBonusReportVO.setMonthRedEnvelopeAmount(BigDecimal.ZERO);
      }
    });
  }


}
