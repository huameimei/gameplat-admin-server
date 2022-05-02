package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DiscountTypeConvert;
import com.gameplat.admin.mapper.DiscountTypeMapper;
import com.gameplat.admin.model.dto.DiscountTypeAddDTO;
import com.gameplat.admin.model.dto.DiscountTypeEditDTO;
import com.gameplat.admin.model.vo.DiscountTypeVO;
import com.gameplat.admin.service.DiscountTypeService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.DiscountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class DiscountTypeServiceImpl extends ServiceImpl<DiscountTypeMapper, DiscountType>
    implements DiscountTypeService {

  @Autowired private DiscountTypeConvert discountTypeConvert;

  @Override
  public void update(DiscountTypeEditDTO dto) {
    if (!this.updateById(discountTypeConvert.toEntity(dto))) {
      throw new ServiceException("更新失败!");
    }
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<DiscountType> update = Wrappers.lambdaUpdate();
    update
        .set(ObjectUtils.isNotEmpty(status), DiscountType::getStatus, status)
        .eq(DiscountType::getId, id);
    this.update(new DiscountType(), update);
  }

  @Override
  public void save(DiscountTypeAddDTO dto) {
    if (null != this.lambdaQuery().eq(DiscountType::getValue, dto.getValue()).one()) {
      throw new ServiceException("编码已存在!");
    }
    dto.setDateType(1);
    dto.setStatus(1);
    if (!this.save(discountTypeConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    this.removeById(id);
  }

  @Override
  public IPage<DiscountTypeVO> findDiscountTypePage(Page<DiscountType> page) {
    return this.lambdaQuery()
        .orderByAsc(DiscountType::getSort)
        .orderByDesc(DiscountType::getCreateTime)
        .page(page)
        .convert(discountTypeConvert::toVo);
  }

  @Override
  public DiscountType getByValue(Integer value) {
    return this.lambdaQuery().eq(DiscountType::getValue, value).one();
  }
}
