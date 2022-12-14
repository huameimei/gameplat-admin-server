package com.gameplat.admin.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.component.RechargeOrderQueryCondition;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.bean.PayLeaderboard;
import com.gameplat.admin.model.bean.PayLeaderboardParam;
import com.gameplat.admin.model.dto.MemberActivationDTO;
import com.gameplat.admin.model.vo.MemberActivationVO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.admin.model.vo.SalaryRechargeVO;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.spread.SpreadUnion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {




  @Select("select sum(pay_amount) from recharge_order ${ew.customSqlSegment}")
  BigDecimal summaryRechargeOrder(@Param(Constants.WRAPPER) Wrapper<RechargeOrder> wrapper);

  /** 获取充值金额达标的会员账号 */
  List<String> getSatisfyRechargeAccount(
      @Param("minRechargeAmount") String minRechargeAmount,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);

  /**
   * 获取充值金额达标的会员账号 VIP等级配置
   *
   * @param startTime
   * @param endTime
   * @return
   */
  List<String> getWealVipRecharge(@Param("type") Integer type,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);

  /**
   * 查询用户累计充值和累计充值天数
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> findRechargeInfo(Map<String, Object> map);

  /**
   * 统计用户充值记录
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> findRechargeDateList(Map<String, Object> map);

  /**
   * 统计首充金额
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> findFirstRechargeAmount(Map<String, Object> map);

  /**
   * 统计二充金额
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> findTwoRechargeAmount(Map<String, Object> map);

  /** 根据会员和最后修改时间获取充值次数、充值金额、充值优惠、其它优惠 */
  MemberActivationVO getRechargeInfoByNameAndUpdateTime(MemberActivationDTO memberActivationDTO);

  /** 根据代理线获取时间段内的充值数据 */
  List<JSONObject> getSpreadReport(
      @Param("list") List<SpreadUnion> list,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);

  List<SalaryRechargeVO> getRechargeForSalary(
      @Param("startDate") String startDate,
      @Param("endDate") String endDate,
      @Param("agentName") String agentName,
      @Param("isInclude") Integer isInclude);

  List<PayLeaderboard> getLeaderboard(PayLeaderboardParam payLeaderboardParam);

  /**
   * 查询用户的首充金额记录
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> findFirstRechargeOrderList(Map<String, Object> map);

  IPage<RechargeOrderVO> findPage(Page<RechargeOrder> page, @Param(Constants.WRAPPER)QueryWrapper<RechargeOrder> wrapper);
}
