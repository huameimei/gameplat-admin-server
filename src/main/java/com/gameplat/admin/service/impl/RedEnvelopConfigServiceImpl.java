package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.RedEnvelopeConfigConvert;
import com.gameplat.admin.mapper.RedEnvelopConfigMapper;
import com.gameplat.admin.model.dto.RedEnvelopeConfigDTO;
import com.gameplat.admin.service.RedEnvelopeConfigService;
import com.gameplat.model.entity.recharge.RedEnvelopeConfig;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RedEnvelopConfigServiceImpl extends ServiceImpl<RedEnvelopConfigMapper, RedEnvelopeConfig>
        implements RedEnvelopeConfigService {

  @Autowired
  private RedEnvelopeConfigConvert redConvert;

  /**
   * 添加红包配置
   */
  @Override
  public boolean redAdd(RedEnvelopeConfigDTO dto) {
    UserCredential credential = SecurityUserHolder.getCredential();
    dto.setCreateBy(credential.getUsername());
    return this.save(redConvert.dtoToEntity(dto));
  }

  /**
   * 获取红包列表
   */
  @Override
  public IPage<RedEnvelopeConfig> redList(PageDTO<RedEnvelopeConfig> page, RedEnvelopeConfigDTO dto) {
    return this.lambdaQuery()
            .like(ObjectUtils.isNotEmpty(dto.getRedName()),RedEnvelopeConfig::getRedName,dto.getRedName())
            .eq(ObjectUtils.isNotEmpty(dto.getIsAging()),RedEnvelopeConfig::getIsAging,dto.getIsAging())
            .eq(ObjectUtils.isNotEmpty(dto.getState()),RedEnvelopeConfig::getState,dto.getState())
            .eq(ObjectUtils.isNotEmpty(dto.getReceiveMethod()),RedEnvelopeConfig::getReceiveMethod,dto.getReceiveMethod())
            .eq(ObjectUtils.isNotEmpty(dto.getReceiveStartTime()),RedEnvelopeConfig::getReceiveStartTime,dto.getReceiveStartTime())
            .eq(ObjectUtils.isNotEmpty(dto.getReceiveEndTime()),RedEnvelopeConfig::getReceiveEndTime,dto.getReceiveEndTime())
            .orderByDesc(RedEnvelopeConfig::getCreateTime)
            .orderByDesc(RedEnvelopeConfig::getUpdateTime)
            .page(page);
  }

  /**
   * 修改红包配置
   */
  @Override
  public boolean redEdit(RedEnvelopeConfigDTO dto) {
    UserCredential credential = SecurityUserHolder.getCredential();
    dto.setUpdateBy(credential.getUsername());
    return this.lambdaUpdate()
            .set(ObjectUtils.isNotNull(dto.getRedName()), RedEnvelopeConfig::getRedName, dto.getRedName())
            .set(ObjectUtils.isNotNull(dto.getAmount()), RedEnvelopeConfig::getAmount, dto.getAmount())
            .set(ObjectUtils.isNotNull(dto.getMultiple()), RedEnvelopeConfig::getMultiple, dto.getMultiple())
            .set(ObjectUtils.isNotNull(dto.getIsAging()), RedEnvelopeConfig::getIsAging, dto.getIsAging())
            .set(ObjectUtils.isNotNull(dto.getReceiveStartTime()), RedEnvelopeConfig::getReceiveStartTime, dto.getReceiveStartTime())
            .set(ObjectUtils.isNotNull(dto.getReceiveEndTime()), RedEnvelopeConfig::getReceiveEndTime, dto.getReceiveEndTime())
            .set(ObjectUtils.isNotNull(dto.getRechargeAmount()), RedEnvelopeConfig::getRechargeAmount, dto.getRechargeAmount())
            .set(ObjectUtils.isNotNull(dto.getChipRequire()), RedEnvelopeConfig::getChipRequire, dto.getChipRequire())
            .set(ObjectUtils.isNotNull(dto.getReceiveMethod()), RedEnvelopeConfig::getReceiveMethod, dto.getReceiveMethod())
            .set(ObjectUtils.isNotNull(dto.getImgUrl()), RedEnvelopeConfig::getImgUrl, dto.getImgUrl())
            .set(ObjectUtils.isNotNull(dto.getLocation()), RedEnvelopeConfig::getLocation, dto.getLocation())
            .set(ObjectUtils.isNotNull(dto.getRemark()), RedEnvelopeConfig::getRemark, dto.getRemark())
            .set(ObjectUtils.isNotNull(dto.getUpdateBy()), RedEnvelopeConfig::getUpdateBy, dto.getUpdateBy())
            .set(RedEnvelopeConfig::getUpdateTime,new Date())
            .eq(ObjectUtils.isNotNull(), RedEnvelopeConfig::getId, dto.getId()).update();
  }

  /**
   * 删除红包配置
   */
  @Override
  public boolean redDelete(List<Integer> ids) {
    return this.removeByIds(ids);
  }


}
