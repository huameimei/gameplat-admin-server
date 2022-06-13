package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysFileManagerMapper;
import com.gameplat.admin.service.AsyncSaveFileRecordService;
import com.gameplat.common.compent.oss.FileStorageEnum;
import com.gameplat.common.util.FileUtils;
import com.gameplat.model.entity.sys.SysFileManager;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author aBen
 * @date 2022/4/15 0:23
 * @desc
 */
@Service
public class AsyncSaveFileRecordServiceImpl
    extends ServiceImpl<SysFileManagerMapper, SysFileManager>
    implements AsyncSaveFileRecordService {

  private static final int SUCCEED = 1;

  @Override
  @Async
  public void asyncSave(
      MultipartFile file, String fileUrl, Integer serviceProvider, String uploadBy) {
    // 获取文件类型
    String fileType =
        Objects.requireNonNull(file.getOriginalFilename())
            .substring(file.getOriginalFilename().lastIndexOf("."));
    SysFileManager sysFileManager = new SysFileManager();
    sysFileManager.setServiceProvider(serviceProvider);
    sysFileManager.setProviderName(FileStorageEnum.valueOf(serviceProvider).getDesc());
    sysFileManager.setOldFileName(file.getOriginalFilename());
    sysFileManager.setStoreFileName(FilenameUtils.getName(fileUrl));
    sysFileManager.setFileUrl(fileUrl);
    sysFileManager.setFileType(FileUtils.getFileType(fileType));
    sysFileManager.setFileSize(FileUtils.getSize(file.getSize()));
    sysFileManager.setStatus(SUCCEED);
    sysFileManager.setCreateBy(uploadBy);
    this.save(sysFileManager);
  }
}
