package com.gameplat.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;

public interface ConfigService {

  String getValue(DictDataEnum dataEnum);

  String getValue(DictDataEnum dataEnum, String defaultValue);

  Integer getInteger(DictDataEnum dataEnum);

  Integer getInteger(DictDataEnum dataEnum, Integer defaultValue);

  Long getLong(DictDataEnum dataEnum);

  Long getLong(DictDataEnum dataEnum, Long defaultValue);

  Double getDouble(DictDataEnum dataEnum, Double defaultValue);

  Double getDouble(DictDataEnum dataEnum);

  Boolean getBoolean(DictDataEnum dataEnum, Boolean defaultValue);

  Boolean getBoolean(DictDataEnum dataEnum);

  <T> T get(DictDataEnum dataEnum, Class<T> clazz);

  <T> T get(DictTypeEnum type, Class<T> clazz);

  <T> T get(String type, String label, Class<T> clazz);

  <T> T get(DictDataEnum dataEnum, TypeReference<T> clazz);

  <T> T getDefaultConfig(DictTypeEnum type, Class<T> clazz);
}
