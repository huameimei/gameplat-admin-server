package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;
import com.gameplat.model.entity.activity.ActivityPrize;

import java.util.List;

/** 活动奖品信息 */
public interface ActivityPrizeService extends IService<ActivityPrize> {

  /**
   * 查询奖品信息
   *
   * @param memberActivityPrizeBean MemberActivityPrizeVO
   * @return List
   */
  List<MemberActivityPrizeVO> findActivityPrizeList(MemberActivityPrizeVO memberActivityPrizeBean);
}
