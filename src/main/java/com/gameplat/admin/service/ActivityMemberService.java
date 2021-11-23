package com.gameplat.admin.service;

import java.util.List;
import java.util.Map;

/**
 * 活动用户关联业务
 *
 * @Author: lyq
 * @Date: 2020/8/21 14:15
 * @Description:
 */
public interface ActivityMemberService {

    //查询用户状态
    List<UserStatusVO> findUserStatusList(List<Long> userId);

    //查询用户等级
    List<UserRankVO1> findUserRankList(List<Long> userId);

    //查询用户账号
    List<UsernameVO> findUsernameList(List<Long> userId);


    //根据ID查询活动需要用户的信息
    List<ActivityUserInfoVO> findActivityUserInfo(Map map);

    //获取活动需要的会员日报表数据
    List<ActivityStatisticItemVO> findMemberDayReport(Map map);

    //新增福利记录
    void insertWealReword(UserWealReword userGrowthReword);

    //新增系统回复用户消息
    void addSysInformation(SysInformation sysInformation);
}
