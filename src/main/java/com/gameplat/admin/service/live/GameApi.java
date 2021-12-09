package com.gameplat.admin.service.live;

import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.RandomUtil;
import com.gameplat.common.live.LiveGameResult;
import com.gameplat.common.live.TransferResource;
import java.math.BigDecimal;

public interface GameApi {
    String Suffix = "Api";

    BigDecimal getBalance(String account) throws Exception;

    TransferResource transfer(String account, BigDecimal amount, String orderNum) throws Exception;

    String free(String gameType, String ip, Boolean isMobile, String url) throws Exception;

    String play(String account, String gameType, String ip, Boolean isMobile, String url) throws Exception;

    void isOpen() throws Exception;

    /**
     * 额度转换订单号生成
     * 预留 订单生成参数
     */
    default String generateOrderNo(Object... param) throws Exception {
        return "G" + RandomUtil.generateOrderCode();
    }

    /**
     * 额度转换查询
     */
    default boolean queryOrderStatus(String orderNo,String account,BigDecimal amount)throws Exception {
        throw new ServiceException("默认订单查询异常");
    }

    default LiveGameResult getGameResult(String billNo) throws Exception{
        return new LiveGameResult();
    }
}