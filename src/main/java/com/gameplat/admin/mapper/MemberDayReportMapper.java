package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberDayReport;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 会员日报表
 */
public interface MemberDayReportMapper extends BaseMapper<MemberDayReport> {

    /**
     * 获取达到有效投注金额的会员账号
     *
     * @param minBetAmount
     * @param startTime
     * @param endTime
     * @return
     */
    List<String> getSatisfyBetAccount(@Param("minBetAmount") String minBetAmount,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);

}
