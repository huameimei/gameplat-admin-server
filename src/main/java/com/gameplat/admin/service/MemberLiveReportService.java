package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportVo;
import com.gameplat.model.entity.member.MemberDayReport;

public interface MemberLiveReportService extends IService<MemberDayReport> {
  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  Page<MemberLiveReportVo> queryPage(PageDTO<MemberLiveReportDto> page, MemberLiveReportDto dto);
}
