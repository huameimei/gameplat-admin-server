package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.IpAnalysisEnum;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.MemberWithdrawHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.service.IpAnalysisService;
import com.gameplat.base.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lily
 * @description ip分析
 * @date 2022/1/19
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class IpAnalysisServiceImpl implements IpAnalysisService {

    @Autowired
    private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

    @Autowired
    private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "token:web:";

    /** ip分析 */
    @Override
    public IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto) {
        IPage<IpAnalysisVO> pagelist = new PageDTO<>();
        List<IpAnalysisVO> list = new ArrayList<>();

        if (Objects.equals(dto.getType(), null)) {
            throw new ServiceException("分析类型不能为空");
        }
    if (IpAnalysisEnum.REGISTER.getCode().equals(dto.getType())) {
            // 注册
            pagelist = memberMapper.page(page, dto);
            list = pagelist.getRecords();
        } else if (IpAnalysisEnum.LOGIN.getCode().equals(dto.getType())) {
            // 登录
        } else if (IpAnalysisEnum.RECHARGE.getCode().equals(dto.getType())) {
            // 充值
            pagelist = rechargeOrderHistoryMapper.page(page, dto);
            list = pagelist.getRecords();
        } else if (IpAnalysisEnum.WITHDRAW.getCode().equals(dto.getType())) {
            pagelist = memberWithdrawHistoryMapper.page(page, dto);
            list = pagelist.getRecords();
        }
        // 判断会员是否在线
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                IpAnalysisVO ipAnalysis = list.get(i);
                if (redisTemplate.hasKey(TOKEN_PREFIX + ipAnalysis.getAccount())) {
                    ipAnalysis.setOffline(1);
                    pagelist.getRecords().set(i, ipAnalysis);
                } else {
                    ipAnalysis.setOffline(0);
                    pagelist.getRecords().set(i, ipAnalysis);
                }
            }
        }
        return pagelist;
    }
}
