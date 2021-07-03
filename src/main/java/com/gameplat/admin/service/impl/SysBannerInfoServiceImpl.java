package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysBannerInfoConvert;
import com.gameplat.admin.dao.SysBannerInfoMapper;
import com.gameplat.admin.model.dto.SysBannerInfoAddDto;
import com.gameplat.admin.model.dto.SysBannerInfoEditDto;
import com.gameplat.admin.model.dto.SysBannerInfoQueryDto;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVo;
import com.gameplat.admin.service.SysBannerInfoService;
import com.gameplat.common.exception.ServiceException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysBannerInfoServiceImpl extends
    ServiceImpl<SysBannerInfoMapper,SysBannerInfo> implements
    SysBannerInfoService {

  @Autowired
  private SysBannerInfoMapper sysBannerInfoMapper;

  @Autowired
  private SysBannerInfoConvert sysBannerInfoConvert;


  @Override
  public IPage<SysBannerInfoVo> queryPage(IPage<SysBannerInfo> page,
      SysBannerInfoQueryDto queryDto) {
    LambdaQueryWrapper<SysBannerInfo> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotBlank(queryDto.getLanguage())){
      query.like(SysBannerInfo::getLanguage,queryDto.getLanguage());
    }
    return this.page(page, query).convert(sysBannerInfoConvert::toVo);
  }

  @Override
  public void save(SysBannerInfoAddDto sysBannerInfoAddDto) throws ServiceException {
    sysBannerInfoAddDto.setCreateTime(new Date());
    sysBannerInfoAddDto.setUpdateTime(new Date());
    if (!this.save(sysBannerInfoConvert.toEntity(sysBannerInfoAddDto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    sysBannerInfoMapper.deleteById(id);
  }

  @Override
  public void update(SysBannerInfoEditDto sysBannerInfoEditDto) throws ServiceException {
    sysBannerInfoEditDto.setUpdateTime(new Date());
    if (!this.updateById(sysBannerInfoConvert.toEntity(sysBannerInfoEditDto))) {
      throw new ServiceException("更新失败!");
    }
  }
}
