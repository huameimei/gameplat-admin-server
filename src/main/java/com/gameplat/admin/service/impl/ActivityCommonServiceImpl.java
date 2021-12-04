package com.gameplat.admin.service.impl;

import com.gameplat.admin.enums.GameTypeEnum;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.service.ActivityCommonService;
import com.gameplat.base.common.util.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 公用活动处理类
 *
 * @author kenvin
 */
@Service
public class ActivityCommonServiceImpl implements ActivityCommonService {

    /**
     * 组装审核备注信息
     *
     * @return
     */
    @Override
    public String getAuditRemark(ActivityLobbyDTO activityLobby, String statisticValue, String validAmount, String startTime, String endTime) {
        Integer targetType = activityLobby.getStatisItem();
        Integer gameType = activityLobby.getGameType();
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





}
