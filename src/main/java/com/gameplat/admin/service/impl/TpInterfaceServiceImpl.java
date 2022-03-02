package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.TpInterfaceConvert;
import com.gameplat.admin.mapper.TpInterfaceMapper;
import com.gameplat.admin.mapper.TpPayTypeMapper;
import com.gameplat.admin.model.vo.TpInterfacePayTypeVo;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.service.TpInterfaceService;
import com.gameplat.admin.service.TpPayTypeService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.pay.TpInterface;
import com.gameplat.model.entity.pay.TpPayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TpInterfaceServiceImpl extends ServiceImpl<TpInterfaceMapper, TpInterface>
    implements TpInterfaceService {

  @Autowired private TpInterfaceConvert tpInterfaceConvert;

  @Autowired private TpInterfaceMapper tpInterfaceMapper;

  @Autowired private TpPayTypeMapper tpPayTypeMapper;

  @Autowired private TpPayTypeService tpPayTypeService;

  @Override
  public List<TpInterfaceVO> queryAll() {
    return this.list().stream().map(tpInterfaceConvert::toVo).collect(Collectors.toList());
  }

  @Override
  public TpInterfaceVO queryTpInterface(String interfaceCode) {
    LambdaQueryWrapper<TpInterface> query = Wrappers.lambdaQuery();
    query.eq(TpInterface::getCode, interfaceCode);
    return tpInterfaceConvert.toVo(this.getOne(query));
  }

  @Override
  public TpInterfacePayTypeVo queryTpInterfacePayType(Long id) {
    TpInterfacePayTypeVo tpInterfacePayTypeVo =
        tpInterfaceConvert.toTpInterfacePayTypeVo(tpInterfaceMapper.selectById(id));
    if (null == tpInterfacePayTypeVo) {
      throw new ServiceException("第三方接口不存在!");
    }
    LambdaQueryWrapper<TpPayType> query = Wrappers.lambdaQuery();
    query.eq(TpPayType::getTpInterfaceCode, tpInterfacePayTypeVo.getCode());
    tpInterfacePayTypeVo.setTpPayTypeList(tpPayTypeMapper.selectList(query));
    return tpInterfacePayTypeVo;
  }

  @Override
  public void update(TpInterface tpInterface) {
    if (!this.updateById(tpInterface)) {
      throw new ServiceException("接口更新失败!");
    }
    LambdaQueryWrapper<TpPayType> query = Wrappers.lambdaQuery();
    query.eq(TpPayType::getTpInterfaceCode, tpInterface.getCode());
    List<TpPayType> tpPayTypeList = tpPayTypeService.list(query);
    // B-A差值
    List<TpPayType> resultList =
        tpPayTypeList.stream()
            .filter(
                a ->
                    !tpInterface.getTpPayTypeList().stream()
                        .map(TpPayType::getId)
                        .collect(Collectors.toList())
                        .contains(a.getId()))
            .collect(Collectors.toList());
    tpPayTypeService.saveOrUpdateBatch(tpInterface.getTpPayTypeList());
    if (null != resultList && resultList.size() > 0) {
      tpPayTypeService.deleteBatchIds(
          resultList.stream().map(TpPayType::getId).collect(Collectors.toList()));
    }
  }

  @Override
  public void add(TpInterface tpInterface) {
    LambdaQueryWrapper<TpInterface> query = Wrappers.lambdaQuery();
    query.eq(TpInterface::getCode, tpInterface.getCode());
    if (this.count(query) > 0) {
      throw new ServiceException("第三方接口已存在");
    }
    if (!this.save(tpInterface)) {
      throw new ServiceException("接口添加失败!");
    }
    tpPayTypeService.saveBatch(tpInterface.getTpPayTypeList());
  }

  @Override
  public void synchronization(TpInterface tpInterface) {
    LambdaQueryWrapper<TpInterface> query = Wrappers.lambdaQuery();
    query.eq(TpInterface::getCode, tpInterface.getCode());
    tpInterfaceMapper.delete(query);
    LambdaQueryWrapper<TpPayType> tpPayType = Wrappers.lambdaQuery();
    tpPayType.eq(TpPayType::getTpInterfaceCode, tpInterface.getCode());
    tpPayTypeService.remove(tpPayType);
    tpInterfaceMapper.insert(tpInterface);
    tpPayTypeService.saveBatch(tpInterface.getTpPayTypeList());
  }
}
