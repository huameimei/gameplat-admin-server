package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysAuthIpAddDto;
import com.gameplat.admin.model.dto.SysAuthIpQueryDto;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVo;

/**
 * IP白名单
 * @author Lenovo
 */
public interface SysAuthIpService extends IService<SysAuthIp> {


  boolean isPermitted(String ip);

  boolean isExist(String ip);

  IPage<SysAuthIpVo> queryPage(IPage<SysAuthIp> sysAuthIp, SysAuthIpQueryDto queryDto);

  void save(SysAuthIpAddDto sysAuthIpAddDto);

  void delete(Long id);
}
