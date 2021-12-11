package com.gameplat.admin.service.live.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.SpinnerDateModel;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Feign 表单提交配置，仅当使用表单提交时，引入此配置
 *
 * @author robben
 */
public class FeignFormConfig extends FeignRestConfig {

  @Autowired private ObjectFactory<HttpMessageConverters> messageConverters;

  @Bean
  public Encoder feignFormEncoder() {

    return new SpringFormEncoder(new SpringEncoder(messageConverters));
  }

  @Bean
  public Decoder feignFormDecocder(){
    MappingJackson2HttpMessageConverter  jackson  = new MappingJackson2HttpMessageConverter(new ObjectMapper());
    List<MediaType> unmodifiedMediaTypes = jackson.getSupportedMediaTypes();
    List<MediaType> mediaTypeList = new ArrayList<>(unmodifiedMediaTypes.size() +1);
    mediaTypeList.addAll(unmodifiedMediaTypes);
    mediaTypeList.add(MediaType.APPLICATION_OCTET_STREAM);
    jackson.setSupportedMediaTypes(mediaTypeList);
    ObjectFactory<HttpMessageConverters>  objectFactory = () -> new HttpMessageConverters(jackson);
    return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
  }
}
