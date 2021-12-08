package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveMemberDayReport;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.dto.LiveMemberDayReportQueryDTO;
import com.gameplat.admin.model.vo.LiveMemberDayReportVO;
import com.gameplat.admin.model.vo.LiveReportVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LiveMemberDayReportMapper extends BaseMapper<LiveMemberDayReport> {


  int getDayCount(@Param("statTime") String statTime,@Param("tableName") String tableName);

  int saveMemberDayReport(@Param("statTime") String statTime, @Param("gamePlatform") GamePlatform gamePlatform,@Param("tableName") String tableName);

  List<LiveReportVO> queryReportList(LiveMemberDayReportQueryDTO dto);

  List<LiveMemberDayReportVO> findByStatTimeBetweenAndValidBetAmountGtZero(@Param("liveRebatePeriod") LiveRebatePeriod liveRebatePeriod,
      @Param("startDate") String startDate, @Param("endDate") String endDate);
}
