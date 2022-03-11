package com.gameplat.admin.constant;

public interface Constants {

    /**
     * 账户资金锁 0 用户名
     */
    String MEMBER_FINANCIAL_LOCK = "lock:member_financial:{0}";

    /**
     * 租户设置类型常量类
     */
    String TEMPLATE_CONFIG_THEME = "template_config_theme";
    String TEMPLATE_CONFIG_TYPE = "template_config_type";
    String SETTING_LANGUAGE = "tenant_language";
    String SETTING_APP_NAVIGATION = "app_navigation";
    String SETTING_H5_NAVIGATION = "h5_navigation";
    String SETTING_SQUARE_NAVIGATION = "square_navigation";
    String SYSTEM_SETTING = "system_setting";
    String SPORT_CONFIG_TYPE = "sport_config";
    String SPORT_CONFIG_CODE = "sportConfig";
    String START_UP_IMAGE = "start_up_image";//启动图
    String PERSONAL_CENTER = "personal_center";//个人中心


    /**
     * 数据字典Type
     */
    String TENANT_CUSTOM_CONFIG = "TENANT_CUSTOM_CONFIG";

    /**
     * 原生体育游戏Code
     */
    String SPORT_IM = "imapi_sport";
    String SPORT_SB = "sb_sport2";
    String SPORT_BTI = "bti_sport";


    String OSS_IMG_DOMAIN = "oss_img_domain";//OSS域名
    String DFS_IMG_DOMAIN = "dfs_img_domain";//DFS域名

    String NO_GAME = "noGame";

    /**
     * 游戏状态(0:维护中/1:正常)
     */
    Integer GAME_NORMAL = 1;
    Integer GAME_MAINTAIN = 0;

    /**
     * 游戏是否支持试玩
     */
    Integer TRIAL_YES = 2;
    Integer TRIAL_NO = 0;//不支持试玩

    String ON = "on";
    String OFF = "off";

    int YES = 1;
    int NO = 0;
}
