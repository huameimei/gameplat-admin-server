package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityTurntableMapper;
import com.gameplat.admin.service.ActivityTurntableService;
import com.gameplat.model.entity.activity.ActivityTurntable;
import org.springframework.stereotype.Service;

/**
 * @author admin
 */
@Service
public class ActivityTurntableServiceImpl
    extends ServiceImpl<ActivityTurntableMapper, ActivityTurntable>
    implements ActivityTurntableService {}
