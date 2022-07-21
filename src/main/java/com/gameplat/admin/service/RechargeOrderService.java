package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.bean.ManualRechargeOrderBo;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.bean.RechargeMemberFileBean;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.model.dto.ManualRechargeOrderDto;
import com.gameplat.admin.model.dto.MemberActivationDTO;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.spread.SpreadUnion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RechargeOrderService extends IService<RechargeOrder> {

  PageExt<RechargeOrderVO, SummaryVO> findPage(Page<RechargeOrder> page, RechargeOrderQueryDTO dto);

  void updateDiscount(
      Long id, Integer discountType, BigDecimal discountAmount, BigDecimal discountDml);

  void updateRemarks(Long id, String auditRemarks);

  void handle(Long id);

  void unHandle(Long id);

  void accept(Long id, String auditRemarks, boolean flag);

  void cancel(Long id);

  void updateStatus(Long id, Integer curStatus, Integer newStatus);

  void manual(ManualRechargeOrderBo manualRechargeOrderBo, UserEquipment equipment);

  /**
   * 查询每天充值的金额
   *
   * @param map Map
   * @return List
   */
  List<ActivityStatisticItem> findRechargeDateList(Map<String, Object> map);

  /**
   * 查询所有首次充值的金额
   *
   * @param map Map
   * @return List
   */
  List<ActivityStatisticItem> findAllFirstRechargeAmount(Map<String, Object> map);

  List<ActivityStatisticItem> findAllTwoRechargeAmount(Map<String, Object> map);

  /** 根据会员和最后修改时间获取充值次数、充值金额、充值优惠、其它优惠 */
  MemberActivationVO getRechargeInfoByNameAndUpdateTime(MemberActivationDTO dto);

  /** 获取时间段内某代理下的所有充值成功数据 */
  List<JSONObject> getSpreadReport(List<SpreadUnion> list, String startTime, String endTime);

  List<ThreeRechReportVo> findThreeRechReport(GameRWDataReportDto dto);

  long getUntreatedRechargeCount();

  void expired(Long id);

  void batchMemberRecharge(
      UserEquipment clientInfo,
      List<RechargeMemberFileBean> strAccount,
      ManualRechargeOrderDto dto);

  void batchFileMemberRecharge(
      List<MemberRechBalanceVO> list, Integer discountType, UserEquipment userEquipment);
}
