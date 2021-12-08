package com.gameplat.admin.service;

import com.gameplat.admin.mapper.MemberGrowupTotalrecordMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.domain.MemberGrowupTotalrecord;
import com.gameplat.admin.model.domain.SysCashConfig;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysLayerConfig;
import com.gameplat.admin.model.vo.CashConfigVO;
import com.gameplat.base.common.util.BeanUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class SysCashConfigService {


    @Autowired private SysDictDataMapper sysDictDataMapper;
    @Autowired private FinancialService financialService;
    @Autowired private MemberGrowupTotalrecordMapper growupTotalrecordMapper;
    @Autowired private SysLayerConfigService sysLayerConfigService;


	/**
	 * 获取配置信息
	 */
	public CashConfigVO info(Long userId) {
        //查询配置信息
        int withdrawLimit = getWithdrawLimit(userId);
        // 根据key获取配置
        List<SysDictData> user_cash_config = sysDictDataMapper.findDataByType("USER_CASH_CONFIG", "");
        SysCashConfig sysCashConfig =  new SysCashConfig();
        Map s = financialService.getWithdrawtRanslateAmount(userId);
        if (user_cash_config != null) {
            Integer finalWithdrawLimit = withdrawLimit;
            user_cash_config.forEach(a ->{
                if ("singleCashMoneyLowerLimit".equals(a.getDictLabel())){
                    sysCashConfig.setWithdrawOneAmount(new BigDecimal (a.getDictValue()));
                }

                if ("singleCashMoneyUpperLimit".equals(a.getDictLabel())){
                    sysCashConfig.setWithdrawOneMaxAmount(new BigDecimal (a.getDictValue()));
                }

                if ("oddDaysCashTriesLimit".equals(a.getDictLabel())) {
                    if (finalWithdrawLimit > 0) {
                        sysCashConfig.setWithdrawCount(new BigDecimal(finalWithdrawLimit).subtract(new BigDecimal(s.get("num").toString())));
                    } else {
                        sysCashConfig.setWithdrawCount(new BigDecimal(a.getDictValue()).subtract(new BigDecimal(s.get("num").toString())));
                    } }

                if ("oddDaysCashMoneyUpperLimit".equals(a.getDictLabel())) {
                    sysCashConfig.setWithdrawMaxAmount(new BigDecimal(a.getDictValue()));
                    BigDecimal bigDecimal = new BigDecimal(a.getDictValue());
                    log.info("系统当日金额提现金额上限：{} ====  用户提现金额：{}",a.getDictValue(),s.get("amount"));
                    sysCashConfig.setWithdrawtRanslateAmount(bigDecimal.subtract(new BigDecimal(s.get("amount").toString())));
                }
                if ("rate".equals(a.getDictLabel())) {
                    sysCashConfig.setRate(new BigDecimal(a.getDictValue()));
                }
                if ("repetitionCashLimit".equals(a.getDictLabel())) {
                    sysCashConfig.setIsRepeat(a.getDictValue());
                }

                if ("usdtCode".equals(a.getDictLabel())) {
                    sysCashConfig.setUsdtCode(a.getDictValue());
                }
                if ("bankCode".equals(a.getDictLabel())) {
                    sysCashConfig.setBankCode(a.getDictValue());
                }
                if ("bankSwith".equals(a.getDictLabel())) {
                    sysCashConfig.setBankSwith(a.getDictValue());
                }
                if ("usdtSwith".equals(a.getDictLabel())) {
                    sysCashConfig.setUsdtSwith(a.getDictValue());
                }
            });
        }


        return BeanUtils.map(sysCashConfig, CashConfigVO.class);
	}



	/** 查询配置信息 */
	public int getWithdrawLimit(Long userId) {
        Integer limit = 1;
        Integer withdrawLimit = 0;
        try {
            //根据用户id查询用户当前的等级
            //查询当前的用户的汇总的充值成长值
            MemberGrowupTotalrecord memberGrowUTotal = growupTotalrecordMapper.searchGrowUpToalByMemberId(userId);
            SysLayerConfig layer = sysLayerConfigService.findUserLevel(memberGrowUTotal.getLevelNow());

            if (layer != null) {
                if (limit.equals(layer.getWithdrawLimit())) {
                    withdrawLimit = withdrawLimit + layer.getMemberOutTime().intValue();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("查询配置信息：{}",e);
            return withdrawLimit;
        }

        return withdrawLimit;
    }

}
