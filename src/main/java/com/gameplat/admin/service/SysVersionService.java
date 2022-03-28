package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.VersionDTO;
import com.gameplat.model.entity.sys.SysVersion;

/**
 * 系统版本 业务层
 *
 * @author three
 */
public interface SysVersionService extends IService<SysVersion> {

  /**
   * 获取系统版本列表
   *
   * @return
   */
  IPage<SysVersion> selectVersionList(IPage<SysVersion> page, VersionDTO versionDTO);
}
