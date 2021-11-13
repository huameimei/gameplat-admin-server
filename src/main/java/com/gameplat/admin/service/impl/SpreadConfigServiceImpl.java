package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadConfigConvert;
import com.gameplat.admin.mapper.SpreadConfigMapper;
import com.gameplat.admin.model.domain.SpreadConfig;
import com.gameplat.admin.model.dto.SpreadConfigAddDTO;
import com.gameplat.admin.model.dto.SpreadConfigDTO;
import com.gameplat.admin.model.dto.SpreadConfigEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadConfigService;
import com.gameplat.common.enums.SystemCodeType;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import com.gameplat.common.validator.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 域名推广配置 服务实现层
 *
 * @author three
 */
@Service
@RequiredArgsConstructor
public class SpreadConfigServiceImpl extends ServiceImpl<SpreadConfigMapper, SpreadConfig>
    implements SpreadConfigService {

  @Autowired private SpreadConfigMapper configMapper;
  @Autowired private SpreadConfigConvert configConvert;

  @Override
  public IPage<SpreadConfigVO> selectSpreadConfigList(
      IPage<SpreadConfig> page, SpreadConfigDTO configDTO) {
    LambdaQueryChainWrapper<SpreadConfig> queryWrapper =
        this.lambdaQuery()
            .eq(ObjectUtils.isNotNull(configDTO.getId()), SpreadConfig::getId, configDTO.getId())
            .eq(
                ObjectUtils.isNotEmpty(configDTO.getAgentAccount()),
                SpreadConfig::getAgentAccount,
                configDTO.getAgentAccount())
            .eq(
                ObjectUtils.isNotEmpty(configDTO.getSpreadType()),
                SpreadConfig::getSpreadType,
                configDTO.getSpreadType())
            .eq(
                ObjectUtils.isNotNull(configDTO.getUserType()),
                SpreadConfig::getUserType,
                configDTO.getUserType())
            .eq(
                ObjectUtils.isNotNull(configDTO.getStatus()),
                SpreadConfig::getStatus,
                configDTO.getStatus());
    // 判断排序
    if (StringUtils.isNotBlank(configDTO.getOrderByColumn())
        && StringUtils.isNotBlank(configDTO.getSortBy())) {
      queryWrapper.orderBy(
          Objects.equals(configDTO.getOrderByColumn(), "createTime"),
          ValidatorUtil.isAsc(configDTO.getSortBy()),
          SpreadConfig::getCreateTime);
      queryWrapper.orderBy(
          Objects.equals(configDTO.getOrderByColumn(), "visitCount"),
          ValidatorUtil.isAsc(configDTO.getSortBy()),
          SpreadConfig::getVisitCount);
      queryWrapper.orderBy(
          Objects.equals(configDTO.getOrderByColumn(), "registCount"),
          ValidatorUtil.isAsc(configDTO.getSortBy()),
          SpreadConfig::getRegistCount);
    }
    return queryWrapper.page(page).convert(configConvert::toVo);
  }

  /**
   * 新增推广信息
   *
   * @param configAddDTO 需求: 1、代理专属推广地址唯一 2、推广码唯一 3、公共推广地址下允许多个推广码 4、代理专属推广地址允许多个推广码
   */
  @Override
  public void insertSpreadConfig(SpreadConfigAddDTO configAddDTO) {
    SpreadConfig config = configConvert.toEntity(configAddDTO);
    if (StringUtils.isNotEmpty(config.getAgentAccount())) {
      int count =
          this.lambdaQuery()
              .ne(SpreadConfig::getAgentAccount, config.getAgentAccount())
              .eq(SpreadConfig::getExternalUrl, config.getExternalUrl())
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .or()
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .count();
      if (count > 0) {
        throw new ServiceException("推广地址或推广码已被使用！");
      }
    } else {
      int count =
          this.lambdaQuery()
              .eq(SpreadConfig::getExternalUrl, config.getExternalUrl())
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .or()
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .count();
      if (count > 0) {
        throw new ServiceException("推广地址或推广码已被使用！");
      }
    }
    config.setExclusiveFlag(SystemCodeType.NO.getCode());
    if (StringUtils.isNotBlank(config.getExternalUrl())
        && StringUtils.isNotBlank(config.getAgentAccount())) {
      // 设置专属
      config.setExclusiveFlag(SystemCodeType.YES.getCode());
    }
    if (configMapper.insert(config) == 0) {
      throw new ServiceException("创建失败");
    }
  }

  @Override
  public void updateSpreadConfig(SpreadConfigEditDTO configEditDTO) {
    SpreadConfig config = configConvert.toEntity(configEditDTO);
    if (StringUtils.isNotEmpty(config.getAgentAccount())) {
      int count =
          this.lambdaQuery()
              .ne(SpreadConfig::getAgentAccount, config.getAgentAccount())
              .eq(SpreadConfig::getExternalUrl, config.getExternalUrl())
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .or()
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .ne(SpreadConfig::getId, config.getId())
              .count();
      if (count > 0) {
        throw new ServiceException("推广地址或推广码已被使用！");
      }
    } else {
      int count =
          this.lambdaQuery()
              .ne(SpreadConfig::getId, config.getId())
              .eq(SpreadConfig::getExternalUrl, config.getExternalUrl())
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .or()
              .eq(ObjectUtils.isNotNull(config.getCode()), SpreadConfig::getCode, config.getCode())
              .ne(SpreadConfig::getId, config.getId())
              .count();
      if (count > 0) {
        throw new ServiceException("推广地址或推广码已被使用！");
      }
    }
    if (configMapper.updateById(config) == 0) {
      throw new ServiceException("修改失败");
    }
  }

  @Override
  public void deleteSpreadConfig(String id) {
    configMapper.deleteById(id);
  }

  @Override
  public void changeStatus(SpreadConfigEditDTO configEditDTO) {
    SpreadConfig config = configConvert.toEntity(configEditDTO);
    if (configMapper.updateById(config) == 0) {
      throw new ServiceException("修改失败");
    }
  }

  @Override
  public void changeReleaseTime(Long id) {
    SpreadConfig config = new SpreadConfig();
    config.setId(id);
    config.setCreateTime(new Date());
    if (configMapper.updateById(config) == 0) {
      throw new ServiceException("修改失败");
    }
  }

  @Override
  public void batchEnableStatus(String ids) {
    UpdateWrapper<SpreadConfig> wrapper = new UpdateWrapper<>();
    wrapper.in("id", Arrays.asList(StringUtils.split(ids, ",")));
    SpreadConfig config = new SpreadConfig();
    config.setStatus(SystemCodeType.ENABLE.getCode());
    configMapper.update(config, wrapper);
  }

  @Override
  public void batchDisableStatus(String ids) {
    UpdateWrapper<SpreadConfig> wrapper = new UpdateWrapper<>();
    wrapper.in("id", Arrays.asList(StringUtils.split(ids, ",")));
    SpreadConfig config = new SpreadConfig();
    config.setStatus(SystemCodeType.DISABLE.getCode());
    configMapper.update(config, wrapper);
  }

  @Override
  public void batchDeleteSpreadConfig(String ids) {
    if (configMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids, ","))) == 0) {
      throw new ServiceException("删除失败");
    }
  }
}
