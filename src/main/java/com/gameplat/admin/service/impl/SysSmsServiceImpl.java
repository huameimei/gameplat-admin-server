package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SmsConvert;
import com.gameplat.admin.mapper.SysSmsMapper;
import com.gameplat.admin.model.domain.SysSMS;
import com.gameplat.admin.model.dto.OperSmsDTO;
import com.gameplat.admin.model.dto.SmsDTO;
import com.gameplat.admin.model.vo.SMSVO;
import com.gameplat.admin.service.SysSmsService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 短信记录 服务实现层
 *
 * @author three
 */
@Service
@RequiredArgsConstructor
public class SysSmsServiceImpl extends ServiceImpl<SysSmsMapper, SysSMS> implements SysSmsService {

  @Autowired private SysSmsMapper smsMapper;

  @Autowired private SmsConvert smsConvert;

  @Override
  @SentinelResource(value = "selectSmsList")
  public IPage<SMSVO> selectSmsList(IPage<SysSMS> page, SmsDTO smsDTO) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(smsDTO.getPhone()), SysSMS::getPhone, smsDTO.getPhone())
        .eq(ObjectUtils.isNotNull(smsDTO.getStatus()), SysSMS::getStatus, smsDTO.getStatus())
        .eq(ObjectUtils.isNotNull(smsDTO.getSmsType()), SysSMS::getSmsType, smsDTO.getSmsType())
        .eq(
            ObjectUtils.isNotEmpty(smsDTO.getValidCode()),
            SysSMS::getValidCode,
            smsDTO.getValidCode())
        .between(
            ObjectUtils.isNotEmpty(smsDTO.getBeginTime())
                && ObjectUtils.isNotEmpty(smsDTO.getEndTime()),
            SysSMS::getCreateTime,
            smsDTO.getBeginTime(),
            smsDTO.getEndTime())
        .orderByDesc(SysSMS::getCreateTime)
        .page(page)
        .convert(smsConvert::toVo);
  }

  @Override
  @SentinelResource(value = "insertSms")
  public void insertSms(OperSmsDTO smsDTO) {
    SysSMS sms = smsConvert.toEntity(smsDTO);
    sms.setCreateTime(new Date());
    if (!this.save(sms)) {
      throw new ServiceException("添加短信失败");
    }
  }

  @Override
  @SentinelResource(value = "updateSms")
  public void updateSms(OperSmsDTO smsDTO) {
    SysSMS sms = smsConvert.toEntity(smsDTO);
    sms.setUpdateTime(new Date());
    if (!this.updateById(sms)) {
      throw new ServiceException("修改短信失败");
    }
  }

  @Override
  @SentinelResource(value = "cleanSms")
  public void cleanSms() {
    smsMapper.clean();
  }
}
