package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.member.MemberDayReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 会员日报表 */
public interface MemberDayReportMapper extends BaseMapper<MemberDayReport> {

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
}
