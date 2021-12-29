package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberStatus;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.IpStatisticsDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.model.vo.IpStatisticsVO;

import java.util.List;

/**
 * @author Lily
 */
public interface MemberStatusMapper extends BaseMapper<MemberStatus> {

    /** 会员ip统计 */
    List<IpStatisticsVO> findStatisticsList(IpStatisticsDTO ipStatisticsDTO);

    /** 会员ip分析 */
    List<IpAnalysisVO> findipAnalysisList(IpAnalysisDTO ipAnalysisDTO);
}
