package com.gameplat.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.gameplat.admin.service.AsyncSaveFileRecordService;
import com.gameplat.admin.service.OssService;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.security.SecurityUserHolder;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class OssServiceImpl implements OssService {

  @Autowired private FileStorageStrategyContext fileStorageStrategyContext;

  @Autowired private AsyncSaveFileRecordService asyncSaveFileRecordService;

  @Resource
  private FileConfig config;

  @Override
  @SneakyThrows
  public String upload(MultipartFile file) {
    String randomFilename = this.getRandomFilename(file);
    fileStorageStrategyContext
            .getProvider(config)
            .upload(file.getInputStream(), file.getContentType(), randomFilename);


    String accessUrl = this.getAccessUrl(config, randomFilename);
    // 异步保存文件记录
    asyncSaveFileRecordService.asyncSave(
            file, accessUrl, config.getProvider(), SecurityUserHolder.getUsername());
    return accessUrl;
  }

  private String getAccessUrl(FileConfig config, String filename) {
    if (StringUtils.isNotEmpty(config.getAccessDomain())) {
      return config.getAccessDomain().concat("/").concat(config.getBucket()).concat("/").concat(filename);
    } else {
      return config.getEndpoint().concat("/").concat(config.getBucket()).concat("/").concat(filename);
    }
  }

  @Override
  public boolean remove(String filePath) {
    return fileStorageStrategyContext.getProvider(config).delete(filePath);
  }

  @Override
  @SneakyThrows
  public String upload(MultipartFile file, String uploadBy) {
    String randomFilename = this.getRandomFilename(file);
    fileStorageStrategyContext
            .getProvider(config)
            .upload(file.getInputStream(), file.getContentType(), randomFilename);


    String accessUrl = this.getAccessUrl(config, randomFilename);
    // 异步保存文件记录
    asyncSaveFileRecordService.asyncSave(file, accessUrl, config.getProvider(), uploadBy);
    return accessUrl;
  }

  private String getRandomFilename(MultipartFile file) {
    return UUID.fastUUID() + "." + FileUtil.getSuffix(file.getOriginalFilename());
  }

  @Override
  @SneakyThrows
  public String upload(MultipartFile file, String uploadBy) {
    String randomFilename = this.getRandomFilename(file);
    fileStorageStrategyContext
            .getProvider(config)
            .upload(file.getInputStream(), file.getContentType(), randomFilename);

    String accessUrl = this.getAccessUrl(config, randomFilename);
    // 异步保存文件记录
    asyncSaveFileRecordService.asyncSave(file, accessUrl, config.getProvider(), uploadBy);
    return accessUrl;
  }
}
