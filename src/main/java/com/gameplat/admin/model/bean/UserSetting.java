package com.gameplat.admin.model.bean;

import lombok.Data;

/**
 * 用户设置
 * @author three
 */
@Data
public class UserSetting {

    /**
     * 用户页
     */
    private String indexUrl;
    /**
     * 用户默认页
     */
    private Integer defaultPageSize = 10;
    /**
     * 充值订单排序
     */
    private Integer receiptOrder = 1;
    /**
     * 提现订单排序
     */
    private Integer withdrawOrder = 1;
    /**
     * 金额千分符
     */
    private Integer thousandsSeparator = 0;
    /**
     * 金额精度
     */
    private Integer fractionCount = 0;
    /**
     * 切换导航时自动打开菜单
     */
    private Integer openDefaultNavMenu = 1;
    /**
     * 用户黑名单
     */
    private String specialMemberWarn;
    /**
     * 用户主题色
     */
    private String themeColor = "#409EFF";
    /**
     * 开启 Tags-View
     */
    private Boolean tagsView = true;
    /**
     * 侧边栏 Logo
     */
    private Boolean sidebarLogo = true;
    /**
     * 展示时间 2、美东时间 1、北京时间
     */
    private Integer showTimeType = 2;
    /**
     * 侧边栏伸缩
     */
    private Boolean sidebarShow = true;
}
