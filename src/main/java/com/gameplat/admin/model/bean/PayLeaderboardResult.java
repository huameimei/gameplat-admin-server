package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 支付排行榜数据结果集
 *
 * @Author zak
 * @Date 2022/01/18 21:17:31
 */
@Data
public class PayLeaderboardResult {
    @ApiModelProperty("使用平台")
    private List<PayLeaderboard> usePlatform;

    @ApiModelProperty("成功支付自动入款率")
    private List<PayLeaderboard> successPayRate;

    @ApiModelProperty("成功笔数最多")
    private List<PayLeaderboard> successPayNum;

    @ApiModelProperty("成功金额最多")
    private List<PayLeaderboard> successPayAmount;
}
