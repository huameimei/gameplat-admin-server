package com.gameplat.admin.mapper.activity;


import com.gameplat.admin.model.domain.activity.ActivityPay2;
import com.gameplat.admin.model.domain.activity.MemberActivityLobby;
import com.gameplat.admin.model.vo.activity.ActivitySendVO;
import com.gameplat.admin.model.vo.activity.StaticActivityVO;

import java.util.List;

public interface ActivityPayDao {
    int deleteByPrimaryKey(Long id);

    int insert(ActivityPay2 record);

    int insertSelective(ActivityPay2 record);

    ActivityPay2 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityPay2 record);

    int updateByPrimaryKey(ActivityPay2 record);

    int insertBatchActivityPay(List<ActivitySendVO> list);

    List<StaticActivityVO> findCumulativeUserPay(ActivityPay2 payRecord);

    List<StaticActivityVO> findFirstPayAmount(MemberActivityLobby memberActivityLobby);

    List<StaticActivityVO> findContinuousDays(MemberActivityLobby activityLobby);

    List<ActivityPay2> findCurrentTimePatList(String startTime);

    List<StaticActivityVO> dealFirstDayPay(MemberActivityLobby memberActivityLobby);
}
