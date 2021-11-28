package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysInformation;
import com.gameplat.admin.model.dto.SysInformationAddDTO;

/**
 * @author lily
 * @description 系统用户个人消息表
 * @date 2021/11/28
 */

public interface SysInformationService extends IService<SysInformation> {
    /**
     * 新增系统回复用户消息
     */
    void createSysInformation(SysInformationAddDTO dto);
}
