package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysSMS;
import com.gameplat.admin.model.dto.OperSmsDTO;
import com.gameplat.admin.model.dto.SmsDTO;
import com.gameplat.admin.model.vo.SMSVO;

/**
 * 短信记录 业务层
 * @author Lenovo
 */
public interface SysSmsService extends IService<SysSMS> {

    IPage<SMSVO> selectSmsList(IPage<SysSMS> page, SmsDTO smsDTO);

    void insertSms(OperSmsDTO smsDTO);

    void updateSms(OperSmsDTO smsDTO);

    void cleanSms();
}
