package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportExportVo;
import com.gameplat.admin.model.vo.MemberLiveReportVo;
import com.gameplat.model.entity.member.MemberDayReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberLiveReportMapper extends BaseMapper<MemberDayReport> {
  Page<MemberLiveReportVo> pageList(
      @Param("page") PageDTO<MemberLiveReportDto> page, @Param("dto") MemberLiveReportDto dto);

  Integer exportCount(@Param("dto") MemberLiveReportDto dto);

  List<MemberLiveReportExportVo> list(@Param("dto") MemberLiveReportDto dto);
}
