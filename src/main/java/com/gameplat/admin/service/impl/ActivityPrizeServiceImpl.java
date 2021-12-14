package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityPrizeMapper;
import com.gameplat.admin.model.domain.ActivityPrize;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;
import com.gameplat.admin.service.ActivityPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityPrizeServiceImpl extends ServiceImpl<ActivityPrizeMapper, ActivityPrize>
        implements ActivityPrizeService {

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public List<MemberActivityPrizeVO> findActivityPrizeList(MemberActivityPrizeVO memberActivityPrizeBean) {
        return activityPrizeMapper.findActivityPrizeList(memberActivityPrizeBean);
    }
}
