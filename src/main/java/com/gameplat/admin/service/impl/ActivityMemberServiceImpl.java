package com.gameplat.admin.service.impl;

import com.gameplat.admin.service.ActivityMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityMemberServiceImpl implements ActivityMemberService {

    private final UserActivityService userActivityService;

    private final UserWealRewordService userWealRewordService;

    private final SysInformationService sysInformationService;

    //查询用户状态
    public List<UserStatusVO> findUserStatusList(List<Long> userId) {
        return userActivityService.findUserStatusList(userId);
    }

    //查询用户等级
    public List<UserRankVO1> findUserRankList(List<Long> userId) {
        return userActivityService.findUserRankList(userId);
    }

    //查询用户账号
    public List<UsernameVO> findUsernameList(List<Long> userId) {
        return userActivityService.findUsernameList(userId);
    }


    //根据ID查询活动需要用户的信息
    public List<ActivityUserInfoVO> findActivityUserInfo(Map map) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        DyDataSourceContextHolder.setDBSuffix(request.getHeader(HttpHead.DB_SUFFIX));
        return userActivityService.findActivityUserInfo(map);
    }

    //获取活动需要的会员日报表数据
    public List<ActivityStatisticItemVO> findMemberDayReport(Map map) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        DyDataSourceContextHolder.setDBSuffix(request.getHeader(HttpHead.DB_SUFFIX));
        return userActivityService.findMemberDayReport(map);
    }

    //新增福利记录
    public void insertWealReword(UserWealReword userGrowthReword) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        DyDataSourceContextHolder.setDBSuffix(request.getHeader(HttpHead.DB_SUFFIX));
        userWealRewordService.insert(userGrowthReword);
    }

    //新增系统回复用户消息
    public void addSysInformation(SysInformation sysInformation) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        DyDataSourceContextHolder.setDBSuffix(request.getHeader(HttpHead.DB_SUFFIX));
        sysInformationService.createSysInformation(sysInformation);
    }
}
