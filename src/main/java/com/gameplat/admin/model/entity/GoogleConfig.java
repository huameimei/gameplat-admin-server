package com.gameplat.admin.model.entity;

import lombok.Data;

@Data
public class GoogleConfig {

    /** 谷歌验证器密钥 */
    private String googleAuthSecret = "";

    /** 谷歌验证器账户 */
    private String googleAuthAccount = "";
}
