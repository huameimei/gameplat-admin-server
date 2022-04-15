package com.gameplat.admin.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameMemberReportMapper;
import com.gameplat.admin.model.dto.DepositReportDto;
import com.gameplat.admin.model.dto.MemberDayReportDto;
import com.gameplat.admin.model.dto.MemberReportDto;
import com.gameplat.admin.model.dto.MemberbetAnalysisdto;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.GameMemberReportService;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.model.entity.member.MemberDayReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author kb @Date 2022/2/8 18:27 @Version 1.0
 */
@Log4j2
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameMemberReportServiceImpl
    extends ServiceImpl<GameMemberReportMapper, MemberDayReport>
    implements GameMemberReportService {

  @Autowired private GameMemberReportMapper gameMemberReportMapper;

  @Override
  public PageDtoVO<MemberDayReportVo> findSumMemberDayReport(
      Page<MemberDayReportVo> page, MemberDayReportDto memberDayReportDto) {
    // 查询充提报表
    Page<MemberDayReport> memberDayReportPage =
        gameMemberReportMapper.findMemberDayReportPage(page, memberDayReportDto);
    List<MemberDayReportVo> memberDayReportVos =
        BeanUtils.mapList(memberDayReportPage.getRecords(), MemberDayReportVo.class);

    PageDtoVO<MemberDayReportVo> pageDtoVO = new PageDtoVO<>();
    // 查询总计
    Map<String, Object> sumMemberDayReport =
        gameMemberReportMapper.findSumMemberDayReport(memberDayReportDto);
    log.info("充提总计：{}", sumMemberDayReport);

    Page<MemberDayReportVo> resultPage = new Page<>();
    resultPage.setRecords(memberDayReportVos);
    pageDtoVO.setOtherData(sumMemberDayReport);
    pageDtoVO.setPage(resultPage);
    pageDtoVO.getPage().setTotal(memberDayReportPage.getTotal());
    pageDtoVO.getPage().setSize(page.getSize());
    pageDtoVO.getPage().setCurrent(page.getCurrent());

    return pageDtoVO;
  }

  @Override
  public PageDtoVO<MemberRWReportVo> findSumMemberRWReport(
      Page<MemberRWReportVo> page, DepositReportDto depositReportDto) {
    Page<MemberRWReportVo> memberRWReportPage =
        gameMemberReportMapper.findMemberRWReport(page, depositReportDto);
    Map<String, Object> sumMemberRWReport =
        gameMemberReportMapper.findSumMemberRWReport(depositReportDto);
    if (sumMemberRWReport != null) {
      Object totailRechargeAmount = sumMemberRWReport.get("totailRechargeAmount");
      Object totailWithdrawAmount = sumMemberRWReport.get("totailWithdrawAmount");
      BigDecimal totalRWAmount =
          (totailRechargeAmount == null
                  ? Convert.toBigDecimal(0)
                  : Convert.toBigDecimal(totailRechargeAmount))
              .subtract(
                  totailWithdrawAmount == null
                      ? Convert.toBigDecimal(0)
                      : Convert.toBigDecimal(totailWithdrawAmount));
      sumMemberRWReport.put("totalRWAmount", totalRWAmount);
    }
    PageDtoVO<MemberRWReportVo> pageDtoVO = new PageDtoVO<>();
    pageDtoVO.setPage(memberRWReportPage);
    pageDtoVO.setOtherData(sumMemberRWReport);
    return pageDtoVO;
  }

  @Override
  public PageDtoVO<MemberGameDayReportVo> findSumMemberGameDayReport(
      Page<MemberGameDayReportVo> page, MemberReportDto memberReportDto) {
    Page<MemberGameDayReportVo> memberGameDayReport =
        gameMemberReportMapper.findMemberGameDayReport(page, memberReportDto);
    Map<String, Object> sumMemberGameDayReport =
        gameMemberReportMapper.findSumMemberGameDayReport(memberReportDto);
    PageDtoVO<MemberGameDayReportVo> pageDtoVO = new PageDtoVO<>();
    pageDtoVO.setPage(memberGameDayReport);
    pageDtoVO.setOtherData(sumMemberGameDayReport);
    return pageDtoVO;
  }

  @Override
  public MemberbetAnalysisVo findMemberbetAnalysis(MemberbetAnalysisdto dto) {
    return gameMemberReportMapper.findMemberbetAnalysis(dto);
  }
}
