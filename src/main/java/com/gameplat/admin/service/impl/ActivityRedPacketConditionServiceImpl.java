package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityRedPacketConditionMapper;
import com.gameplat.admin.service.ActivityRedPacketConditionService;
import com.gameplat.model.entity.activity.ActivityRedPacketCondition;
import org.springframework.stereotype.Service;

/**
 * @author admin
 */
@Service
public class ActivityRedPacketConditionServiceImpl
    extends ServiceImpl<ActivityRedPacketConditionMapper, ActivityRedPacketCondition>
    implements ActivityRedPacketConditionService {}
