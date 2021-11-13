package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.DiscountType;
import com.gameplat.admin.model.dto.DiscountTypeAddDTO;
import com.gameplat.admin.model.dto.DiscountTypeEditDTO;

public interface DiscountTypeService extends IService<DiscountType> {

  void update(DiscountTypeEditDTO dto);

  void updateStatus(Long id, Integer status);

  void save(DiscountTypeAddDTO dto);

  void delete(Long id);

  IPage<DiscountType> findDiscountTypePage(Page<DiscountType> page);

  DiscountType getByValue(Integer value);
}
