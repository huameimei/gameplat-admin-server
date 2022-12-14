package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysVersionMapper;
import com.gameplat.admin.model.dto.VersionDTO;
import com.gameplat.admin.service.SysVersionService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.model.entity.sys.SysVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统版本 业务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysVersionServiceImpl extends ServiceImpl<SysVersionMapper, SysVersion>
    implements SysVersionService {

  @Override
  @SentinelResource(value = "selectVersionList")
  public IPage<SysVersion> selectVersionList(IPage<SysVersion> page, VersionDTO versionDTO) {
    return this.lambdaQuery()
        .eq(SysVersion::getStatus, EnableEnum.ENABLED.code())
        .ge(
            ObjectUtils.isNotEmpty(versionDTO.getBeginTime()),
            SysVersion::getCreateTime,
            versionDTO.getBeginTime())
        .le(
            ObjectUtils.isNotEmpty(versionDTO.getEndTime()),
            SysVersion::getCreateTime,
            versionDTO.getEndTime())
        .orderByDesc(SysVersion::getCreateTime)
        .page(page);
  }
}
