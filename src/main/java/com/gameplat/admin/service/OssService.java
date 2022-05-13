package com.gameplat.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

  /**
   * 上传单个文件
   *
   * @param file MultipartFile
   * @return String
   */
  String upload(MultipartFile file);

  /**
   * 删除文件
   *
   * @param filePath String
   * @return boolean
   */
  boolean remove(String filePath);
}
