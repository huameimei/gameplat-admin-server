package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysRecommendUserOplMapper;
import com.gameplat.admin.service.SysRecommendUserOplService;
import com.gameplat.model.entity.move.SysRecommendUserOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 用户代理结构 @Author : cc @Date : 2022/4/18 */
@Service
public class SysRecommendUserOplServiceImpl
    extends ServiceImpl<SysRecommendUserOplMapper, SysRecommendUserOpl>
    implements SysRecommendUserOplService {}
