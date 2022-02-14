package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberDayReport;
import com.gameplat.admin.model.dto.DepositReportDto;
import com.gameplat.admin.model.dto.MemberDayReportDto;
import com.gameplat.admin.model.vo.MemberDayReportVo;
import com.gameplat.admin.model.vo.MemberRWReportVo;
import com.gameplat.admin.model.vo.PageDtoVO;


public interface GameMemberReportService extends IService<MemberDayReport> {


    PageDtoVO<MemberDayReportVo> findSumMemberDayReport(Page<MemberDayReportVo> page, MemberDayReportDto memberDayReportDto);


    PageDtoVO<MemberRWReportVo> findSumMemberRWReport(Page<MemberRWReportVo> page, DepositReportDto depositReportDto);



}
