package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.AuthIpMapper;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.service.SysAuthIpService;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/** @author Lenovo */
@Service
public class SysAuthIpServiceImpl extends ServiceImpl<AuthIpMapper, SysAuthIp>
    implements SysAuthIpService {

  /** 获取所有 */
  @Override
  public List<SysAuthIp> listByIp(String ip) {
    return StringUtils.isNotBlank(ip)
        ? this.lambdaQuery().eq(SysAuthIp::getAllowIp, ip).list()
        : this.lambdaQuery().list();
  }

  @Override
  public boolean isPermitted(String ip) {
      Set<String> permittedIpSet = this.lambdaQuery().list().stream().map(SysAuthIp::getAllowIp).collect(
          Collectors.toSet());
      return StringUtils.isNotEmpty(ip) && (permittedIpSet.isEmpty() || permittedIpSet.contains(ip));
  }

  @Override
  public boolean isExist(String ip) {
    Set<String> permittedIpSet = this.lambdaQuery().list().stream().map(SysAuthIp::getAllowIp).collect(
        Collectors.toSet());
    return !permittedIpSet.isEmpty() && permittedIpSet.contains(ip);
  }
}
