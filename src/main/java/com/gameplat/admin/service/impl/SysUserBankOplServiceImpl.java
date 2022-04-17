package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysUserBankOplMapper;
import com.gameplat.admin.service.SysUserBankOplService;
import com.gameplat.model.entity.move.SysUserBankOpl;
import org.springframework.stereotype.Service;

/** @Description : 老平台 用户银行卡 @Author : cc @Date : 2022/4/18 */
@Service
public class SysUserBankOplServiceImpl extends ServiceImpl<SysUserBankOplMapper, SysUserBankOpl>
    implements SysUserBankOplService {}
