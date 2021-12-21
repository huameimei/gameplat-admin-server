package com.gameplat.admin.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.gameplat.admin.enums.GameTypeEnum;
import com.gameplat.admin.mapper.RechargeOrderMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.IdempotentKeyUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.RandomUtil;
import com.gameplat.base.common.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公用活动处理类
 *
 * @author kenvin
 */
@Slf4j
@Service
public class ActivityCommonServiceImpl implements ActivityCommonService {

    @Autowired
    private ActivityLobbyService activityLobbyService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private ActivityBlacklistService activityBlacklistService;

    @Autowired
    private ActivityQualificationService activityQualificationService;

    @Autowired
    private ActivityLobbyDiscountService activityLobbyDiscountService;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private LiveMemberDayReportService liveMemberDayReportService;

    @Autowired
    private RechargeOrderService rechargeOrderService;

    @Resource
    private LiveBetRecordService liveBetRecordService;


    /**
     * 组装审核备注信息
     *
     * @return
     */
    @Override
    public String getAuditRemark(ActivityLobby activityLobby, String statisticValue, String validAmount, String startTime, String endTime) {
        Integer targetType = activityLobby.getStatisItem();
        String gameType = activityLobby.getGameType();
        StringBuilder sb = new StringBuilder();
        Integer applyWay = activityLobby.getApplyWay();
        if (applyWay == 1) {
            sb.append("申请方式:手动,");
        }
        if (applyWay == 2) {
            sb.append("申请方式:自动,");
        }
        Integer auditWay = activityLobby.getAuditWay();
        if (auditWay == 1) {
            sb.append("审核方式:手动,");
        }
        if (auditWay == 2) {
            sb.append("审核方式:自动,");
        }
        if (StringUtils.isNotEmpty(statisticValue)) {
            //充值活动
            if (activityLobby.getType() == 1) {
                if (targetType == 1) {
                    sb.append("累计充值金额:").append(statisticValue);
                }
                if (targetType == 2) {
                    sb.append("累计充值天数:").append(statisticValue);
                }
                if (targetType == 3) {
                    sb.append("连续充值天数:").append(statisticValue);
                }
                if (targetType == 4) {
                    sb.append("单日首充金额:").append(statisticValue);
                }
                if (targetType == 5) {
                    sb.append("首充金额:").append(statisticValue);
                }
            }
            //游戏活动
            else if (activityLobby.getType() == 2) {
                if (targetType == 6) {
                    sb.append("累计").append(GameTypeEnum.getName(gameType)).append("打码金额:").append(statisticValue);
                }
                if (targetType == 7) {
                    sb.append("累计").append(GameTypeEnum.getName(gameType)).append("打码天数:").append(statisticValue);
                }
                if (targetType == 8) {
                    sb.append("连续").append(GameTypeEnum.getName(gameType)).append("打码天数:").append(statisticValue);
                }
                if (targetType == 9) {
                    sb.append("单日").append(GameTypeEnum.getName(gameType)).append("亏损金额:").append(statisticValue);
                }
                if (targetType == 10) {
                    sb.append("指定比赛打码金额:").append(statisticValue);
                }
            }
        }

        if (StringUtils.isNotEmpty(validAmount)) {
            if (targetType != 6 || targetType != 10) {
                sb.append(",累计打码量:").append(validAmount);
            }
        }
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            sb.append(",资格来源:手动添加");
        }
        return sb.toString();
    }

    @Override
    public void userDetection(MemberInfoVO memberInfo, int flagCheck) {
        if (memberInfo.getStatus() == 0) {
            //账号状态异常
            throw new ServiceException("账号状态异常");
        }
    }

    @Override
    public ActivityLobby activityDetection(Long activityId, Date countDate, int flagCheck) {
        if (flagCheck == 1) {
            //根据活动发布的ID查找绑定的活动大厅的ID
            ActivityInfo activityInfo = activityInfoService.getById(activityId);
            if (StringUtils.isNull(activityInfo)) {
                //活动发布信息异常
                throw new ServiceException("活动信息异常");
            }

            activityId = activityInfo.getActivityLobbyId();
        }

        //根据活动ID查询活动详细信息
        ActivityLobby activityLobby = activityLobbyService.getById(activityId);
        if (StringUtils.isNull(activityLobby)) {
            //活动信息异常
            throw new ServiceException("活动信息没找到");
        }

        if (activityLobby.getStatus() != 1) {
            //该活动已关闭
            throw new ServiceException("该活动已关闭");
        }

        //如果开启隔天申请，那么活动总的结束时间应该延长一天
        Date endTime = activityLobby.getEndTime();
        if (activityLobby.getNextDayApply() == 1) {
            endTime = DateUtil.date(endTime).offset(DateField.DAY_OF_YEAR, 1);
        }

        if (countDate.before(activityLobby.getStartTime())) {
            //活动未开始
            throw new ServiceException("活动未开始");
        }

        if (countDate.after(endTime)) {
            //活动已结束
            throw new ServiceException("活动已结束");
        }

        if (activityLobby.getApplyWay() == 2) {
            if (flagCheck == 1 || flagCheck == 4) {
                //该活动不需要手动参与,次日将自动统计您是否符合该活动的参与条件
                throw new ServiceException("该活动不需要手动参与,次日将自动统计您是否符合该活动的参与条件");
            }

        }

        //判断申请日期是否是规定的统计日期
        ActivityLobby memberActivityLobby = checkActivityDate(activityLobby, countDate, true);
        if (memberActivityLobby == null) {
            //未到申请时间
            throw new ServiceException("未到申请时间");
        }

        return activityLobby;
    }

    /**
     * 检测日期
     *
     * @param activityLobby
     * @param countDate
     * @param b
     * @return
     */
    private ActivityLobby checkActivityDate(ActivityLobby activityLobby, Date countDate, boolean b) {
        Integer date = activityLobby.getStatisDate();
        Integer nextDayApply = activityLobby.getNextDayApply();
        // 统计日期（1 每日，2 每周，3 每月，4 每周X，5 每月X日）
        if (date == 1) {
            if (nextDayApply == 1) {
                //如果开启了隔天申请，当统计日期为每天，活动的第一天则不在申请时间内
                if (!DateUtil.isSameDay(countDate, activityLobby.getStartTime())) {
                    return activityLobby;
                }
            } else {
                return activityLobby;
            }
        } else {
            String applyDates = activityLobby.getApplyDateList();
            if (applyDates.contains(DateUtil.format(countDate, "yyyy-MM-dd"))) {
                return activityLobby;
            }
        }
        return null;
    }

    @Override
    public void blacklistDetection(ActivityLobby activityLobby, MemberInfoVO memberInfo, int i) {
        //查询该活动黑名单
        LambdaQueryChainWrapper<ActivityBlacklist> activityBlacklistLambdaQueryChainWrapper = activityBlacklistService.lambdaQuery();
        activityBlacklistLambdaQueryChainWrapper
                .eq(ActivityBlacklist::getActivityId, activityLobby.getId());

        if (StringUtils.isNotBlank(memberInfo.getAccount()) && StringUtils.isNotBlank(memberInfo.getLastLoginIp())) {
            activityBlacklistLambdaQueryChainWrapper
                    .and(wrapper -> wrapper.eq(ActivityBlacklist::getLimitedContent, memberInfo.getAccount()
                            ).or().eq(ActivityBlacklist::getLimitedContent, memberInfo.getLastLoginIp())
                    );
        }

        Integer count = activityBlacklistLambdaQueryChainWrapper.count();
        if (count > 0) {
            //您已被禁止参加此活动，如有疑问，请联系客服
            throw new ServiceException("您已被禁止参加此活动，如有疑问，请联系客服");
        }
    }

    @Override
    public void qualificationDetection(ActivityLobby activityLobby, MemberInfoVO memberInfo, Date countDate, int i) {
        //判断该用户是否重复参加该活动
        ActivityQualification query = new ActivityQualification();
        query.setActivityId(activityLobby.getId());
        query.setActivityType(activityLobby.getType());
        query.setUserId(memberInfo.getId());
        if (activityLobby.getIsRepeat() == 1) {
            query.setSoleIdentifier(IdempotentKeyUtils.md5(DateUtil.formatDate(countDate)));
        }

        Integer count = activityQualificationService.lambdaQuery().eq(ActivityQualification::getActivityId, activityLobby.getId())
                .eq(ActivityQualification::getActivityType, activityLobby.getType())
                .eq(ActivityQualification::getUserId, memberInfo.getId())
                .eq(activityLobby.getIsRepeat() == 1
                        , ActivityQualification::getSoleIdentifier, IdempotentKeyUtils.md5(DateUtil.formatDate(countDate)))
                .count();
        if (count > 0) {
            if (activityLobby.getIsRepeat() == 1) {
                //活动的每个统计周期内,只能参与一次
                throw new ServiceException("活动的每个统计周期内,只能参与一次");
            } else if (activityLobby.getIsRepeat() == 0) {
                //该活动不可重复参加
                throw new ServiceException("该活动不可重复参加");
            }
        }

        // 判断用户充值层级是否满足活动要求
        String gradedRequire = activityLobby.getGraded();
        String rank = memberInfo.getUserLevel() != null ? String.valueOf(memberInfo.getUserLevel()) : "0";
        if (StringUtils.isNotEmpty(gradedRequire)) {
            String[] graders = gradedRequire.split(",");
            List<String> graderList = Lists.newArrayList(graders);
            if (!graderList.contains(rank)) {
                log.info("活动:{},限制此用户充值层级:{}", activityLobby.getTitle(), memberInfo.getId());
                //你的条件不符合该活动要求
                throw new ServiceException("你的条件不符合该活动要求");
            }
        }

        //判断用户VIP等级是否满足活动要求
        String userLevelRequire = activityLobby.getUserLevel();
        String userLevel = memberInfo.getLevel() != null ? String.valueOf(memberInfo.getLevel()) : "0";
        if (StringUtils.isNotEmpty(userLevelRequire)) {
            String[] userLevels = userLevelRequire.split(",");
            List<String> userLevelList = Lists.newArrayList(userLevels);
            if (!userLevelList.contains(userLevel)) {
                log.info("活动:{},限制此用户VIP等级:{}", activityLobby.getTitle(), memberInfo.getId());
                //你的VIP等级不符合该活动要求
                throw new ServiceException("你的VIP等级不符合该活动要求");
            }
        }
    }

    @Override
    public List<ActivityQualification> activityRuleDetection(ActivityLobby activityLobby, Date countDate, MemberInfoVO memberInfo, int flagCheck) {
        // 根据活动id查询对应的优惠列表
        List<ActivityLobbyDiscount> lobbyDiscounts =
                activityLobbyDiscountService.lambdaQuery()
                        .eq(ActivityLobbyDiscount::getLobbyId, activityLobby.getId()).list();
        if (StringUtils.isEmpty(lobbyDiscounts)) {
            //活动优惠列表信息异常
            throw new ServiceException("活动优惠列表信息异常");
        }

        //获取统计的时间(时间段)
        Map map = getStatisticalDate(activityLobby, countDate);
        String statisticalStartTime = (String) map.get("startTime");
        String statisticalEndTime = (String) map.get("endTime");

        Integer statisItem = activityLobby.getStatisItem();
        List<ActivityQualification> manageList = null;
        switch (statisItem) {
            case 1:
                // 累计充值金额
                manageList = dealCumulativeRecharge(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 2:
                // 累计充值天数
                manageList = dealCumulativeRechargeDays(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 3:
                // 连续充值天数
                manageList = dealContinuousDays(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 4:
                // 单日首充金额
                manageList = dealFirstPayDay(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 5:
                // 首充金额
                manageList = dealFirstPay(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 6:
                // 累计游戏打码金额
                manageList = dealCumulativeGameDml(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 7:
                // 累计游戏打码天数
                manageList = dealCumulativeGameDmDays(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 8:
                // 连续游戏打码天数
                manageList = dealContinuousGameDmDays(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 9:
                // 单日游戏亏损金额
                manageList = dealGameWinAmount(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
            case 10:
                // 指定比赛打码量
                manageList = dealAssignMatch(activityLobby, lobbyDiscounts, memberInfo, statisticalStartTime, statisticalEndTime, flagCheck, countDate);
                break;
        }

        return manageList;
    }

    /**
     * 指定比赛
     */
    public List<ActivityQualification> dealAssignMatch(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                       MemberInfoVO memberInfo, String startTime,
                                                       String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        Map map = new HashMap();
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("gameCode", "xj");
        map.put("bonusDate", startTime.substring(0, 10));
        map.put("matchId", memberActivityLobby.getMatchId());
        List<ActivityStatisticItem> userAssignMatchDmlList = liveBetRecordService.xjAssignMatchDml(map);
        if (StringUtils.isEmpty(userAssignMatchDmlList)) {
            //您没有该场比赛的投注记录
            throw new ServiceException("E035");
        }

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem userAssignMatchDml : userAssignMatchDmlList) {
            //组装会员信息
            if (userAssignMatchDml.getUserName().equals(memberInfo.getAccount())) {
                userAssignMatchDml.setUserId(memberInfo.getId());
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeValidAmount(userAssignMatchDml.getCumulativeGameDml(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, userAssignMatchDml, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "该场比赛的打码量未达到活动最低标准,目前打码量:" + userAssignMatchDml.getValidAmount();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在该场比赛的打码量未达到活动最低标准
                        errorMsg = "您在该场比赛的打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }

        return qualificationManageList;
    }

    /**
     * 单日游戏亏损金额
     *
     * @param memberActivityLobby
     * @param discounts
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealGameWinAmount(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                         MemberInfoVO memberInfo, String startTime,
                                                         String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        Map map = new HashMap<>();
        if (memberInfo == null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("sportGameList", memberActivityLobby.getGameList());
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);
        if (StringUtils.isEmpty(memberGameReportInfoList)) {
            throw new ServiceException("您在活动统计期间,对于指定的游戏,没有有效的投注金额");
        }

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem memberGameReportInfo : memberGameReportInfoList) {
            //组装会员信息
            if (memberGameReportInfo.getUserName().equals(memberInfo.getAccount())) {
                memberGameReportInfo.setUserId(memberInfo.getId());
            }

            //先判断输赢金额是否大于等于0
            if (NumberUtil.isGreaterOrEqual(memberGameReportInfo.getGameWinAmount(), BigDecimal.ZERO)) {
                if (memberActivityLobby.getApplyWay() == 1) {
                    //您在活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准
                    throw new ServiceException("您在活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准");
                } else if (memberActivityLobby.getApplyWay() == 2) {
                    continue;
                }
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeMoney(memberGameReportInfo.getGameWinAmount().negate(), memberGameReportInfo.getCumulativeGameDml(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, memberGameReportInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准,目前亏损金额:" + memberGameReportInfo.getGameWinAmount() + ",打码量:" + memberGameReportInfo.getCumulativeGameDml();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 连续游戏打码天数
     *
     * @param memberActivityLobby
     * @param discounts
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealContinuousGameDmDays(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                                MemberInfoVO memberInfo, String startTime,
                                                                String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        Map map = new HashMap<>();
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("sportGameList", memberActivityLobby.getGameList());
        map.put("sportValidAmount", memberActivityLobby.getSportValidAmount());
        map.put("statisItem", memberActivityLobby.getStatisItem());
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);
        if (StringUtils.isEmpty(memberGameReportInfoList)) {
            throw new ServiceException("您在活动统计期间,对于指定的游戏,没有有效的投注金额");
        }

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem memberGameReportInfo : memberGameReportInfoList) {
            //组装会员信息
            if (memberGameReportInfo.getUserName().equals(memberInfo.getAccount())) {
                memberGameReportInfo.setUserId(memberInfo.getId());
            }
            //最大连续游戏打码天数
            int maxCumulativeGameDmDays = getMaxPayDay(memberGameReportInfo.getGameCountDateList());

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeDays(maxCumulativeGameDmDays, memberGameReportInfo.getCumulativeGameDml(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, memberGameReportInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,对于指定的游戏,最大连续打码天数和总打码量未达到活动最低标准,目前最大连续打码天数:" + maxCumulativeGameDmDays + ",打码量:" + memberGameReportInfo.getCumulativeGameDml();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间,对于指定的游戏,最大连续打码天数和总打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间,对于指定的游戏,最大连续打码天数和总打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }


    /**
     * 累计游戏打码天数
     *
     * @param memberActivityLobby 活动
     * @param discounts           优惠
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealCumulativeGameDmDays(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                                MemberInfoVO memberInfo, String startTime,
                                                                String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        Map map = new HashMap<>();
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("sportGameList", memberActivityLobby.getGameList());
        map.put("sportValidAmount", memberActivityLobby.getSportValidAmount());
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);
        if (StringUtils.isEmpty(memberGameReportInfoList)) {
            throw new ServiceException("您在活动统计期间,对于指定的游戏,累计打码金额未达到活动最低标准");
        }

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem memberGameReportInfo : memberGameReportInfoList) {
            //组装会员信息
            if (memberGameReportInfo.getUserName().equals(memberInfo.getAccount())) {
                memberGameReportInfo.setUserId(memberInfo.getId());
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeDays(memberGameReportInfo.getCumulativeGameDmDays(), memberGameReportInfo.getCumulativeGameDml(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, memberGameReportInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,对于指定的游戏,累计打码天数和总打码量未达到活动最低标准,目前累计打码天数:" + memberGameReportInfo.getCumulativeGameDmDays() + ",打码量:" + memberGameReportInfo.getCumulativeGameDml();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间,对于指定的游戏,累计打码天数和总打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间,对于指定的游戏,累计打码天数和总打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 累计游戏打码金额
     *
     * @param memberActivityLobby 活动
     * @param discounts           优惠
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealCumulativeGameDml(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                             MemberInfoVO memberInfo, String startTime,
                                                             String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        Map map = new HashMap<>();
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("sportGameList", memberActivityLobby.getGameList());
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);
        if (StringUtils.isEmpty(memberGameReportInfoList)) {
            //您在活动统计期间,对于指定的游戏,没有有效的投注金额
            throw new ServiceException("您在活动统计期间,对于指定的游戏,没有有效的投注金额");
        }
        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem memberGameReportInfo : memberGameReportInfoList) {
            //组装会员信息
            if (memberGameReportInfo.getUserName().equals(memberInfo.getAccount())) {
                memberGameReportInfo.setUserId(memberInfo.getId());
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeValidAmount(memberGameReportInfo.getCumulativeGameDml(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, memberGameReportInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,对于指定的游戏,累计打码金额未达到活动最低标准,目前累计打码金额:" + memberGameReportInfo.getCumulativeGameDml();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间,对于指定的游戏,累计打码金额未达到活动最低标准
                        errorMsg = "您在活动统计期间,对于指定的游戏,累计打码金额未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }

        return qualificationManageList;
    }

    /**
     * 检查有效金额
     *
     * @param validAmount
     * @param discounts
     * @param satisfyDiscounts
     * @param memberActivityLobby
     * @param statisticUserInfo
     * @param startTime
     * @param endTime
     * @param countDate
     * @return
     */
    public ActivityQualification judgeValidAmount(BigDecimal validAmount, List<ActivityLobbyDiscount> discounts,
                                                  List<ActivityLobbyDiscount> satisfyDiscounts, ActivityLobby memberActivityLobby,
                                                  ActivityStatisticItem statisticUserInfo, String startTime, String endTime, Date countDate) {
        //通过目标值和赠送打码量筛选，因为活动优惠列表集合是倒序，所以找到的第一个为目标满足的最大条件
        ActivityLobbyDiscount lobbyDiscount = discounts.stream().filter(discount ->
                NumberUtil.isGreaterOrEqual(validAmount, Convert.toBigDecimal(discount.getTargetValue())))
                .findFirst().orElse(null);
        if (StringUtils.isNotNull(lobbyDiscount)) {
            String auditRemark = getAuditRemark(memberActivityLobby, validAmount.toString(), null, startTime, endTime);
            satisfyDiscounts.add(lobbyDiscount);
            //如果活动选择了多重彩金，则递归调用此方法
            if (memberActivityLobby.getMultipleHandsel() == 1) {
                discounts.remove(lobbyDiscount);
                //如果活动优惠列表的长度为0，则停止递归
                if (discounts.size() > 0) {
                    judgeValidAmount(validAmount, discounts, satisfyDiscounts, memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
                }
            }
            ActivityQualification manage = assembleQualificationManage(memberActivityLobby, statisticUserInfo, satisfyDiscounts, auditRemark, countDate, startTime, endTime);
            return manage;
        }
        return null;
    }

    /**
     * 首充金额
     *
     * @param memberActivityLobby 活动
     * @param discounts           优惠
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealFirstPay(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                    MemberInfoVO memberInfo, String startTime,
                                                    String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        //查询统计周期内的会员游戏日报表汇总数据(打码量)
        Map map = new HashMap<>();
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("payType", memberActivityLobby.getPayType());
        map.put("startTime", DateUtil.format(memberActivityLobby.getStartTime(), DatePattern.NORM_DATE_PATTERN));
        map.put("endTime", DateUtil.format(memberActivityLobby.getEndTime(), DatePattern.NORM_DATE_PATTERN));
        List<ActivityStatisticItem> statisticUserInfoList = rechargeOrderService.findAllFirstRechargeAmount(map);
        if (StringUtils.isEmpty(statisticUserInfoList)) {
            throw new ServiceException("您在活动统计期间没有有效的充值金额");
        }

        map.put("userNameList", statisticUserInfoList.stream().map(ActivityStatisticItem::getUserName).collect(Collectors.toList()));
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem statisticUserInfo : statisticUserInfoList) {
            if (statisticUserInfo.getIsNewUser() == 2) {
                if (flagCheck == 3) {
                    //该活动只针对新用户有效
                    throw new ServiceException("该活动只针对新用户有效");
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您账号的第一笔首充订单并不在活动时间内
                        throw new ServiceException("您账号的第一笔首充订单并不在活动时间内");
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
            }
            //先将所有会员打码量都赋值为0
            statisticUserInfo.setValidAmount(BigDecimal.ZERO);
            //如果部分会员有打码量金额,则赋值实际的打码量金额
            if (StringUtils.isNotEmpty(memberGameReportInfoList)) {
                //组装会员信息
                for (ActivityStatisticItem memberDayReportVO : memberGameReportInfoList) {
                    if (statisticUserInfo.getUserName().equals(memberDayReportVO.getUserName())) {
                        statisticUserInfo.setValidAmount(memberDayReportVO.getCumulativeGameDml());
                    }
                }
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeMoney(statisticUserInfo.getFirstRechargeAmount(), statisticUserInfo.getValidAmount(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,首充金额和打码量未达到活动最低标准,目前首充金额:" + statisticUserInfo.getFirstRechargeAmount() + ",打码量:" + statisticUserInfo.getValidAmount();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间的首充金额和打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间的首充金额和打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 单日首充金额
     *
     * @param memberActivityLobby
     * @param discounts
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealFirstPayDay(ActivityLobby memberActivityLobby, List<ActivityLobbyDiscount> discounts,
                                                       MemberInfoVO memberInfo, String startTime,
                                                       String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        //查询统计周期内的会员游戏日报表汇总数据(打码量)
        Map map = new HashMap<>();
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("payType", memberActivityLobby.getPayType());
        List<ActivityStatisticItem> statisticUserInfoList = rechargeOrderMapper.findFirstRechargeAmount(map);
        if (StringUtils.isEmpty(statisticUserInfoList)) {
            throw new ServiceException("您在活动统计期间没有有效的充值金额");
        }

        map.put("userNameList", statisticUserInfoList.stream().map(ActivityStatisticItem::getUserName).collect(Collectors.toList()));
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem statisticUserInfo : statisticUserInfoList) {
            //先将所有会员打码量都赋值为0
            statisticUserInfo.setValidAmount(BigDecimal.ZERO);
            //如果部分会员有打码量金额,则赋值实际的打码量金额
            if (StringUtils.isNotEmpty(memberGameReportInfoList)) {
                //组装会员信息
                for (ActivityStatisticItem memberDayReportVO : memberGameReportInfoList) {
                    if (statisticUserInfo.getUserName().equals(memberDayReportVO.getUserName())) {
                        statisticUserInfo.setValidAmount(memberDayReportVO.getCumulativeGameDml());
                    }
                }
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeMoney(statisticUserInfo.getFirstRechargeAmount(), statisticUserInfo.getValidAmount(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,单日首充金额和打码量未达到活动最低标准,目前单日首充金额:" + statisticUserInfo.getFirstRechargeAmount() + ",打码量:" + statisticUserInfo.getValidAmount();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间的单日首充金额和打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间的单日首充金额和打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 连续充值天数
     *
     * @param memberActivityLobby
     * @param discounts
     * @param memberInfo
     * @param startTime
     * @param endTime
     * @param flagCheck
     * @param countDate
     * @return
     */
    public List<ActivityQualification> dealContinuousDays(ActivityLobby memberActivityLobby,
                                                          List<ActivityLobbyDiscount> discounts, MemberInfoVO memberInfo,
                                                          String startTime, String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        //查询统计周期内的会员游戏日报表汇总数据(打码量)
        Map map = new HashMap<>(1);
        if (memberInfo != null) {
            map.put("userNameList", Lists.newArrayList(memberInfo.getAccount()));
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("payType", memberActivityLobby.getPayType());
        map.put("rechargeValidAmount", memberActivityLobby.getRechargeValidAmount());
        List<ActivityStatisticItem> statisticUserInfoList = rechargeOrderService.findRechargeDateList(map);
        if (StringUtils.isEmpty(statisticUserInfoList)) {
            throw new ServiceException("您在活动统计期间没有有效的充值金额");
        }

        map.put("userNameList", statisticUserInfoList.stream().map(ActivityStatisticItem::getUserName).collect(Collectors.toList()));
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem statisticUserInfo : statisticUserInfoList) {
            //先将所有会员打码量都赋值为0
            statisticUserInfo.setValidAmount(BigDecimal.ZERO);
            //如果部分会员有打码量金额,则赋值实际的打码量金额
            if (StringUtils.isNotEmpty(memberGameReportInfoList)) {
                //组装会员信息
                for (ActivityStatisticItem memberDayReportVO : memberGameReportInfoList) {
                    if (statisticUserInfo.getUserName().equals(memberDayReportVO.getUserName())) {
                        statisticUserInfo.setValidAmount(memberDayReportVO.getCumulativeGameDml());
                    }
                }
            }
            //最大连续充值天数
            int maxRechargeDay = getMaxPayDay(statisticUserInfo.getRechargeTimeList());

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeDays(maxRechargeDay, statisticUserInfo.getValidAmount(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,最大连续充值天数和打码量未达到活动最低标准,目前最大连续充值天数:" + maxRechargeDay + ",打码量:" + statisticUserInfo.getValidAmount();
                } else {
                    //您在活动统计期间的最大连续充值天数和打码量未达到活动最低标准
                    if (memberActivityLobby.getApplyWay() == 1) {
                        errorMsg = "您在活动统计期间的最大连续充值天数和打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 获取最大连续时间天数
     *
     * @param vos
     * @return
     */
    public int getMaxPayDay(List<Date> vos) {
        int count = 1;
        int resultCount = 0;
        for (int i = 0; i < vos.size(); i++) {
            LocalDate start = vos.get(i).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (i >= vos.size() - 1) {
                break;
            }
            LocalDate end = vos.get(i + 1).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (end.minusDays(1).equals(start)) {
                count++;
            } else {
                resultCount = Math.max(resultCount, count);
                count = 1;
            }
        }
        return Math.max(resultCount, count);
    }

    /**
     * 累计充值天数
     *
     * @param memberActivityLobby 活动
     * @param discounts           活动 所有优惠
     */
    public List<ActivityQualification> dealCumulativeRechargeDays(ActivityLobby memberActivityLobby,
                                                                  List<ActivityLobbyDiscount> discounts,
                                                                  MemberInfoVO memberInfo,
                                                                  String startTime, String endTime, Integer flagCheck, Date countDate) {
        String errorMsg = null;
        //查询统计周期内的会员游戏日报表汇总数据(打码量)
        Map map = new HashMap<>(1);
        if (memberInfo != null) {
            List<String> accountList = new ArrayList<>();
            accountList.add(memberInfo.getAccount());
            map.put("userNameList", accountList);
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        map.put("payType", memberActivityLobby.getPayType());
        map.put("rechargeValidAmount", memberActivityLobby.getRechargeValidAmount());
        List<ActivityStatisticItem> statisticUserInfoList = rechargeOrderMapper.findRechargeInfo(map);
        if (StringUtils.isEmpty(statisticUserInfoList)) {
            throw new ServiceException("您在活动统计期间没有有效的充值金额");
        }

        map.put("userNameList", statisticUserInfoList.stream().map(ActivityStatisticItem::getUserName).collect(Collectors.toList()));
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem statisticUserInfo : statisticUserInfoList) {
            //先将所有会员打码量都赋值为0
            statisticUserInfo.setValidAmount(BigDecimal.ZERO);
            //如果部分会员有打码量金额,则赋值实际的打码量金额
            if (StringUtils.isNotEmpty(memberGameReportInfoList)) {
                //组装会员信息
                for (ActivityStatisticItem memberDayReportVO : memberGameReportInfoList) {
                    if (statisticUserInfo.getUserName().equals(memberDayReportVO.getUserName())) {
                        statisticUserInfo.setValidAmount(memberDayReportVO.getCumulativeGameDml());
                    }
                }
            }

            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeDays(statisticUserInfo.getAccumulativeRechargeNum(), statisticUserInfo.getValidAmount(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,累计充值天数和打码量未达到活动最低标准,目前累计充值天数:" + statisticUserInfo.getAccumulativeRechargeNum() + ",打码量:" + statisticUserInfo.getValidAmount();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间的累计充值天数和打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间的累计充值天数和打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 判断天数
     *
     * @param days
     * @param validAmount
     * @param discounts
     * @param satisfyDiscounts
     * @param memberActivityLobby
     * @param statisticUserInfo
     * @param startTime
     * @param endTime
     * @param countDate
     * @return
     */
    public ActivityQualification judgeDays(Integer days, BigDecimal validAmount, List<ActivityLobbyDiscount> discounts,
                                           List<ActivityLobbyDiscount> satisfyDiscounts,
                                           ActivityLobby memberActivityLobby, ActivityStatisticItem statisticUserInfo,
                                           String startTime, String endTime, Date countDate) {
        //通过目标值和赠送打码量筛选，因为活动优惠列表集合是倒序，所以找到的第一个为目标满足的最大条件
        ActivityLobbyDiscount lobbyDiscount = discounts.stream().filter(discount ->
                days >= discount.getTargetValue() &&
                        NumberUtil.isGreaterOrEqual(validAmount, discount.getPresenterDml()))
                .findFirst().orElse(null);
        if (StringUtils.isNotNull(lobbyDiscount)) {
            String auditRemark = getAuditRemark(memberActivityLobby, days.toString(), validAmount.toString(), startTime, endTime);
            satisfyDiscounts.add(lobbyDiscount);
            //如果活动选择了多重彩金，则递归调用此方法
            if (memberActivityLobby.getMultipleHandsel() == 1) {
                discounts.remove(lobbyDiscount);
                //如果活动优惠列表的长度为0，则停止递归
                if (discounts.size() > 0) {
                    judgeDays(days, validAmount, discounts, satisfyDiscounts, memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
                }
            }
            ActivityQualification manage = assembleQualificationManage(memberActivityLobby, statisticUserInfo, satisfyDiscounts, auditRemark, countDate, startTime, endTime);
            return manage;
        }
        return null;
    }

    /**
     * 累计充值金额
     *
     * @param memberActivityLobby 活动
     * @param discounts           活动 所有优惠
     */
    public List<ActivityQualification> dealCumulativeRecharge(ActivityLobby memberActivityLobby,
                                                              List<ActivityLobbyDiscount> discounts,
                                                              MemberInfoVO memberInfo,
                                                              String startTime, String endTime,
                                                              Integer flagCheck, Date countDate) {
        //查询统计周期内的会员游戏日报表汇总数据(打码量)
        Map map = new HashMap<>();
        if (memberInfo != null) {
            List<String> accountList = new ArrayList<>();
            accountList.add(memberInfo.getAccount());
            map.put("userNameList", accountList);
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("payType", memberActivityLobby.getPayType());
        List<ActivityStatisticItem> statisticUserInfoList = rechargeOrderMapper.findRechargeInfo(map);
        if (StringUtils.isEmpty(statisticUserInfoList)) {
            //您在活动统计期间没有有效的充值金额
            throw new ServiceException("您在活动统计期间没有有效的充值金额");
        }

        map.put("userNameList", statisticUserInfoList.stream().map(ActivityStatisticItem::getUserName).collect(Collectors.toList()));
        List<ActivityStatisticItem> memberGameReportInfoList = liveMemberDayReportService.getGameReportInfo(map);

        //根据目标值对活动优惠列表倒叙排列
        discounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue).reversed());

        List<ActivityQualification> qualificationManageList = new ArrayList<>();
        for (ActivityStatisticItem statisticUserInfo : statisticUserInfoList) {
            //先将所有会员打码量都赋值为0
            statisticUserInfo.setValidAmount(BigDecimal.ZERO);
            //如果部分会员有打码量金额,则赋值实际的打码量金额
            if (StringUtils.isNotEmpty(memberGameReportInfoList)) {
                //组装会员信息
                for (ActivityStatisticItem memberDayReportVO : memberGameReportInfoList) {
                    if (statisticUserInfo.getUserName().equals(memberDayReportVO.getUserName())) {
                        statisticUserInfo.setValidAmount(memberDayReportVO.getCumulativeGameDml());
                    }
                }
            }

            String errorMsg = null;
            //深拷贝一份活动优惠列表,用于活动自动申请计算多重彩金的最终金额
            List<ActivityLobbyDiscount> copyDiscounts = BeanUtils.mapList(discounts, ActivityLobbyDiscount.class);
            ActivityQualification qualificationManage = judgeMoney(statisticUserInfo.getAccumulativeRechargeAmount(), statisticUserInfo.getValidAmount(), copyDiscounts, new ArrayList<>(),
                    memberActivityLobby, statisticUserInfo, startTime, endTime, countDate);
            if (StringUtils.isNull(qualificationManage)) {
                if (flagCheck == 3) {
                    errorMsg = "活动统计期间,累计充值金额和打码量未达到活动最低标准,目前累计充值金额:" + statisticUserInfo.getAccumulativeRechargeAmount() + ",打码量:" + statisticUserInfo.getValidAmount();
                } else {
                    if (memberActivityLobby.getApplyWay() == 1) {
                        //您在活动统计期间的累计充值金额和打码量未达到活动最低标准
                        errorMsg = "您在活动统计期间的累计充值金额和打码量未达到活动最低标准";
                    } else if (memberActivityLobby.getApplyWay() == 2) {
                        continue;
                    }
                }
                throw new ServiceException(errorMsg);
            }
            qualificationManageList.add(qualificationManage);
        }
        return qualificationManageList;
    }

    /**
     * 获取统计的时间(时间段)
     *
     * @param memberActivityLobby
     * @param countDate
     * @return
     */
    public Map getStatisticalDate(ActivityLobby memberActivityLobby, Date countDate) {
        String startTime = "";
        String endTime = "";
        Integer statisDate = memberActivityLobby.getStatisDate();

        if (statisDate == 1 || statisDate == 4 || statisDate == 5) {
            // 统计日期类型如果是每日 每周X 每月X
            // 当开启了隔天审核，统计的结束日期是申请日期的前一天
            if (memberActivityLobby.getNextDayApply() == 1) {
                startTime = DateUtil.format(DateUtil.date(countDate).offset(DateField.DAY_OF_YEAR, -1), DatePattern.NORM_DATE_PATTERN);
                endTime = DateUtil.format(DateUtil.date(countDate).offset(DateField.DAY_OF_YEAR, -1), DatePattern.NORM_DATE_PATTERN);
            } else {
                // 没有开启隔天审核，统计的结束日期是申请日期当天
                startTime = DateUtil.format(countDate, DatePattern.NORM_DATE_PATTERN);
                endTime = DateUtil.format(countDate, DatePattern.NORM_DATETIME_PATTERN);
            }
        } else if (statisDate == 2) {
            // 统计日期类型如果是每周
            // 当开启了隔天审核，统计的结束日期是申请日期的前一天，开始日期是申请日期往前的7天
            if (memberActivityLobby.getNextDayApply() == 1) {
                startTime = DateUtil.format(DateUtil.date(countDate).offset(DateField.DAY_OF_YEAR, -7), DatePattern.NORM_DATE_PATTERN);
                endTime = DateUtil.format(DateUtil.date(countDate).offset(DateField.DAY_OF_YEAR, -1), DatePattern.NORM_DATE_PATTERN);
            } else {
                // 没有开启隔天审核，统计的结束日期是申请日期当天，开始日期是申请日期往前的6天
                startTime = DateUtil.format(DateUtil.date(countDate).offset(DateField.DAY_OF_YEAR, -6), DatePattern.NORM_DATE_PATTERN);
                endTime = DateUtil.format(countDate, DatePattern.NORM_DATETIME_PATTERN);
            }
        } else if (statisDate == 3) {
            // 统计日期类型如果是每月
            // 当开启了隔天审核，统计的结束日期是申请日期的前一天(上个月最后一天)，开始日期是上个月的1号
            if (memberActivityLobby.getNextDayApply() == 1) {
                startTime = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
                endTime = DateUtil.format(DateUtil.date(countDate).offset(DateField.DAY_OF_YEAR, -1), DatePattern.NORM_DATE_PATTERN);
            } else {
                // 没有开启隔天审核，统计的结束日期是申请日期当天(当月的最后一天)，开始日期是当月的1号
                startTime = DateUtil.format(DateUtil.beginOfMonth(countDate), DatePattern.NORM_DATE_PATTERN);
                endTime = DateUtil.format(countDate, DatePattern.NORM_DATETIME_PATTERN);
            }
        }

        //如果最终计算出的统计开始日期大于活动的开始时间，那么实际统计的开始时间应该是活动开始时间
        if (DateUtil.parseDate(startTime).before(memberActivityLobby.getStartTime())) {
            startTime = DateUtil.format(memberActivityLobby.getStartTime(), DatePattern.NORM_DATE_PATTERN);
        }

//        Map map = new HashMap();
        //如果统计的开始时间和活动开始时间是同一天，那么统计的开始时间应该拼接上活动开始时间的时分秒
//        if (DateUtil.isSameDay(DateUtil.parseDate(startTime), memberActivityLobby.getStartTime())) {
//            startTime = startTime + activityStartTime.substring(10);
//            map.put("startTime", startTime + " " + activityStartTime.substring(10));
//        } else {
//            map.put("startTime", startTime + " 00:00:00");
//        }
//        //如果统计的结束时间和活动结束时间是同一天，那么统计的结束时间应该拼接上活动结束时间的时分秒
//        if (DateUtil.isSameDay(DateUtil.parseDate(endTime), memberActivityLobby.getEndTime())) {
//            map.put("endTime", endTime + " " + activityEndTime.substring(10));
//        } else {
//            map.put("endTime", endTime + " 23:59:59");
//        }

        Map map = new HashMap();
        map.put("startTime", startTime + " 00:00:00");
        if (memberActivityLobby.getNextDayApply() == 1) {
            map.put("endTime", endTime + " 23:59:59");
        } else {
            map.put("endTime", endTime);
        }

        return map;
    }

    public ActivityQualification judgeMoney(BigDecimal money, BigDecimal validAmount,
                                            List<ActivityLobbyDiscount> discounts, List<ActivityLobbyDiscount> satisfyDiscounts,
                                            ActivityLobby activityLobby, ActivityStatisticItem statisticUserInfo,
                                            String startTime, String endTime, Date countDate) {
        //通过目标值和赠送打码量筛选，因为活动优惠列表集合是倒序，所以找到的第一个为目标满足的最大条件
        ActivityLobbyDiscount lobbyDiscount = discounts.stream().filter(discount ->
                NumberUtil.isGreaterOrEqual(money, Convert.toBigDecimal(discount.getTargetValue())) &&
                        NumberUtil.isGreaterOrEqual(validAmount, discount.getPresenterDml()))
                .findFirst().orElse(null);
        if (StringUtils.isNotNull(lobbyDiscount)) {
            String auditRemark = getAuditRemark(activityLobby, money.toString(), validAmount.toString(), startTime, endTime);
            satisfyDiscounts.add(lobbyDiscount);
            //如果活动选择了多重彩金，则递归调用此方法
            if (activityLobby.getMultipleHandsel() == 1) {
                discounts.remove(lobbyDiscount);
                //如果活动优惠列表的长度为0，则停止递归
                if (discounts.size() > 0) {
                    judgeMoney(money, validAmount, discounts, satisfyDiscounts, activityLobby, statisticUserInfo, startTime, endTime, countDate);
                }
            }
            ActivityQualification manage = assembleQualificationManage(activityLobby, statisticUserInfo, satisfyDiscounts, auditRemark, countDate, startTime, endTime);
            return manage;
        }
        return null;
    }

    /**
     * 组装优惠数据
     *
     * @param activityLobby
     * @param userInfo
     * @param satisfyDiscounts
     * @param auditRemark
     * @param countDate
     * @param startTime
     * @param endTime
     * @return
     */
    private ActivityQualification assembleQualificationManage(ActivityLobby activityLobby,
                                                              ActivityStatisticItem userInfo, List<ActivityLobbyDiscount> satisfyDiscounts,
                                                              String auditRemark, Date countDate, String startTime, String endTime) {
        ActivityQualification manage = new ActivityQualification();
        manage.setActivityId(activityLobby.getId());
        manage.setActivityName(activityLobby.getTitle());
        manage.setActivityType(activityLobby.getType());
        if (StringUtils.isNotNull(userInfo.getUserId())) {
            manage.setUserId(userInfo.getUserId());
        }
        manage.setUsername(userInfo.getUserName());
        manage.setAuditRemark(auditRemark);
        manage.setStatus(1);
        manage.setActivityStartTime(activityLobby.getStartTime());
        manage.setActivityEndTime(activityLobby.getEndTime());
        manage.setDeleteFlag(1);
        manage.setApplyTime(countDate);
        manage.setCreateTime(countDate);
        if (activityLobby.getApplyWay() == 1) {
            manage.setCreateBy(userInfo.getUserName());
        } else if (activityLobby.getApplyWay() == 2) {
            manage.setCreateBy("system");
        }
        manage.setDrawNum(1);
        manage.setEmployNum(0);
        manage.setQualificationStatus(1);
        manage.setStatisItem(activityLobby.getStatisItem());
        manage.setMaxMoney(satisfyDiscounts.stream().mapToInt(ActivityLobbyDiscount::getPresenterValue).sum());
        manage.setWithdrawDml(satisfyDiscounts.stream().mapToInt(ActivityLobbyDiscount::getWithdrawDml).sum());
        manage.setQualificationActivityId(RandomUtil.generateOrderCode());
        manage.setSoleIdentifier(IdempotentKeyUtils.md5(DateUtil.formatDate(countDate)));
        manage.setStatisStartTime(DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss"));
        manage.setStatisEndTime(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss"));
        //根据目标值对活动优惠列表倒叙排列
        satisfyDiscounts.sort(Comparator.comparingInt(ActivityLobbyDiscount::getTargetValue));
        manage.setAwardDetail(JSON.parseArray(JSONObject.toJSONString(satisfyDiscounts)).toJSONString());
        manage.setGetWay(activityLobby.getGetWay());
        return manage;
    }

}
