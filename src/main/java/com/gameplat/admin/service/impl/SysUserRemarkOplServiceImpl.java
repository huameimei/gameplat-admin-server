package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysUserRemarkOplMapper;
import com.gameplat.admin.service.SysUserRemarkOplService;
import com.gameplat.model.entity.move.SysUserRemarkOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 用户备注 @Author : cc @Date : 2022/4/18 */
@Service
public class SysUserRemarkOplServiceImpl
    extends ServiceImpl<SysUserRemarkOplMapper, SysUserRemarkOpl>
    implements SysUserRemarkOplService {}
