package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysPayBankConvert;
import com.gameplat.admin.dao.SysPayBankMapper;
import com.gameplat.admin.model.dto.SysPayBankAddDTO;
import com.gameplat.admin.model.dto.SysPayBankEditDTO;
import com.gameplat.admin.model.entity.SysPayBank;
import com.gameplat.admin.service.SysPayBankService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.security.util.SecurityUtil;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysPayBankServiceImpl extends ServiceImpl<SysPayBankMapper, SysPayBank>
    implements SysPayBankService {

  @Autowired private SysPayBankConvert sysPayBankConvert;

  @Autowired private SysPayBankMapper sysPayBankMapper;

  @Override
  public List<SysPayBank> queryList() {
    return this.list();
  }

  @Override
  public void save(SysPayBankAddDTO dto) throws ServiceException {
    if (this.checkRow(dto)) {
      throw new ServiceException("银行编码已存在!");
    }
    if (!this.save(sysPayBankConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void update(SysPayBankEditDTO dto) {
    if (!this.updateById(sysPayBankConvert.toEntity(dto))) {
      throw new ServiceException("更新失败!");
    }
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<SysPayBank> update = Wrappers.lambdaUpdate();
    update.set(SysPayBank::getStatus, status);
    update.eq(SysPayBank::getId, id);
    this.update(update);
  }

  @Override
  public void delete(Long id) {
    this.getById(id).deleteById();
  }

  /** 检查记录是否存在 */
  private boolean checkRow(SysPayBankAddDTO dto) {
    boolean result = false;
    LambdaQueryWrapper<SysPayBank> query = Wrappers.lambdaQuery();
    query.eq(SysPayBank::getBankCode, dto.getBankCode());
    List<SysPayBank> list = sysPayBankMapper.selectList(query);
    if (list != null && list.size() != 0) {
      result = true;
    } else {
      result = false;
    }
    return result;
  }
}
