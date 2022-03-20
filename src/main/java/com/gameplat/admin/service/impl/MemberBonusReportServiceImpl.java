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
import com.gameplat.admin.util.ListUtils;
import com.gameplat.base.common.exception.ServiceException;
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
import java.util.stream.Collectors;

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

  private final static int UP_REWARD = 0;

  private final static int WEEK_WAGE = 1;

  private final static int MONTH_WAGE = 2;

  private final static int BIRTH_GIFT_MONEY = 3;

  private final static int MONTH_RED_ENVELOPE = 4;


  @Override
  public PageDtoVO<MemberBonusReportVO> findMemberBonusReportPage(Page<MemberBonusReportVO> page, MemberBonusReportQueryDTO queryDTO) {
    PageDtoVO<MemberBonusReportVO> pageDtoVO = new PageDtoVO<>();
    Page<MemberBonusReportVO> memberBonusReportVOList = memberBonusReportMapper.findMemberBonusReportPage(page, queryDTO);
    if (StringUtils.isNotEmpty(memberBonusReportVOList.getRecords())) {
      // 查询每个会员的VIP福利统计
      findMemberVipBonusDetail(memberBonusReportVOList.getRecords(), queryDTO);
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
    // 查询VIP红利总计
    List<TotalVipWealTypeVO> totalVipWeal = memberWealRewordMapper.findVipWealTypeTotal(wealDto);
    // 组装总计
    totalVipWeal.forEach(data -> {
      if (data.getType() == UP_REWARD) {
        totalBonusData.setTotalUpRewardAmount(data.getRewordAmount());
      } else if (data.getType() == WEEK_WAGE) {
        totalBonusData.setTotalWeekWageAmount(data.getRewordAmount());
      } else if (data.getType() == MONTH_WAGE) {
        totalBonusData.setTotalMonthWageAmount(data.getRewordAmount());
      } else if (data.getType() == BIRTH_GIFT_MONEY) {
        totalBonusData.setTotalBirthGiftMoneyAmount(data.getRewordAmount());
      } else if (data.getType() == MONTH_RED_ENVELOPE) {
        totalBonusData.setTotalMonthRedEnvelopeAmount(data.getRewordAmount());
      }
    });

    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", totalBonusData);
    pageDtoVO.setPage(memberBonusReportVOList);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public void exportMemberBonusReport(MemberBonusReportQueryDTO queryDTO, HttpServletResponse response) {
    Integer memberSum = memberBonusReportMapper.findMemberBonusReportCount(queryDTO);
    if (memberSum > 200000) {
      throw new ServiceException("导出红利报表会员数量大于20万，请缩短导出时间");
    }

    List<MemberBonusReportVO> memberBonusReportVOList = memberBonusReportMapper.findMemberBonusReportList(queryDTO);
    if (StringUtils.isNotEmpty(memberBonusReportVOList)) {
      List<List<MemberBonusReportVO>> splitList = ListUtils.splitListBycapacity(memberBonusReportVOList, 1000);
      splitList.forEach(list -> {
        findMemberVipBonusDetail(list, queryDTO);
      });
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
  public void findMemberVipBonusDetail(List<MemberBonusReportVO> memberBonusReportVOList, MemberBonusReportQueryDTO queryDTO) {
    // 过滤出需要查询VIP详细福利的会员 0: 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金 4：每月红包
    List<String> userNameList = memberBonusReportVOList.stream()
            .filter(item -> item.getVipBonusAmount().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList())
            .stream().map(MemberBonusReportVO::getUserName).collect(Collectors.toList());
    MemberWealRewordDTO wealDto = new MemberWealRewordDTO();
    wealDto.setUserNameList(userNameList);
    wealDto.setStartTime(queryDTO.getStartTime() + " 00:00:00");
    wealDto.setEndTime(queryDTO.getEndTime() + " 23:59:59");
    List<MemberWealRewordVO> memberVipBonusDetailList = memberWealRewordMapper.findVipBonusDetailList(wealDto);
    // 根据userName进行分组
    Map<String, List<MemberWealRewordVO>> memberMap = memberVipBonusDetailList.stream().collect(Collectors.groupingBy(MemberWealRewordVO::getUserName));
    // 循环原本的所有红利数据
    memberBonusReportVOList.parallelStream().forEach(item -> {
      // 默认赋值为0
      item.setUpRewardAmount(BigDecimal.ZERO);
      item.setWeekWageAmount(BigDecimal.ZERO);
      item.setMonthWageAmount(BigDecimal.ZERO);
      item.setBirthGiftMoneyAmount(BigDecimal.ZERO);
      item.setMonthRedEnvelopeAmount(BigDecimal.ZERO);
      List<MemberWealRewordVO> memberVipBonusDetail = memberMap.get(item.getUserName());
      // 如果有，则赋值对应VIP福利详情数据
      if (StringUtils.isNotEmpty(memberVipBonusDetail)) {
        memberVipBonusDetail.forEach(data -> {
          if (data.getType() == UP_REWARD) {
            item.setUpRewardAmount(data.getRewordAmount());
          } else if (data.getType() == WEEK_WAGE) {
            item.setWeekWageAmount(data.getRewordAmount());
          } else if (data.getType() == MONTH_WAGE) {
            item.setMonthWageAmount(data.getRewordAmount());
          } else if (data.getType() == BIRTH_GIFT_MONEY) {
            item.setBirthGiftMoneyAmount(data.getRewordAmount());
          } else if (data.getType() == MONTH_RED_ENVELOPE) {
            item.setMonthRedEnvelopeAmount(data.getRewordAmount());
          }
        });
      }
    });
  }


}
