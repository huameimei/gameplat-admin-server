package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberLiveReportMapper;
import com.gameplat.admin.model.dto.MemberLiveReportDto;
import com.gameplat.admin.model.vo.MemberLiveReportVo;
import com.gameplat.admin.service.MemberLiveReportService;
import com.gameplat.model.entity.member.MemberDayReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberLiveReportServiceImpl
    extends ServiceImpl<MemberLiveReportMapper, MemberDayReport>
    implements MemberLiveReportService {

  @Autowired private MemberLiveReportMapper memberLiveReportMapper;

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
}
