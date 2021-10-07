package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * 用户成长等级配置
 *
 * @author Lenovo
 */
@Data
public class MemberGrowthLevel extends BaseEntity {

    /**
     * 等级
     */
    private Integer level;

    /**
     * 等级称号
     */
    private String levelName;

    /**
     * 所需成长值
     */
    private Integer growth;

    /**
     * 保级成长值（按照某种策略 达不到则降级）
     */
    private Integer limitGrowth;

    /**
     * 升到此级奖励
     */
    private Double upReward;

    /**
     * 周俸禄
     */
    private Long weekWage;

    /**
     * 月俸禄
     */
    private Long monthWage;

    /**
     * 生日礼金
     */
    private Long birthGiftMoney;

    /**
     * 红包
     */
    private Double redEnvelope;

    /**
     * 借呗额度
     */
    private Long creditMoney;

    /**
     * 备注
     */
    private String remark;

    /**
     * 移动端VIP图片
     */
    private String mobileVipImage;


    /**
     * WEB端VIP图片
     */
    private String webVipImage;

    /**
     * 移动端达成背景
     */
    private String mobileReachBackImage;

    /**
     * 移动端未达成背景
     */
    private String mobileUnReachBackImage;

    /**
     * 移动端达成VIP图
     */
    private String mobileReachVipImage;


    /**
     * 移动端未达成VIP图
     */
    private String mobileUnReachVipImage;


    /**
     * WEB端达成VIP图
     */
    private String webReachVipImage;


    /**
     * WEB端未达成VIP图
     */
    private String webUnReachVipImage;


}
