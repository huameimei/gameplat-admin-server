package com.gameplat.admin.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author aBen
 * @date 2022/4/15 0:23
 * @desc
 */
public interface AsyncSaveFileRecordService {

  void asyncSave(MultipartFile file, String fileUrl, Integer serviceProvider, String uploadBy);
}
