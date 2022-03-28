package com.gameplat.admin.service.impl;

import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.OssService;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.enums.DictTypeEnum;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssServiceImpl implements OssService {

  @Autowired private ConfigService configService;

  @Autowired private FileStorageStrategyContext fileStorageStrategyContext;

  @Override
  public String upload(MultipartFile file) {
    return this.upload(file, file.getOriginalFilename());
  }

  @Override
  @SneakyThrows
  public String upload(MultipartFile file, String filename) {
    return fileStorageStrategyContext
        .getProvider(this.getConfig())
        .upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
  }

  @Override
  public boolean remove(String filePath) {
    return fileStorageStrategyContext.getProvider(this.getConfig()).delete(filePath);
  }

  private FileConfig getConfig() {
    return configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
  }
}