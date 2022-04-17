package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysLayerConfigOplMapper;
import com.gameplat.admin.service.SysLayerConfigOplService;
import com.gameplat.model.entity.move.SysLayerConfigOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 用户充值层级 @Author : cc @Date : 2022/4/18 */
@Service
public class SysLayerConfigOplServiceImpl
    extends ServiceImpl<SysLayerConfigOplMapper, SysLayerConfigOpl>
    implements SysLayerConfigOplService {}
