package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.UserGrowthConfigOplMapper;
import com.gameplat.admin.service.UserGrowthConfigOplService;
import com.gameplat.model.entity.move.UserGrowthConfigOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 VIP配置 @Author : cc @Date : 2022/4/18 */
@Service
public class UserGrowthConfigOplServiceImpl
    extends ServiceImpl<UserGrowthConfigOplMapper, UserGrowthConfigOpl>
    implements UserGrowthConfigOplService {}
