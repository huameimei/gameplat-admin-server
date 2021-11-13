package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 用户个性DTO
 * @author three
 */
@Data
public class UserSettingDTO {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户页
     */
    private String indexUrl;
    /**
     * 用户默认页
     */
    private Integer defaultPageSize;
    /**
     * 充值订单排序
     */
    private Integer receiptOrder;
    /**
     * 提现订单排序
     */
    private Integer withdrawOrder;
    /**
     * 金额千分符
     */
    private Integer thousandsSeparator;
    /**
     * 金额精度
     */
    private Integer fractionCount;
    /**
     * 切换导航时自动打开菜单
     */
    private Integer openDefaultNavMenu;
    /**
     * 用户黑名单
     */
    private String specialMemberWarn;
    /**
     * 用户主题色
     */
    private String themeColor;
    /**
     * 开启 Tags-View
     */
    private Boolean tagsView;
    /**
     * 侧边栏 Logo
     */
    private Boolean sidebarLogo;
    /**
     * 展示时间 2、美东时间 1、北京时间
     */
    private Integer showTimeType;
    /**
     * 侧边栏伸缩
     */
    private Boolean sidebarShow;
}
