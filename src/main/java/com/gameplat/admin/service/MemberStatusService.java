package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberStatus;
import com.gameplat.admin.model.dto.IpStatisticsDTO;
import com.gameplat.admin.model.vo.IpStatisticsVO;

import java.util.List;

public interface MemberStatusService extends IService<MemberStatus> {

    List<IpStatisticsVO> findStatisticsList(IpStatisticsDTO ipStatisticsDTO);
}
