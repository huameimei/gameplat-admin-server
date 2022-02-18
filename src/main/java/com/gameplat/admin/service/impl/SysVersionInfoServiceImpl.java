package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysVersionInfoConvert;
import com.gameplat.admin.mapper.SysVersionInfoMapper;
import com.gameplat.admin.model.domain.SysVersionInfo;
import com.gameplat.admin.model.dto.SysVersionInfoDTO;
import com.gameplat.admin.service.SysVersionInfoService;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 获取发版信息
 */
@Service
@RequiredArgsConstructor
public class SysVersionInfoServiceImpl extends ServiceImpl<SysVersionInfoMapper, SysVersionInfo> implements SysVersionInfoService {


  private final SysVersionInfoMapper sysPackageInfoMapper;

  private final SysVersionInfoConvert convert;

  /**
   * 获取发版信息列表
   */
  @Override
  @SentinelResource(value = "getSysPackageInfo")
  public IPage<SysVersionInfo> getSysPackageInfo(IPage<SysVersionInfo> page, SysVersionInfoDTO dto) {
    return this.lambdaQuery()
//            .eq(ObjectUtils.isNotEmpty(dto.getVersionNum()), SysPackageInfo::getVersionNum,dto.getVersionNum())
            .orderByDesc(SysVersionInfo::getCreateTime)
            .page(page);
  }

  /**
   * 创建新的发版信息
   */
  @Override
  public boolean createSysPackageInfo(SysVersionInfoDTO dto) {
    UserCredential credential = SecurityUserHolder.getCredential();
    dto.setCreateBy(credential.getUsername());
    return this.save(convert.toEntity(dto));
  }

  /**
   * 编辑发版信息
   */
  @Override
  public int editSysPackageInfo(SysVersionInfoDTO dto) {
    UserCredential credential = SecurityUserHolder.getCredential();
    dto.setCreateBy(credential.getUsername());
    LambdaUpdateWrapper<SysVersionInfo> update = Wrappers.lambdaUpdate();
    // todo 标题，版本号，内容，强更新，状态，企业签开关，企业签，超级签开关，超级签地址，描述文件，安卓包地址
    update.set(ObjectUtils.isNotNull(dto.getTitle()),SysVersionInfo::getTitle,dto.getTitle());
    update.set(ObjectUtils.isNotNull(dto.getVersionNum()),SysVersionInfo::getVersionNum,dto.getVersionNum());
    update.set(ObjectUtils.isNotNull(dto.getContext()),SysVersionInfo::getContext,dto.getContext());
    update.set(ObjectUtils.isNotNull(dto.getUrlType()),SysVersionInfo::getUrlType,dto.getUrlType());
    update.set(ObjectUtils.isNotNull(dto.getUrl()),SysVersionInfo::getUrl,dto.getUrl());
    update.set(ObjectUtils.isNotNull(dto.getIpaUrl()),SysVersionInfo::getIpaUrl,dto.getIpaUrl());
    update.set(ObjectUtils.isNotNull(dto.getIsThirdSign()),SysVersionInfo::getIsThirdSign,dto.getIsThirdSign());
    update.set(ObjectUtils.isNotNull(dto.getThirdSignUrl()),SysVersionInfo::getThirdSignUrl,dto.getThirdSignUrl());
    update.set(ObjectUtils.isNotNull(dto.getSuperSignStatus()),SysVersionInfo::getSuperSignStatus,dto.getSuperSignStatus());
    update.set(ObjectUtils.isNotNull(dto.getPlistFileId()),SysVersionInfo::getPlistFileId,dto.getPlistFileId());
    update.set(ObjectUtils.isNotNull(dto.getRemarkFileUrl()),SysVersionInfo::getRemarkFileUrl,dto.getRemarkFileUrl());
    update.eq(SysVersionInfo::getId,dto.getId());
    return sysPackageInfoMapper.update(null,update);
  }

  /**
   * 删除发版信息
   */
  @Override
  public boolean removeSysPackageInfo(Integer id) {
    return this.removeById(id);
  }


}
