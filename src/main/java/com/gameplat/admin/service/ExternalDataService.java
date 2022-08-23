package com.gameplat.admin.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 外部数据导入
 */
public interface ExternalDataService {
    void dealData(String username, MultipartFile file, HttpServletRequest request);

  void enPswd();
}
