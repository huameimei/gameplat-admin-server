package com.gameplat.admin;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.admin.service.RechargeOrderHistoryService;
import com.gameplat.common.util.DateUtils;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class AdminApplicationTests {

  private static final String EST4_BEGIN = " 12:00:00";
  private static final String EST4_END = " 11:59:59";
  @Autowired private ActivityQualificationService activityQualificationService;
  @Autowired private RechargeOrderHistoryService rechargeOrderHistoryService;
  @Autowired private ActivityLobbyService activityLobbyService;

  @Test
  public void testCount() {
    log.info("资料表有={}条记录", activityQualificationService.count());
  }

  /** 生成红包雨资格 */
  @Test
  public void activityRedEnvelopeQualification() {
    activityQualificationService.activityRedEnvelopeQualification();
  }

  @Test
  public void query() {
    Date yesterday = DateUtils.yesterday();
    String start = DateUtils.formatDateTime(yesterday) + EST4_BEGIN;
    String end = DateUtils.formatDateTime(yesterday) + EST4_END;
    QueryWrapper<RechargeOrderHistory> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .select(
            "IFNULL(sum(amount),0) as dayTotalAmount,member_id as memberId,account,member_level as memberLevel")
        .eq("member_type", "M")
        .eq("status", 3)
        .eq("point_flag", 1)
        .between("audit_time", start, end)
        .groupBy("member_id,member_level")
        .having("sum(amount)>0");
    List<Map<String, Object>> list = rechargeOrderHistoryService.listMaps(queryWrapper);
  }

  /** 采用resultMap方式查询属性为另一张表的数据 activity_lobby 和 activity_lobby_discount 为 1:N */
  @Test
  public void getActivityLobbyVOById() {
    Long activityLobbyId = 1L;
    ActivityLobbyVO test = activityLobbyService.getActivityLobbyVOById(activityLobbyId);
    log.info(test.toString());
  }

  @Test
  public void testJson() {
    String redPacketConfig =
        "{\"activityBalcklist\":\"xiaosheng,liming,jdkjsdksdjlakgdj laksgj oiqtl ;kjlkg jkhflhgurhi lkjfgs bgjhskb fgastwe\",\"dmlMultiple\":2,\"redenvelopeBeginTime\":\"14:00\",\"redenvelopeChat\":\"-1\",\"redenvelopeEndTime\":\"23:30\",\"redenvelopeIsAutoDistribute\":true,\"redenvelopeIsIpLimit\":\"true\",\"redenvelopeMoneyMax\":99999,\"redenvelopeMoneyMim\":1,\"rotarySwitchBeginTime\":\"12:00:00\",rotarySwitchEndTime\":\"23:00:00\",\"weekendRedenvelopeDay\":\"7,1\",\"weekendRedenvelopeId\":null}";
    ActivityRedPacketConfigVO configVO =
        JSON.parseObject(redPacketConfig, ActivityRedPacketConfigVO.class);
  }
}
