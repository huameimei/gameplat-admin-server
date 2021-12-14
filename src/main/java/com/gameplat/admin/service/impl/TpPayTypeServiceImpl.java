package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.TpPayTypeConvert;
import com.gameplat.admin.mapper.TpPayTypeMapper;
import com.gameplat.admin.model.domain.TpPayType;
import com.gameplat.admin.model.vo.TpPayTypeVO;
import com.gameplat.admin.service.TpPayTypeService;
import com.gameplat.base.common.exception.ServiceException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TpPayTypeServiceImpl extends ServiceImpl<TpPayTypeMapper, TpPayType>
    implements TpPayTypeService {

  @Autowired private TpPayTypeConvert tpPayTypeConvert;

  @Autowired private TpPayTypeMapper tpPayTypeMapper;

  @Override
  public List<TpPayTypeVO> queryTpPayTypes(String interfaceCode) {
    LambdaQueryWrapper<TpPayType> query = Wrappers.lambdaQuery();
    query.eq(TpPayType::getTpInterfaceCode, interfaceCode);
    return this.list(query).stream()
        .map(e -> tpPayTypeConvert.toVo(e))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteBatchIds(List<Long> ids) {
    if (0 == tpPayTypeMapper.deleteBatchIds(ids)) {
      throw new ServiceException("通道删除失败!");
    }
  }
}
