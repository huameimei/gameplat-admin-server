package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.ActivityLobbyDTO;

/**
 * 活动公共处理类
 */
public interface ActivityCommonService {

    String getAuditRemark(ActivityLobbyDTO activityLobby, String statisticValue, String validAmount, String startTime, String endTime);


}