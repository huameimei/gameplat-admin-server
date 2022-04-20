package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.UserGrowthLevelOplMapper;
import com.gameplat.admin.service.UserGrowthLevelOplService;
import com.gameplat.model.entity.move.UserGrowthLevelOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 VIP等级 @Author : cc @Date : 2022/4/18 */
@Service
public class UserGrowthLevelOplServiceImpl
    extends ServiceImpl<UserGrowthLevelOplMapper, UserGrowthLevelOpl>
    implements UserGrowthLevelOplService {}
