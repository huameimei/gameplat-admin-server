package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.SysEmailMapper;
import com.gameplat.admin.model.dto.SysEmailQueryDto;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.service.SysEmailService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
public class SysEmailServiceImpl extends ServiceImpl<SysEmailMapper, SysEmail> implements
    SysEmailService {

  @Override
  public List<SysEmail> listByQueryDto(SysEmailQueryDto sysEmailQueryDto) {
    return this.lambdaQuery().like(SysEmail::getTitle,sysEmailQueryDto.getTitle())
        .eq(SysEmail::getType,sysEmailQueryDto.getType())
        .eq(SysEmail::getStatus,sysEmailQueryDto.getStatus()).list();

  }
}
