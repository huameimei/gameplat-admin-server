package com.gameplat.admin.model.domain.limit;

import lombok.Data;

import java.io.Serializable;

@Data
public class LiveTransferLimit implements Serializable {
    /**
     * 转入最大次数
     */
    private Integer dayInMaxCount;
    /**
     * 转入最大金额
     */
    private Double dayInMaxLimit;
    /**
     * 转入最大次数
     */
    private Integer dayOutMaxCount;
    /**
     * 转入最大金额
     */
    private Double dayOutMaxLimit;
    /**
     * 入款时间间隔
     */
    private Integer inTimeInterval;
    /**
     * 出款时间间隔
     */
    private Integer outTimeInterval;
}
