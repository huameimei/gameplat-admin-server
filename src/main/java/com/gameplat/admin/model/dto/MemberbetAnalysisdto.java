package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb 投注分析dto
 * @Date 2022/2/24 22:15
 * @Version 1.0
 *
 * */
 @Data
public class MemberbetAnalysisdto implements Serializable {

    /**
     * 会员账号
     */
    private String account;

    /**
     * 是否含线下会员( 0 不包含 、 1 包含 )
     */
    private Integer isTrue = 0;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
