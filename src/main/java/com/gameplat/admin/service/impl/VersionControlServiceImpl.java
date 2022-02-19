package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.VersionControlConvert;
import com.gameplat.admin.mapper.VersionControlMapper;
import com.gameplat.admin.model.domain.VersionControl;
import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.admin.service.VersionControlService;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 获取发版信息
 */
@Service
@RequiredArgsConstructor
public class VersionControlServiceImpl extends ServiceImpl<VersionControlMapper, VersionControl> implements VersionControlService {


  private final VersionControlMapper sysPackageInfoMapper;

  private final VersionControlConvert convert;

  /**
   * 获取发版信息列表
   */
  @Override
  @SentinelResource(value = "getSysPackageInfo")
  public IPage<VersionControl> getSysPackageInfo(IPage<VersionControl> page, VersionControlDTO dto) {
    return this.lambdaQuery()
//            .eq(ObjectUtils.isNotEmpty(dto.getVersionNum()), SysPackageInfo::getVersionNum,dto.getVersionNum())
            .orderByDesc(VersionControl::getCreateTime)
            .page(page);
  }

  /**
   * 创建新的发版信息
   */
  @Override
  @SentinelResource(value = "createSysPackageInfo")
  public boolean createSysPackageInfo(VersionControlDTO dto) {
    UserCredential credential = SecurityUserHolder.getCredential();
    dto.setCreateBy(credential.getUsername());
    return this.save(convert.toEntity(dto));
  }

  /**
   * 编辑发版信息
   */
  @Override
  @SentinelResource(value = "editSysPackageInfo")
  public int editSysPackageInfo(VersionControlDTO dto) {
    UserCredential credential = SecurityUserHolder.getCredential();
    dto.setCreateBy(credential.getUsername());
    LambdaUpdateWrapper<VersionControl> update = Wrappers.lambdaUpdate();
    update.set(ObjectUtils.isNotNull(dto.getTitle()), VersionControl::getTitle,dto.getTitle());
    update.set(ObjectUtils.isNotNull(dto.getVersion()), VersionControl::getVersion,dto.getVersion());
    update.set(ObjectUtils.isNotNull(dto.getForceUpdate()), VersionControl::getForceUpdate,dto.getForceUpdate());
    update.set(ObjectUtils.isNotNull(dto.getContent()), VersionControl::getContent,dto.getContent());
    update.set(ObjectUtils.isNotNull(dto.getType()), VersionControl::getType,dto.getType());
    update.set(ObjectUtils.isNotNull(dto.getStart()), VersionControl::getStart,dto.getStart());
    update.set(ObjectUtils.isNotNull(dto.getAndroidUrl()), VersionControl::getAndroidUrl,dto.getAndroidUrl());
    update.set(ObjectUtils.isNotNull(dto.getAndroidUrlType()), VersionControl::getAndroidUrlType,dto.getAndroidUrlType());
    update.set(ObjectUtils.isNotNull(dto.getIosEnterpriseSing()), VersionControl::getIosEnterpriseSing,dto.getIosEnterpriseSing());
    update.set(ObjectUtils.isNotNull(dto.getIosSuperSing()), VersionControl::getIosSuperSing,dto.getIosSuperSing());
    update.set(ObjectUtils.isNotNull(dto.getIosDescribeUrl()), VersionControl::getIosDescribeUrl,dto.getIosDescribeUrl());
    update.eq(VersionControl::getId,dto.getId());
    return sysPackageInfoMapper.update(null,update);
  }

  /**
   * 删除发版信息
   */
  @Override
  @SentinelResource(value = "removeSysPackageInfo")
  public boolean removeSysPackageInfo(Integer id) {
    return this.removeById(id);
  }


}
