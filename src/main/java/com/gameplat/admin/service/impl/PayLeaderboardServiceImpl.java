package com.gameplat.admin.service.impl;

import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.mapper.RechargeOrderMapper;
import com.gameplat.admin.model.bean.PayLeaderboard;
import com.gameplat.admin.model.bean.PayLeaderboardParam;
import com.gameplat.admin.model.bean.PayLeaderboardResult;
import com.gameplat.admin.model.bean.PayLeaderboardSearch;
import com.gameplat.admin.service.PayLeaderboardService;
import com.gameplat.admin.service.PayTypeService;
import com.gameplat.admin.service.TpMerchantService;
import com.gameplat.model.entity.pay.PayType;
import com.gameplat.model.entity.pay.TpMerchant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zak
 * @Date 2022/01/18 20:03:44
 */
@Service
@RequiredArgsConstructor
public class PayLeaderboardServiceImpl implements PayLeaderboardService {

    private final TpMerchantService tpMerchantService;

    private final PayTypeService payTypeService;

    private final RechargeOrderMapper rechargeOrderMapper;

    @Override
    public PayLeaderboardSearch getLeaderboardSearch() {
        PayLeaderboardSearch payLeaderboardSearch = new PayLeaderboardSearch();
        // 入款商户
        List<TpMerchant> list = tpMerchantService.lambdaQuery().groupBy(TpMerchant::getTpInterfaceCode).list();
        List<Map<String, String>> interfaceList = list.stream().map(d -> {
            Map<String, String> interfaceMap = new HashMap<>();
            interfaceMap.put("interfaceName", d.getName());
            interfaceMap.put("interfaceCode", d.getTpInterfaceCode());
            return interfaceMap;
        }).collect(Collectors.toList());
        payLeaderboardSearch.setInterfaceList(interfaceList);
        // 支付类型
        List<PayType> typeList = payTypeService.lambdaQuery()
                .eq(PayType::getStatus, TrueFalse.TRUE.getValue())
                .list();
        List<Map<String, String>> payTypeList = typeList.stream().map(p -> {
            Map<String, String> payTypeMap = new HashMap<>();
            payTypeMap.put("payTypeName", p.getName());
            payTypeMap.put("payTypeCode", p.getCode());
            return payTypeMap;
        }).collect(Collectors.toList());
        payLeaderboardSearch.setPayTypeList(payTypeList);
        return payLeaderboardSearch;
    }

    @Override
    public PayLeaderboardResult getLeaderboard(PayLeaderboardParam payLeaderboardParam) {
        List<PayLeaderboard> result = rechargeOrderMapper.getLeaderboard(payLeaderboardParam);
        // 计算自动成功率
        result.forEach(p -> {
            BigDecimal autoSucc = new BigDecimal(p.getAutoSuccessOrderNum());
            BigDecimal totalOrderNum = new BigDecimal(p.getTotalOrderNum());
            p.setInterfaceUseNum(p.getTotalOrderNum());
            // 分母不能为0
            if (totalOrderNum.compareTo(BigDecimal.ZERO) == 0) {
                p.setAutoSuccessRate(BigDecimal.ZERO);
            } else {
                BigDecimal autoSuccessRate = autoSucc.divide(totalOrderNum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                p.setAutoSuccessRate(autoSuccessRate);
            }
        });
        // 排行榜数量
        Integer rankCount = payLeaderboardParam.getRankCount();
        // 使用平台最多
        List<PayLeaderboard> usePlatform = getResultList(result, Comparator.comparing(PayLeaderboard::getInterfaceUseNum), rankCount);
        // 成功支付自动入款率
        List<PayLeaderboard> successPayRate = getResultList(result, Comparator.comparing(PayLeaderboard::getAutoSuccessRate), rankCount);
        // 成功笔数最多
        List<PayLeaderboard> successPayNum = getResultList(result, Comparator.comparing(PayLeaderboard::getAutoSuccessOrderNum), rankCount);
        // 成功金额最多
        List<PayLeaderboard> successPayAmount = getResultList(result, Comparator.comparing(PayLeaderboard::getAutoSuccessAmount), rankCount);
        return new PayLeaderboardResult() {{
            setUsePlatform(usePlatform);
            setSuccessPayRate(successPayRate);
            setSuccessPayNum(successPayNum);
            setSuccessPayAmount(successPayAmount);
        }};
    }

    /**
     * 获取结果集
     *
     * @return java.util.List<com.live.cloud.client.model.financial.po.PayLeaderboard>
     * @Author zak
     * @Date 2022/1/19 19:44
     * @Param list 待处理列表
     * @Param comparing 排序字段比较器
     * @Param rankCount 排行榜数量
     */
    List<PayLeaderboard> getResultList(List<PayLeaderboard> list, Comparator<PayLeaderboard> comparing, int rankCount) {
        List<PayLeaderboard> resultList = new ArrayList<>();
        CollectionUtils.addAll(resultList, new Object[list.size()]);
        Collections.copy(resultList, list);
        resultList.sort(comparing.reversed());
        if(list.size() < rankCount){
            return resultList;
        }
        return resultList.subList(0, rankCount);
    }
}
