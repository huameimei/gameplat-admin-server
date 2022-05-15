package com.gameplat.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.gameplat.admin.service.AsyncSaveFileRecordService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.OssService;
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

  @Override
  @SneakyThrows
  public String upload(MultipartFile file) {
    FileConfig config = this.getConfig();
    String randomFilename = this.getRandomFilename(file);
    String url =
        fileStorageStrategyContext
            .getProvider(config)
            .upload(file.getInputStream(), file.getContentType(), randomFilename);

    // 异步保存文件记录
    asyncSaveFileRecordService.asyncSave(
        file, url, config.getProvider(), SecurityUserHolder.getUsername());

    return this.getAccessUrl(config, randomFilename);
  }

  private String getAccessUrl(FileConfig config, String filename) {
    return config.getEndpoint().concat("/").concat(config.getBucket()).concat("/").concat(filename);
  }

  @Override
  public boolean remove(String filePath) {
    return fileStorageStrategyContext.getProvider(this.getConfig()).delete(filePath);
  }

  private FileConfig getConfig() {
    return configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
  }

  private String getRandomFilename(MultipartFile file) {
    return UUID.fastUUID() + "." + FileUtil.getSuffix(file.getOriginalFilename());
  }
}
