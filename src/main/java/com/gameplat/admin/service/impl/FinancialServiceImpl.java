package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.CurrencyTypeEnum;
import com.gameplat.admin.enums.OrderNoEnum;
import com.gameplat.admin.mapper.FinancialMapper;
import com.gameplat.admin.model.domain.Financial;
import com.gameplat.admin.model.domain.SysCurrencyRate;
import com.gameplat.admin.model.vo.CashConfigVO;
import com.gameplat.admin.service.FinancialService;
import com.gameplat.admin.service.SysCashConfigService;
import com.gameplat.admin.service.SysCurrencyRateService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author lily
 * @description 流水
 * @date 2021/11/27
 */

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl extends ServiceImpl<FinancialMapper, Financial> implements FinancialService {

    @Autowired private FinancialMapper financialMapper;
    @Autowired private SysCurrencyRateService sysCurrencyRateService;
    @Autowired private SysCashConfigService sysCashConfigService;
    /**
     * 添加流水
     */
    @Override
    public int insert(Financial financial) {
        // 参数校验
        checkFinancialParam(financial);
        financial.setCreateBy("system");
        // 生产订单号
        String orderNo = OrderNoEnum.FINANCIAL.getCode() + RandomUtil.generateOrderCode();
        financial.setFlowNo(orderNo);

        //如果是真币；算出其人民币价格
        if (financial.getCurrencyType() == CurrencyTypeEnum.REAL.getCode()) {
            SysCurrencyRate sysCurrencyRate = sysCurrencyRateService.info();
            if (ObjectUtils.isEmpty(sysCurrencyRate)) {
                throw new ServiceException("操作失败！");
            }
            if (sysCurrencyRate == null || sysCurrencyRate.getRechargeAndroidRate() == null
                    || sysCurrencyRate.getRechargeAndroidRate().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("操作失败！");
            }
            financial.setAmountCny(financial.getAmount().divide(sysCurrencyRate.getRechargeAndroidRate(), 2,
                    BigDecimal.ROUND_HALF_UP));
        }

        if (financial.getCurrencyType() == CurrencyTypeEnum.TOKEN.getCode()
                || financial.getCurrencyType() == CurrencyTypeEnum.FREEZE.getCode()) {
            //提现配置
            CashConfigVO sysCashConfigVO = sysCashConfigService.info(financial.getUserId());
            if (sysCashConfigVO == null) {
                throw new ServiceException("操作失败！");
            }
            if (sysCashConfigVO.getRate() == null || sysCashConfigVO.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("操作失败！");
            }
            financial.setAmountCny(financial.getAmount().multiply(sysCashConfigVO.getRate()));
        }

        // 插入数据
        financial.setCreateTime(new Date());
        Integer type = 15;
        Integer types = 16;
        if(type.equals(financial.getSourceType()) || types.equals(financial.getSourceType())) {
            financial.setCreateBy(financial.getUsername());
        } else {
            financial.setCreateBy("system");
        }
        int row = financialMapper.saveFinancial(financial);
        if (row != 1) {
            throw new ServiceException("数据插入失败！");
        }
        return row;
    }

    @Override
    public void checkFinancialParam(Financial financial) {

        if (StringUtils.isBlank(financial.getUsername())) {
            throw new ServiceException("用户名不能为空！");
        }
        if (financial.getUserId() == null) {
            throw new ServiceException("用户id不能为空！");
        }

        if (financial.getSourceType() == null) {
            throw new ServiceException("来源类型不能为空！");
        }

        if (StringUtils.isBlank(financial.getSourceId())) {
            throw new ServiceException("来源id不能为空！");
        }

        if (StringUtils.isBlank(financial.getCurrencyName())) {
            throw new ServiceException("货币名称不能为空！");
        }

        if (financial.getCurrencyType() == null) {
            throw new ServiceException("货币种类不能为空!");
        }

        if (financial.getAmount() == null) {
            throw new ServiceException("金额不能为空!");
        }

        if (StringUtils.isBlank(financial.getRemark())) {
            financial.setRemark("会员订单号："+financial.getSourceId());
        }
        if (StringUtils.isBlank(financial.getIpAddress())) {
            throw new ServiceException("ip地址不能为空!");
        }

        if (financial.getIsLess() == null) {
            throw new ServiceException("是否扣除资产不能为空!");
        }

        if (financial.getIsLess() != 0 && financial.getIsLess() != 1) {
            throw new ServiceException("是否扣除资产输入有误!");
        }

        if (financial.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("金额不能小于0!");
        }

        Integer stat = 0;
    }

    @Override
    public Map getWithdrawtRanslateAmount(Long userId) {
        Map withdrawtRanslateAmount = financialMapper.getWithdrawtRanslateAmount(userId);
        return withdrawtRanslateAmount;
    }

}
