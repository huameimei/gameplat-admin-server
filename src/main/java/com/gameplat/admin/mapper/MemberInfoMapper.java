package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.dto.QueryIpStatReportDTO;
import com.gameplat.admin.model.vo.IpStatisticsVO;

import java.util.List;

public interface MemberInfoMapper extends BaseMapper<MemberInfo> {

    /** 登录IP统计 */
    List<IpStatisticsVO> findIp(QueryIpStatReportDTO dto);
}
