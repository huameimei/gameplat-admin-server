package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.TenantDomainMapper;
import com.gameplat.admin.service.TenantDomainService;
import com.gameplat.model.entity.setting.TenantDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @date 2022/2/16
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TenantDomainServiceImpl extends ServiceImpl<TenantDomainMapper, TenantDomain>
    implements TenantDomainService {

  @Override
  public String getChatDomain() {
    return this.lambdaQuery()
        .eq(TenantDomain::getDomainType, "chat_api_domain")
        .oneOpt()
        .map(TenantDomain::getDomain)
        .filter(domain -> domain.endsWith("/"))
        .map(domain -> domain.substring(0, domain.length() - 1))
        .orElse("");
  }
}
