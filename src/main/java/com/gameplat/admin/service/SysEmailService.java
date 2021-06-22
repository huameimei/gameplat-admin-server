package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysEmailQueryDto;
import com.gameplat.admin.model.dto.SysSmsQueryDto;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.entity.SysSms;
import java.util.List;

/**
 * @author Lenovo
 */
public interface SysEmailService extends IService<SysEmail> {

  List<SysEmail> listByQueryDto(SysEmailQueryDto sysEmailQueryDto);
}
