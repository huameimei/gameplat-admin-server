package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/2 21:38
 * @Version 1.0
 */
@Data
public class GameRWDataReportDto implements Serializable {


    /**
     * 账号
     */
    private String account;

    /**
     * 代理账号
     */
    private String superAccount;

    /**
     * 是否直属会员 （1 是 0 否）
     */
    private int flag;

    /**
     * 开始日期
     */
    private String startTime;

    /**
     * 结束日期
     */
    private String endTime;
}
