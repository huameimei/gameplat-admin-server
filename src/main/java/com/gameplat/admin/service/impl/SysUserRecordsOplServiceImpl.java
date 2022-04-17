package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysUserRecordsOplMapper;
import com.gameplat.admin.service.SysUserRecordsOplService;
import com.gameplat.model.entity.move.SysUserRecordsOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 用户充提报表 @Author : cc @Date : 2022/4/18 */
@Service
public class SysUserRecordsOplServiceImpl
    extends ServiceImpl<SysUserRecordsOplMapper, SysUserRecordsOpl>
    implements SysUserRecordsOplService {}
