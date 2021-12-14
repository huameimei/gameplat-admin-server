package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ActivityPrize;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;

import java.util.List;

/**
 * 活动奖品信息
 */
public interface ActivityPrizeService extends IService<ActivityPrize> {
    /**
     * 查询奖品信息
     *
     * @param memberActivityPrizeBean
     * @return
     */
    List<MemberActivityPrizeVO> findActivityPrizeList(MemberActivityPrizeVO memberActivityPrizeBean);
}
