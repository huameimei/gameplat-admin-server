package com.gameplat.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.common.enums.DictDataEnum;

public interface ConfigService {

  String getValue(DictDataEnum dataEnum);

  String getValue(DictDataEnum dataEnum, String defaultValue);

  Integer getValueInteger(DictDataEnum dataEnum);

  Integer getValueInteger(DictDataEnum dataEnum, Integer defaultValue);

  Long getValueLong(DictDataEnum dataEnum);

  Long getValueLong(DictDataEnum dataEnum, Long defaultValue);

  Double getValueDouble(DictDataEnum dataEnum, Double defaultValue);

  Double getValueDouble(DictDataEnum dataEnum);

  Boolean getValueBoolean(DictDataEnum dataEnum, Boolean defaultValue);

  Boolean getValueBoolean(DictDataEnum dataEnum);

  <T> T get(DictDataEnum dataEnum, Class<T> clazz);

  <T> T get(DictDataEnum dataEnum, TypeReference<T> clazz);
}
