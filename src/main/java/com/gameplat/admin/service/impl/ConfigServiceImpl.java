package com.gameplat.admin.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.common.enums.DefaultEnums;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigServiceImpl implements ConfigService {

  @Autowired private SysDictDataService dictDataService;

  @Override
  public Integer getValueInteger(DictDataEnum dataEnum) {
    return this.getValueInteger(dataEnum, null);
  }

  @Override
  public Integer getValueInteger(DictDataEnum dataEnum, Integer defaultValue) {
    return this.getDictData(dataEnum).map(Integer::new).orElse(defaultValue);
  }

  @Override
  public String getValue(DictDataEnum dataEnum) {
    return this.getValue(dataEnum, null);
  }

  @Override
  public String getValue(DictDataEnum dataEnum, String defaultValue) {
    return this.getDictData(dataEnum).orElse(defaultValue);
  }

  @Override
  public <T> T get(DictDataEnum dataEnum, Class<T> clazz) {
    return this.getDictData(dataEnum)
        .map(c -> JsonUtils.parse(c, clazz))
        .orElseThrow(() -> new ServiceException("配置信息不存在!"));
  }

  @Override
  public <T> T get(DictDataEnum dataEnum, TypeReference<T> reference) {
    return this.getDictData(dataEnum)
        .map(c -> JsonUtils.parse(c, reference))
        .orElseThrow(() -> new ServiceException("配置信息不存在!"));
  }

  @Override
  public Long getValueLong(DictDataEnum dataEnum) {
    return this.getValueLong(dataEnum, null);
  }

  @Override
  public Long getValueLong(DictDataEnum dataEnum, Long defaultValue) {
    return this.getDictData(dataEnum).map(Long::new).orElse(defaultValue);
  }

  @Override
  public Double getValueDouble(DictDataEnum dataEnum, Double defaultValue) {
    return this.getDictData(dataEnum).map(Double::new).orElse(defaultValue);
  }

  @Override
  public Double getValueDouble(DictDataEnum dataEnum) {
    return this.getValueDouble(dataEnum, null);
  }

  @Override
  public Boolean getValueBoolean(DictDataEnum dataEnum, Boolean defaultValue) {
    return this.getDictData(dataEnum).map(Boolean::new).orElse(defaultValue);
  }

  @Override
  public Boolean getValueBoolean(DictDataEnum dataEnum) {
    return this.getValueBoolean(dataEnum, Boolean.FALSE);
  }

  @Override
  public <T> T getDefaultConfig(DictTypeEnum type, Class<T> clazz) {
    return dictDataService.getDictDataByType(type.getValue()).stream()
        .filter(e -> DefaultEnums.YES.match(e.getIsDefault()))
        .map(e -> JsonUtils.parse(e.getDictValue(), clazz))
        .findAny()
        .orElseThrow(() -> new ServiceException("配置信息不存在或未指定默认配置!"));
  }

  private Optional<String> getDictData(DictDataEnum dataEnum) {
    return Optional.ofNullable(
        dictDataService.getDictDataValue(dataEnum.getType().getValue(), dataEnum.getLabel()));
  }
}
