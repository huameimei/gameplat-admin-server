package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 在线用户DTO
 * @author three
 */
@Data
public class OnlineUserDTO {

    /**
     * 账号
     */
    private String userName;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 客户端类型
     */
    private Integer clientType;
    /**
     * 告警会员
     */
    private String specialAccounts;
}
