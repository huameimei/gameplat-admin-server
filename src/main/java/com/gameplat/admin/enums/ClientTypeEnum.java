package com.gameplat.admin.enums;

/**
 * 客户端类型
 */
public enum ClientTypeEnum {
    USER_CLIENT("客户端"),
    TENANT_CLIENT("租户后台"),
    SYSTEM_MANAGE("系统管理"),
    GAME_MANAGE("游戏管理"),
    REPORT_MANAGE("报表管理"),
    FINANCIAL_MANAGE("资金管理"),
    IN_OUT_CONFIG("出入款配置"),
    ACCOUNT_MANAGE("账号管理"),
    PLATFORM_CLIENT("总控后台"),
    NEW_LOTTERY("新彩票管理"),
    LOTTERY("彩票管理"),
    ACTIVITY_MANAGE("活动管理"),
    SPORTS_MANAGE("体育管理"),
    USER_CENTER("用户中心"),
    AUTH_SERVER("验证服务"),
    PLATFORM_CENTER("平台中心"),
    PAY_CENTER("支付中心"),
    SETTING_CENTER("设置中心"),
    CHAT_MANAGE("聊天室管理"),
    REBATE_MANAGE("分红管理");

    private String clientName;

    ClientTypeEnum(String clientName) {
        this.clientName = clientName;
    }
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
