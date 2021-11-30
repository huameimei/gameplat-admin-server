package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityBlacklistMapper;
import com.gameplat.admin.model.domain.ActivityBlacklist;
import com.gameplat.admin.service.ActivityBlacklistService;
import org.springframework.stereotype.Service;

@Service
public class ActivityBlacklistServiceImpl extends ServiceImpl<ActivityBlacklistMapper, ActivityBlacklist>
        implements ActivityBlacklistService {
}
