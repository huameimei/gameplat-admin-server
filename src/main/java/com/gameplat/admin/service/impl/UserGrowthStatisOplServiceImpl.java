package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.UserGrowthStatisOplMapper;
import com.gameplat.admin.service.UserGrowthStatisOplService;
import com.gameplat.model.entity.move.UserGrowthStatisOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 VIP汇总 @Author : cc @Date : 2022/4/18 */
@Service
public class UserGrowthStatisOplServiceImpl
    extends ServiceImpl<UserGrowthStatisOplMapper, UserGrowthStatisOpl>
    implements UserGrowthStatisOplService {}
