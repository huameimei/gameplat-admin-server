package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PpInterfaceConvert;
import com.gameplat.admin.mapper.PpInterfaceMapper;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.model.entity.pay.PpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class PpInterfaceServiceImpl extends ServiceImpl<PpInterfaceMapper, PpInterface>
    implements PpInterfaceService {

  @Autowired private PpInterfaceConvert ppInterfaceConvert;

  @Override
  public List<PpInterfaceVO> queryAll() {
    return this.list().stream().map(ppInterfaceConvert::toVo).collect(Collectors.toList());
  }

  @Override
  public PpInterfaceVO queryPpInterface(String interfaceCode) {
    LambdaQueryWrapper<PpInterface> query = Wrappers.lambdaQuery();
    query.eq(PpInterface::getCode, interfaceCode);
    return ppInterfaceConvert.toVo(this.getOne(query));
  }

  @Override
  public PpInterface get(String interfaceCode) {
    LambdaQueryWrapper<PpInterface> query = Wrappers.lambdaQuery();
    query.eq(PpInterface::getCode, interfaceCode);
    return this.getOne(query);
  }

  @Override
  public void synchronization(PpInterface ppInterface) {
    LambdaQueryWrapper<PpInterface> query = Wrappers.lambdaQuery();
    query.eq(PpInterface::getCode, ppInterface.getCode());
    this.remove(query);
    this.save(ppInterface);
  }
}
