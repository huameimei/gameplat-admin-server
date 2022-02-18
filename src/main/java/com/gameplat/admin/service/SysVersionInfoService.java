package com.gameplat.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.domain.SysVersionInfo;
import com.gameplat.admin.model.dto.SysVersionInfoDTO;

/**
 * 系统版本 业务层
 * @author three
 */
public interface SysVersionInfoService {


    /**
     * 获取
     */
    IPage<SysVersionInfo> getSysPackageInfo(IPage<SysVersionInfo> page , SysVersionInfoDTO sysPackageInfoDTO);

    /**
     * 新增发版信息
     */
    boolean createSysPackageInfo(SysVersionInfoDTO dto);

    /**
     * 编辑发版信息
     */
    int editSysPackageInfo(SysVersionInfoDTO dto);

    /**
     * 删除发版信息
     */
    boolean removeSysPackageInfo(Integer id);

}
