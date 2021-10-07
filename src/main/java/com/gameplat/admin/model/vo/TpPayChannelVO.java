package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TpPayChannelVO extends Model<TpPayChannelVO> {

    private String name;

    private String userLevels;

    private String remarks;

    private String chanDesc;

    private Integer sort;

    private Integer status;

    private Long rechargeTimes;

    private Long rechargeAmount;

    private String limitInfo;

    private String payChannelTip;

    private String interfaceName;

    private String payTypeName;

    private String tpMerchantName;

    private Long tpInterfaceId;

    private String tpPayTypeName;

    private String payType;

    /**
     * 通道金额设置标识，0位禁用
     */
    private Integer limitStatus;

    /**
     * 通道金额收款上限
     */
    private Long limitAmount;

    /**
     * 通道时间设置标识，0为启用时间设置，1位禁用时间设置
     */
    private Integer channelTimeStatus;

    /**
     * 通道显示开始时间
     */
    private Integer channelTimeStart;

    /**
     * 通道显示结束时间
     */
    private Integer channelTimeEnd;

    /**
     * 通道展示端，1展示在电脑，2展示在安卓，3展示在IOS
     */
    private String channelShows;

    /**
     * 通道单笔金额金额最小值
     */
    private Long minAmountPerOrder;

    /**
     * 通道单笔金额金额最小值
     */
    private Long maxAmountPerOrder;

    /**
     * 通道风控金额类型 0.任何金额 1.浮动金额 2.固定金额 3浮动固定金额
     */
    private Integer riskControlType;

    /**
     * 风控值
     */
    private String riskControlValue;
}
