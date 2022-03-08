package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.AuthIpConvert;
import com.gameplat.admin.mapper.SysAuthIpMapper;
import com.gameplat.admin.model.dto.AuthIpDTO;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.sys.SysAuthIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * ip白名单 服务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysAuthIpServiceImpl extends ServiceImpl<SysAuthIpMapper, SysAuthIp>
    implements SysAuthIpService {

  @Autowired private AuthIpConvert authIpConvert;

  @Override
  @SentinelResource(value = "selectAuthIpList")
  public IPage<AuthIpVo> selectAuthIpList(IPage<SysAuthIp> page, AuthIpDTO authIpDTO) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(authIpDTO.getIp()), SysAuthIp::getAllowIp, authIpDTO.getIp())
        .apply(
            ObjectUtils.isNotEmpty(authIpDTO.getIpType()),
            "FIND_IN_SET ('" + authIpDTO.getIpType() + "',ip_type)")
        .like(
            ObjectUtils.isNotEmpty(authIpDTO.getRemark()),
            SysAuthIp::getRemark,
            authIpDTO.getRemark())
        .page(page)
        .convert(authIpConvert::toVo);
  }

  @Override
  @SentinelResource(value = "addAuthIp")
  @CacheInvalidate(name = CachedKeys.AUTH_IP, key = "'all'")
  public void addAuthIp(OperAuthIpDTO dto) {
    SysAuthIp authIp = authIpConvert.toEntity(dto);
    Assert.isFalse(isExist(dto.getIp()), "IP已存在");
    Assert.isTrue(this.save(authIp), "保存失败!");
  }

  @Override
  @SentinelResource(value = "updateAuthIp")
  @CacheInvalidate(name = CachedKeys.AUTH_IP, key = "'all'")
  public void updateAuthIp(OperAuthIpDTO operAuthIpDTO) {
    Assert.isTrue(this.updateById(authIpConvert.toEntity(operAuthIpDTO)), "更新失败!");
  }

  @Override
  @CacheInvalidate(name = CachedKeys.AUTH_IP, key = "'all'")
  @SentinelResource(value = "deleteAuthIp")
  public void deleteAuthIp(Long id) {
    Assert.isTrue(this.removeById(id), "删除失败!");
  }

  @Override
  @SentinelResource(value = "deleteBatch")
  @CacheInvalidate(name = CachedKeys.AUTH_IP, key = "'all'")
  public void deleteBatch(String ids) {
    Assert.isTrue(this.removeByIds(Arrays.asList(StringUtils.split(ids, ","))), "批量删除失败!");
  }

  @Override
  @SentinelResource(value = "checkAuthIpUnique")
  public boolean checkAuthIpUnique(String ip) {
    return !isExist(ip);
  }

  @Override
  @CachePenetrationProtect
  @Cached(name = CachedKeys.AUTH_IP, key = "'all'", expire = 3600)
  @CacheRefresh(refresh = 600, stopRefreshAfterLastAccess = 7200)
  @SentinelResource(value = "getAll")
  public List<SysAuthIp> getAll() {
    return this.lambdaQuery().select(SysAuthIp::getAllowIp, SysAuthIp::getIpType).list();
  }

  @Override
  public boolean isExist(String ip) {
    return this.lambdaQuery().eq(SysAuthIp::getAllowIp, ip).exists();
  }
}
