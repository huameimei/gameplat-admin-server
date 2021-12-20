package com.gameplat.admin.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.RechargeOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {

    @Select("select sum(amount) from recharge_order ${ew.customSqlSegment}")
    BigDecimal summaryRechargeOrder(@Param(Constants.WRAPPER) Wrapper<RechargeOrder> wrapper);

/*
  @Select("select sum(amount) from recharge_order where status = #{status}")
  RechargeSummaryVO summaryRechargeOrder(@Param("status") Integer status);
*/

    /**
     * 获取充值金额达标的会员账号
     */
    List<String> getSatisfyRechargeAccount(@Param("minRechargeAmount") String minRechargeAmount,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime);

    /**
     * 查询用户累计充值和累计充值天数
     *
     * @param map
     * @return
     */
    List<ActivityStatisticItem> findRechargeInfo(Map map);

    /**
     * 统计用户充值记录
     *
     * @param map
     * @return
     */
    List<ActivityStatisticItem> findRechargeDateList(Map map);

    /**
     * 统计首充金额
     *
     * @param map
     * @return
     */
    List<ActivityStatisticItem> findFirstRechargeAmount(Map map);
}
