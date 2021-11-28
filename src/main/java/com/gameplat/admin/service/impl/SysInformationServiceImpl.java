package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysInformationConvert;
import com.gameplat.admin.mapper.SysInformationMapper;
import com.gameplat.admin.model.domain.SysInformation;
import com.gameplat.admin.model.dto.SysInformationAddDTO;
import com.gameplat.admin.service.SysInformationService;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lily
 * @description 系统用户个人消息
 * @date 2021/11/28
 */

@Service
@RequiredArgsConstructor
public class SysInformationServiceImpl extends ServiceImpl<SysInformationMapper, SysInformation> implements SysInformationService {

    @Autowired private SysInformationConvert informationConvert;

    @Override
    public void createSysInformation(SysInformationAddDTO dto) {
        SysInformation sysInformation = informationConvert.toEntity(dto);
        if (!this.save(sysInformation)) {
            throw new ServiceException("新增系统回复用户消息失败！");
        }
    }
}
