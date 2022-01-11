package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.vo.MemberInfoVO;

import java.util.Date;
import java.util.List;

/** 活动公共处理类 */
public interface ActivityCommonService {

  /**
   * 获取审核说明信息
   *
   * @param activityLobby
   * @param statisticValue
   * @param validAmount
   * @param startTime
   * @param endTime
   * @return
   */
  String getAuditRemark(
      ActivityLobby activityLobby,
      String statisticValue,
      String validAmount,
      String startTime,
      String endTime);

  /**
   * 用户信息检测
   *
   * @param memberInfo
   * @param flagCheck
   */
  void userDetection(MemberInfoVO memberInfo, int flagCheck);

  /**
   * 活动信息检测
   *
   * @param activityId
   * @param countDate
   * @param flagCheck
   * @return
   */
  ActivityLobby activityDetection(Long activityId, Date countDate, int flagCheck);

  /**
   * 黑名单信息检测
   *
   * @param activityLobby
   * @param memberInfo
   * @param i
   */
  void blacklistDetection(ActivityLobby activityLobby, MemberInfoVO memberInfo, int i);

  /**
   * 活动资格检测
   *
   * @param activityLobby
   * @param memberInfo
   * @param countDate
   * @param i
   */
  void qualificationDetection(
      ActivityLobby activityLobby, MemberInfoVO memberInfo, Date countDate, int i);

  /**
   * 活动规则检测
   *
   * @param activityLobby
   * @param countDate
   * @param memberInfo
   * @param i
   * @return
   */
  List<ActivityQualification> activityRuleDetection(
      ActivityLobby activityLobby, Date countDate, MemberInfoVO memberInfo, int i);
}
