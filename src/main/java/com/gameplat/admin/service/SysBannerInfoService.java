package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysBannerInfoAddDto;
import com.gameplat.admin.model.dto.SysBannerInfoEditDto;
import com.gameplat.admin.model.dto.SysBannerInfoQueryDto;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVo;

public interface SysBannerInfoService extends IService<SysBannerInfo> {

  IPage<SysBannerInfoVo> queryPage(IPage<SysBannerInfo> page, SysBannerInfoQueryDto queryDto);

  void save(SysBannerInfoAddDto sysBannerInfoAddDto);

  void delete(Long id);

  void update(SysBannerInfoEditDto sysBannerInfoEditDto);
}
