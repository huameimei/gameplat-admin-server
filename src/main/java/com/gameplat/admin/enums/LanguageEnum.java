package com.gameplat.admin.enums;

/**
 * @author: lily
 * @description: 语言枚举
 * @date 2021/11/20
 **/
public enum LanguageEnum {
    //客户端传参
    app_zh_CN("zh-CN", "简体中文(中国)"),
    app_en_US("en-US", "英文(美国)"),
    app_in_ID("in-ID","印尼"),
    app_th_TH("th-TH","泰文"),
    app_vi_VN("vi-VN","越南语"),
    //第三方
    zh_CN("zh_CN", "简体中文(中国)"), // BG
    CHN("CHN", "简体中文"),
    CN("CN", "简体中文"),
    cn("cn", "简体中文"), //AE
    zh_SG("zh_SG", "简体中文(新加坡)"),
    THN("THN", "繁体中文"),
    zh_HK("zh_HK", "繁体中文(香港)"),
    zh_TW("zh_TW", "繁体中文(台湾)"),
    en_US("en_US", "英文(美国)"), // BG
    ENG("ENG", "英文"),
    EN("EN", "英文"),
    en("en", "英文"), //AE
    en_IN("en_IN", "英文(印度)"),
    in_ID("in_ID","印尼"),
    id_ID("id_ID","印尼"), // BG
    JP("JP","日本語"),
    KR("KR","韩语"),
    vi_VN("vi_VN","越南语"),
    VN("VN","越南语"),
    vn("vn","越南语"),//AE
    VI("vi","越南语"),
    th_TH("th_TH","泰文"), // BG
    th("th","泰文"), // AE
    TH("TH","泰语"),
    ES("ES","西班牙语"),
    PT("PT","葡萄牙语"),
    FR("FR","法语"),
    DE("DE","德语"),
    IT("IT","意大利语"),
    RU("RU","俄语"),
    ID("ID","印尼语");


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String code;
    private String name;

    LanguageEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public static LanguageEnum get(String code) {
        LanguageEnum[] vs = LanguageEnum.values();
        for (LanguageEnum t : vs) {
            if (t.getCode().equalsIgnoreCase(code)) {
                return t;
            }
        }
        return null;
    }
}
