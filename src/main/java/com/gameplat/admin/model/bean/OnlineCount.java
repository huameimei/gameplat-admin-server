package com.gameplat.admin.model.bean;

import lombok.Data;

/**
 * 在线设备合计
 * @author three
 */
@Data
public class OnlineCount {

    private int iphoneCount;
    private int androidCount;
    private int iphoneH5Count;
    private int androidH5Count;
    /**
     * win用户合计
     */
    private int windowsCount;
    /**
     * 会员合计
     */
    private int memberCount;
    /**
     * 试玩会员合计
     */
    private int testUserCount;
    /**
     * 推广会员合计
     */
    private int promotionCount;
    /**
     * 告警会员合计
     */
    private int specialCount;
}
