package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.TenantDomainMapper;
import com.gameplat.admin.service.TenantDomainService;
import com.gameplat.model.entity.TenantDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TenantDomainServiceImpl extends ServiceImpl<TenantDomainMapper, TenantDomain>
    implements TenantDomainService {

  @Override
  public String getChatDomain() {
    TenantDomain tenantDomain =
        lambdaQuery().eq(TenantDomain::getDomainType, "chat_api_domain").one();
    String chatDomain = tenantDomain.getDomain();
    if (chatDomain.endsWith("/")) {
      return chatDomain.substring(0, chatDomain.length() - 1);
    } else {
      return chatDomain;
    }
  }
}
