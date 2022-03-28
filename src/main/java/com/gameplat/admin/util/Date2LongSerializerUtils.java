package com.gameplat.admin.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 时间戳转换工具类
 *
 * @author lily
 * @date 2021/11/21
 */
public class Date2LongSerializerUtils extends JsonSerializer<Date> {
  @Override
  public void serialize(
      Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeNumber(date.getTime() / 1000);
  }
}
