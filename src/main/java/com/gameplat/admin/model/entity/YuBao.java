package com.gameplat.admin.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class YuBao {

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员ID
     */
    private Long userId;

    /**
     * 会员账号
     */
    private String userAccount;

    /**
     * 用户正式姓名
     */
    private String fullName;

    /**
     * 金额 单位分
     */
    private Long money;

    /**
     * 转入时间
     */
    private Date transferInTime;

}
