package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.EmailConvert;
import com.gameplat.admin.mapper.SysEmailMapper;
import com.gameplat.admin.model.domain.SysEmail;
import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.dto.OperEmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.base.common.exception.ServiceException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件记录 服务实现层
 *
 * @author three
 */
@Service
@RequiredArgsConstructor
public class SysEmailServiceImpl extends ServiceImpl<SysEmailMapper, SysEmail>
    implements SysEmailService {

  @Autowired private SysEmailMapper emailMapper;

  @Autowired private EmailConvert emailConvert;

  @Override
  @SentinelResource(value = "selectEmailList")
  public IPage<EmailVO> selectEmailList(IPage<SysEmail> page, EmailDTO emailDTO) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(emailDTO.getTitle()), SysEmail::getTitle, emailDTO.getTitle())
        .eq(ObjectUtils.isNotNull(emailDTO.getStatus()), SysEmail::getStatus, emailDTO.getStatus())
        .eq(
            ObjectUtils.isNotNull(emailDTO.getEmailType()),
            SysEmail::getEmailType,
            emailDTO.getEmailType())
        .between(
            ObjectUtils.isNotEmpty(emailDTO.getBeginTime()),
            SysEmail::getCreateTime,
            emailDTO.getBeginTime(),
            emailDTO.getEndTime())
        .orderByDesc(SysEmail::getCreateTime)
        .page(page)
        .convert(emailConvert::toVo);
  }

  @Override
  @SentinelResource(value = "insertEmail")
  public void insertEmail(OperEmailDTO emailDTO) {
    SysEmail email = emailConvert.toEntity(emailDTO);
    email.setCreateTime(new Date());
    if (!this.save(email)) {
      throw new ServiceException("添加邮件失败");
    }
  }

  @Override
  @SentinelResource(value = "updateEmail")
  public void updateEmail(OperEmailDTO emailDTO) {
    SysEmail email = emailConvert.toEntity(emailDTO);
    email.setUpdateTime(new Date());
    if (!this.updateById(email)) {
      throw new ServiceException("修改邮件失败");
    }
  }

  @Override
  @SentinelResource(value = "cleanEmail")
  public void cleanEmail() {
    emailMapper.clean();
  }
}
