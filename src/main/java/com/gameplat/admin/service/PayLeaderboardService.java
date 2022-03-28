package com.gameplat.admin.service;

import com.gameplat.admin.model.bean.PayLeaderboardParam;
import com.gameplat.admin.model.bean.PayLeaderboardResult;
import com.gameplat.admin.model.bean.PayLeaderboardSearch;

public interface PayLeaderboardService {
    /**
     * 获取支付排行榜搜索条件数据
     *
     * @Author zak
     * @Date 2022/1/18 21:00
     * @return PayLeaderboardSearch
     */
    PayLeaderboardSearch getLeaderboardSearch();

    /**
     * 支付排行榜数据
     *
     * @Author zak
     * @Date 2022/1/18 21:47
     * @Param payLeaderboardVo
     * @return PayLeaderboardResult
     */
    PayLeaderboardResult getLeaderboard(PayLeaderboardParam payLeaderboardParam);
}
