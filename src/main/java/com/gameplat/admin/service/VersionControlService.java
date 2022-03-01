package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.model.entity.VersionControl;

/**
 * 系统版本 业务层
 *
 * @author three
 */
public interface VersionControlService {

  /** 获取 */
  IPage<VersionControl> getSysPackageInfo(
      IPage<VersionControl> page, VersionControlDTO sysPackageInfoDTO);

  /** 新增发版信息 */
  boolean createSysPackageInfo(VersionControlDTO dto);

  /** 编辑发版信息 */
  int editSysPackageInfo(VersionControlDTO dto);

  /** 删除发版信息 */
  boolean removeSysPackageInfo(Integer id);
}
