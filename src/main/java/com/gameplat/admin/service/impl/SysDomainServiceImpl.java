package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysDomainMapper;
import com.gameplat.admin.service.SysDomainService;
import com.gameplat.model.entity.setting.SysDomain;
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
public class SysDomainServiceImpl extends ServiceImpl<SysDomainMapper, SysDomain>
    implements SysDomainService {

  @Override
  public String getChatDomain() {
    SysDomain sysDomain =
            lambdaQuery().eq(SysDomain::getDomainType, "chat_api_domain").one();
    String chatDomain = sysDomain.getDomain();
    if (chatDomain.endsWith("/")) {
      return chatDomain.substring(0, chatDomain.length() - 1);
    } else {
      return chatDomain;
    }
  }
}
