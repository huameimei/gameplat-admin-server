package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.entity.SysAuthIp;
import java.util.List;

/**
 * IP白名单
 * @author Lenovo
 */
public interface SysAuthIpService extends IService<SysAuthIp> {

  List<SysAuthIp> listByIp(String ip);

  boolean isPermitted(String ip);

  boolean isExist(String ip);
}
