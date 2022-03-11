package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/7 14:31
 * @Version 1.0
 */
@Data
public class UserGameBetRecordDto implements Serializable {

    /**
     *用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String account;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 游戏大类
     */
    private String gameType;


    /**
     * 平台编码
     */
    private String platformCode;


    /**
     * 游戏编码
     */
    private String gameKind;
}
