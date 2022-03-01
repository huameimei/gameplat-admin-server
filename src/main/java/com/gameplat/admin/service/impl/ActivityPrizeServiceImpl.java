package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityPrizeMapper;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;
import com.gameplat.admin.service.ActivityPrizeService;
import com.gameplat.model.entity.activity.ActivityPrize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActivityPrizeServiceImpl extends ServiceImpl<ActivityPrizeMapper, ActivityPrize>
    implements ActivityPrizeService {

  @Autowired private ActivityPrizeMapper activityPrizeMapper;

  @Override
  public List<MemberActivityPrizeVO> findActivityPrizeList(
      MemberActivityPrizeVO memberActivityPrizeBean) {
    return activityPrizeMapper.findActivityPrizeList(memberActivityPrizeBean);
  }
}
