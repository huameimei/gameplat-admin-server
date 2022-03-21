package com.gameplat.admin.service;

import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.model.entity.activity.ActivityLobby;
import com.gameplat.model.entity.activity.ActivityQualification;

import java.util.Date;
import java.util.List;

/** 活动公共处理类 */
public interface ActivityCommonService {

  /**
   * 获取审核说明信息
   *
   * @param activityLobby ActivityLobby
   * @param statisticValue String
   * @param validAmount String
   * @param startTime String
   * @param endTime String
   * @return String
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
   * @param memberInfo MemberInfoVO
   * @param flagCheck int
   */
  void userDetection(MemberInfoVO memberInfo, int flagCheck);

  /**
   * 活动信息检测
   *
   * @param activityId Long
   * @param countDate Date
   * @param flagCheck int
   * @return ActivityLobby
   */
  ActivityLobby activityDetection(Long activityId, Date countDate, int flagCheck);

  /**
   * 黑名单信息检测
   *
   * @param activityLobby ActivityLobby
   * @param memberInfo MemberInfoVO
   * @param i int
   */
  void blacklistDetection(ActivityLobby activityLobby, MemberInfoVO memberInfo, int i);

  /**
   * 活动资格检测
   *
   * @param activityLobby ActivityLobby
   * @param memberInfo MemberInfoVO
   * @param countDate Date
   * @param i int
   */
  void qualificationDetection(
      ActivityLobby activityLobby, MemberInfoVO memberInfo, Date countDate, int i);

  /**
   * 活动规则检测
   *
   * @param activityLobby ActivityLobby
   * @param countDate Date
   * @param memberInfo MemberInfoVO
   * @param i int
   * @return List
   */
  List<ActivityQualification> activityRuleDetection(
      ActivityLobby activityLobby, Date countDate, MemberInfoVO memberInfo, int i);
}
