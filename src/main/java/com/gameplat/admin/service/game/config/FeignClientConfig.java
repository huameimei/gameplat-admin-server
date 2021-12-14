package com.gameplat.admin.service.game.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.gameplat.base.common.web.Result;
import feign.Util;
import feign.codec.Decoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FeignClientConfig extends FeignRestConfig {

  private final ObjectMapper mapper = new ObjectMapper();

  @Bean
  public Decoder decoder() {
    return (response, type) -> {
      String payload = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
      log.info("Feign自定义解码器：{}", payload);

      TypeFactory typeFactory = mapper.getTypeFactory();
      JavaType parameterType =
          typeFactory.constructParametricType(Result.class, mapper.constructType(type));

      Result<?> result = mapper.readValue(payload, parameterType);
      return Optional.ofNullable(result)
          .filter(Result::isSucceed)
          .map(Result::getData)
          .orElse(null);
    };
  }
}
