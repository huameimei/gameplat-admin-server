package com.gameplat.admin.service.impl;

import com.gameplat.admin.service.AsyncSaveFileRecordService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.OssService;
import com.gameplat.admin.service.SysDomainService;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.security.SecurityUserHolder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssServiceImpl implements OssService {

  @Autowired private ConfigService configService;

  @Autowired private FileStorageStrategyContext fileStorageStrategyContext;

  @Autowired private AsyncSaveFileRecordService asyncSaveFileRecordService;

  @Autowired private SysDomainService domainService;

  @Override
  @SneakyThrows
  public String upload(MultipartFile file) {
    FileConfig config = this.getConfig();

    String filename =
        fileStorageStrategyContext
            .getProvider(config)
            .upload(file.getInputStream(), file.getContentType());

    String accessUrl = domainService.getOssDomain().concat("/").concat(filename);

    // 异步保存文件记录
    asyncSaveFileRecordService.asyncSave(
        file, accessUrl, config.getProvider(), SecurityUserHolder.getUsername());

    return accessUrl;
  }

  @Override
  public boolean remove(String filePath) {
    return fileStorageStrategyContext.getProvider(this.getConfig()).delete(filePath);
  }

  private FileConfig getConfig() {
    return configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
  }
}
