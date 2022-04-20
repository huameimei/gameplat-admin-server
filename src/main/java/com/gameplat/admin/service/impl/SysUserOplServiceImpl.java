package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysUserOplMapper;
import com.gameplat.admin.service.SysUserOplService;
import com.gameplat.model.entity.move.SysUserOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 用户信息 @Author : cc @Date : 2022/4/18 */
@Service
public class SysUserOplServiceImpl extends ServiceImpl<SysUserOplMapper, SysUserOpl>
    implements SysUserOplService {}
