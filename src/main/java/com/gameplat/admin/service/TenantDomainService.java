package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.setting.TenantDomain;

/**
 * @author Lily
 */
public interface TenantDomainService extends IService<TenantDomain> {

  String getChatDomain();
}
