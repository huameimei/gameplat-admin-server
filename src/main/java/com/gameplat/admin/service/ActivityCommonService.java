package com.gameplat.admin.service;

import com.gameplat.admin.model.bean.ActivityMemberInfo;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;

import java.util.Date;
import java.util.List;

/**
 * 活动公共处理类
 */
public interface ActivityCommonService {

    String getAuditRemark(ActivityLobby activityLobby, String statisticValue, String validAmount, String startTime, String endTime);


    void userDetection(ActivityMemberInfo activityMemberInfo, int flagCheck);


    ActivityLobby activityDetection(Long activityId, Date countDate, int flagCheck);

    void blacklistDetection(ActivityLobby activityLobby, ActivityMemberInfo activityMemberInfo, int i);

    void qualificationDetection(ActivityLobby activityLobby, ActivityMemberInfo activityMemberInfo, Date countDate, int i);

    List<ActivityQualification> activityRuleDetection(ActivityLobby activityLobby, Date countDate, List<ActivityMemberInfo> activityUserInfoList, int i);
}