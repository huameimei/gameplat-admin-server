package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysEmailConvert;
import com.gameplat.admin.dao.SysEmailMapper;
import com.gameplat.admin.model.dto.SysEmailQueryDto;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVo;
import com.gameplat.admin.service.SysEmailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author Lenovo */
@Service
public class SysEmailServiceImpl extends ServiceImpl<SysEmailMapper, SysEmail>
    implements SysEmailService {

  @Autowired private SysEmailConvert sysEmailConvert;

  @Override
  public IPage<SysEmailVo> queryPage(IPage<SysEmail> page, SysEmailQueryDto queryDto) {
    LambdaQueryWrapper<SysEmail> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotBlank(queryDto.getTitle())) {
      query.like(SysEmail::getTitle, queryDto.getTitle());
    }
    if (queryDto.getType() != null) {
      query.eq(SysEmail::getType, queryDto.getType());
    }
    if (queryDto.getStatus() != null) {
      query.eq(SysEmail::getStatus, queryDto.getStatus());
    }
    return this.page(page, query).convert(sysEmailConvert::toVo);
  }
}
