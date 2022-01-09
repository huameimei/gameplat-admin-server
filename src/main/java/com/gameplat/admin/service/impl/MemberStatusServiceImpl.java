package com.gameplat.admin.service.impl;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.domain.MemberStatus;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.QueryIpStatReportDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.service.MemberStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lily
 * @description 会员ip统计
 * @date 2021/12/28
 */

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberStatusServiceImpl extends ServiceImpl<MemberStatusMapper, MemberStatus> implements MemberStatusService {

    @Autowired
    private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

    @Autowired
    private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Override
    public List<IpStatisticsVO> findStatisticsList(QueryIpStatReportDTO dto) {

        Optional.of(dto.getIpType()).orElseThrow(() -> new ServiceException("查询参数IP类型不可为空"));

        List<IpStatisticsVO> list = new ArrayList<>();

        if (dto.getIpType() == 1){
            //注册IP统计
            list = memberMapper.findIp(dto);
        } else if (dto.getIpType() == 2){
            //登录IP统计
            list = memberInfoMapper.findIp(dto);
        } else if (dto.getIpType() == 3){
            //充值IP统计
            list = rechargeOrderHistoryMapper.findIp(dto);
        }else if (dto.getIpType() == 4){
            //提现IP统计
            list = memberWithdrawHistoryMapper.findIp(dto);
        }
        return list;
    }

    @Override
    public List<IpAnalysisVO> findIpAnalysisList(IpAnalysisDTO ipAnalysisDTO) {
       return null;
    }
}
