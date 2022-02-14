package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.MemberDayReport;
import com.gameplat.admin.model.dto.DepositReportDto;
import com.gameplat.admin.model.dto.MemberDayReportDto;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.MemberRWReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * @Author kb
 * @Date 2022/2/8 18:31
 * @Version 1.0
 */
public interface GameMemberReportMapper extends BaseMapper<MemberDayReport> {


    Page<MemberDayReport> findMemberDayReportPage(Page<MemberDayReportVo> page, @Param("dto") MemberDayReportDto dto);

    Map<String,Object> findSumMemberDayReport(MemberDayReportDto dto);


    Page<MemberRWReportVo> findMemberRWReport(Page<MemberRWReportVo> page, @Param("dto") DepositReportDto depositReportDto);

    Map<String,Object> findSumMemberRWReport(DepositReportDto depositReportDto);
}
