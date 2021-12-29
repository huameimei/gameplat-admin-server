package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberStatusMapper;
import com.gameplat.admin.model.domain.MemberStatus;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.IpStatisticsDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.service.MemberStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lily
 * @description 会员ip统计
 * @date 2021/12/28
 */

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberStatusServiceImpl extends ServiceImpl<MemberStatusMapper, MemberStatus> implements MemberStatusService {

    @Autowired
    private MemberStatusMapper memberStatusMapper;

    @Override
    public List<IpStatisticsVO> findStatisticsList(IpStatisticsDTO ipStatisticsDTO) {
        return memberStatusMapper.findStatisticsList(ipStatisticsDTO);
    }

    @Override
    public List<IpAnalysisVO> findIpAnalysisList(IpAnalysisDTO ipAnalysisDTO) {
        return memberStatusMapper.findIpAnalysisList(ipAnalysisDTO);
    }
}
