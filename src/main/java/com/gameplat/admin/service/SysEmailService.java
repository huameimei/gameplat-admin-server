package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysEmailQueryDto;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVo;

/**
 * @author Lenovo
 */
public interface SysEmailService extends IService<SysEmail> {

  IPage<SysEmailVo> queryPage(IPage<SysEmail> page, SysEmailQueryDto sysEmailQueryDto);
}
