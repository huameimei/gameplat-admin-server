package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.dto.SysBannerInfoQueryDTO;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVO;

public interface SysBannerInfoService extends IService<SysBannerInfo> {

  IPage<SysBannerInfoVO> queryPage(IPage<SysBannerInfo> page, SysBannerInfoQueryDTO queryDto);

  void save(SysBannerInfoAddDTO sysBannerInfoAddDto);

  void delete(Long id);

  void update(SysBannerInfoEditDTO sysBannerInfoEditDto);
}
