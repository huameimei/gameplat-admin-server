package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/1 22:15
 * @Version 1.0
 */
@Data
public class GameKGLotteryDto implements Serializable {

    private String account;

    private String superAccount;

    private int lotteryType;

    private String lotteryCode;

    private String startTime;

    private String endTime;
}
