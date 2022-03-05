package com.gameplat.admin.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.DepositReportDto;
import com.gameplat.admin.model.dto.MemberDayReportDto;
import com.gameplat.admin.model.dto.MemberReportDto;
import com.gameplat.admin.model.dto.MemberbetAnalysisdto;
import com.gameplat.admin.model.vo.*;
import com.gameplat.model.entity.member.MemberDayReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author kb @Date 2022/2/8 18:31 @Version 1.0
 */
public interface GameMemberReportMapper extends BaseMapper<MemberDayReport> {

  Page<MemberDayReport> findMemberDayReportPage(
      Page<MemberDayReportVo> page, @Param("dto") MemberDayReportDto dto);

  Map<String, Object> findSumMemberDayReport(MemberDayReportDto dto);

  Page<MemberRWReportVo> findMemberRWReport(
      Page<MemberRWReportVo> page, @Param("dto") DepositReportDto depositReportDto);

  Map<String, Object> findSumMemberRWReport(DepositReportDto depositReportDto);

  Page<MemberGameDayReportVo> findMemberGameDayReport(
      Page<MemberGameDayReportVo> page, @Param("dto") MemberReportDto memberReportDto);

  Map<String, Object> findSumMemberGameDayReport(MemberReportDto memberReportDto);

  /**
   * 获取达到有效投注金额的会员账号
   *
   * @param minBetAmount
   * @param startTime
   * @param endTime
   * @return
   */
  List<String> getSatisfyBetAccount(
      @Param("minBetAmount") String minBetAmount,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);

  /** 获取数据（详细） */
  List<JSONObject> getSpreadReportInfo(
      @Param("account") String account,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);

  /** 获取数据（总报表） */
  List<JSONObject> getSpreadReport(
      @Param("list") List<SpreadUnionVO> list,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);

  /**
   * 投注分析
   *
   * @param dto
   * @return
   */
  MemberbetAnalysisVo findMemberbetAnalysis(MemberbetAnalysisdto dto);
}
