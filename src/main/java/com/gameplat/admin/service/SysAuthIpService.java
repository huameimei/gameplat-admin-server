package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysAuthIpAddDTO;
import com.gameplat.admin.model.dto.SysAuthIpQueryDTO;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVO;

/**
 * IP白名单
 *
 * @author Lenovo
 */
public interface SysAuthIpService extends IService<SysAuthIp> {

  boolean isPermitted(String ip);

  boolean isExist(String ip);

  IPage<SysAuthIpVO> queryPage(IPage<SysAuthIp> sysAuthIp, SysAuthIpQueryDTO queryDto);

  void save(SysAuthIpAddDTO sysAuthIpAddDto);

  void delete(Long id);
}
