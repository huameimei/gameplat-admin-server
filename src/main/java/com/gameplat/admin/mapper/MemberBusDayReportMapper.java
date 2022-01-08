package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberBusDayReport;
import com.gameplat.admin.model.dto.DayReportDTO;
import com.gameplat.admin.model.vo.DayReportVO;

import java.util.List;

public interface MemberBusDayReportMapper extends BaseMapper<MemberBusDayReport> {

   List<DayReportVO> findList(DayReportDTO dto);
}
