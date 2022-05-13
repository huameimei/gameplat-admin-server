package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.setting.SysDomain;

/**
 * @author Lily
 */
public interface SysDomainService extends IService<SysDomain> {

  String getChatDomain();

  String getOssDomain();
}
