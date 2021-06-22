package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.SysSmsMapper;
import com.gameplat.admin.model.dto.SysSmsQueryDto;
import com.gameplat.admin.model.entity.SysSms;
import com.gameplat.admin.service.SysSmsService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
public class SysSmsServiceImpl extends ServiceImpl<SysSmsMapper,SysSms> implements
    SysSmsService {

  public List<SysSms> listByQueryDto(SysSmsQueryDto sysSmsQueryDto) {
    return this.lambdaQuery().like(SysSms::getPhone,sysSmsQueryDto.getPhone())
          .eq(SysSms::getSmsType,sysSmsQueryDto.getSmsType())
          .eq(SysSms::getStatus,sysSmsQueryDto.getStatus()).list();

  }
}
