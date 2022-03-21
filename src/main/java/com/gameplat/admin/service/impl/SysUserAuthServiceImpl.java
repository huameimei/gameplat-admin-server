package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysUserAuthConvert;
import com.gameplat.admin.mapper.SysUserAuthMapper;
import com.gameplat.admin.model.dto.SysUserAuthDto;
import com.gameplat.admin.model.vo.SysUserAuthVo;
import com.gameplat.admin.service.SysUserAuthService;
import com.gameplat.model.entity.sys.SysUserAuth;
import com.gameplat.security.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/** kb @Date 2022/3/12 17:20 @Version 1.0 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysUserAuthServiceImpl extends ServiceImpl<SysUserAuthMapper, SysUserAuth>
    implements SysUserAuthService {

  @Autowired private SysUserAuthConvert sysUserAuthConvert;

  @Override
  public void save(SysUserAuthDto dto) {
    String username = SecurityUserHolder.getCredential().getUsername();
    if (ObjectUtil.isEmpty(dto.getAuthId())) {
      dto.setCreateTime(new Date());
      dto.setCreateBy(username);
      Assert.isTrue(this.save(sysUserAuthConvert.toEntity(dto)), "新增认证失败配置失败!");
    } else {
      dto.setUpdateTime(new Date());
      dto.setUpdateBy(username);
      Assert.isTrue(this.updateById(sysUserAuthConvert.toEntity(dto)), "新增认证失败配置失败!");
    }
  }

  @Override
  public List<SysUserAuthVo> findAuth() {
    return this.lambdaQuery().list().stream()
        .map(sysUserAuthConvert::toVo)
        .collect(Collectors.toList());
  }
}
