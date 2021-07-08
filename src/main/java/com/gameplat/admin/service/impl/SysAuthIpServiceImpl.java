package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysAuthIpConvert;
import com.gameplat.admin.dao.AuthIpMapper;
import com.gameplat.admin.model.dto.SysAuthIpAddDTO;
import com.gameplat.admin.model.dto.SysAuthIpQueryDTO;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVO;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.exception.ServiceException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author Lenovo */
@Service
public class SysAuthIpServiceImpl extends ServiceImpl<AuthIpMapper, SysAuthIp>
    implements SysAuthIpService {
  @Autowired private AuthIpMapper authIpMapper;

  @Autowired private SysAuthIpConvert sysAuthIpConvert;

  @Override
  public boolean isPermitted(String ip) {
    Set<String> permittedIpSet =
        this.lambdaQuery().list().stream().map(SysAuthIp::getAllowIp).collect(Collectors.toSet());
    return StringUtils.isNotEmpty(ip) && (permittedIpSet.isEmpty() || permittedIpSet.contains(ip));
  }

  @Override
  public boolean isExist(String ip) {
    Set<String> permittedIpSet =
        this.lambdaQuery().list().stream().map(SysAuthIp::getAllowIp).collect(Collectors.toSet());
    return !permittedIpSet.isEmpty() && permittedIpSet.contains(ip);
  }

  @Override
  public IPage<SysAuthIpVO> queryPage(IPage<SysAuthIp> page, SysAuthIpQueryDTO queryDto) {
    LambdaQueryWrapper<SysAuthIp> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotBlank(queryDto.getAuthIp())) {
      query.like(SysAuthIp::getAllowIp, queryDto.getAuthIp());
    }
    return this.page(page, query).convert(sysAuthIpConvert::toVo);
  }

  @Override
  public void save(SysAuthIpAddDTO sysAuthIpAddDto) throws ServiceException {
    LambdaQueryWrapper<SysAuthIp> query = Wrappers.lambdaQuery();
    query.eq(SysAuthIp::getAllowIp, sysAuthIpAddDto.getAllowIp());
    if (this.count(query) > 0) {
      throw new ServiceException("IP已经存在，请勿重复添加");
    }
    //    sysAuthIpAddDto.setCreateBy(SecurityUtil.getUserName());
    sysAuthIpAddDto.setCreateTime(new Date());
    //    sysAuthIpAddDto.setUpdateBy(SecurityUtil.getUserName());
    sysAuthIpAddDto.setUpdateTime(new Date());
    if (!this.save(sysAuthIpConvert.toEntity(sysAuthIpAddDto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    authIpMapper.deleteById(id);
  }
}
