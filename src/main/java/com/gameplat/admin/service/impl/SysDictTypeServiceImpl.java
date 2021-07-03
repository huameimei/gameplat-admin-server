package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysDictTypeConvert;
import com.gameplat.admin.dao.SysDictTypeMapper;
import com.gameplat.admin.model.dto.SysDictTypeAddDto;
import com.gameplat.admin.model.dto.SysDictTypeEditDto;
import com.gameplat.admin.model.dto.SysDictTypeQueryDto;
import com.gameplat.admin.model.entity.SysDictType;
import com.gameplat.admin.model.vo.SysDictTypeVo;
import com.gameplat.admin.service.SysDictTypeService;
import com.gameplat.common.exception.ServiceException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

  @Autowired
  private SysDictTypeMapper sysDictTypeMapper;

  @Autowired
  private SysDictTypeConvert sysDictTypeConvert;


  @Override
  public void delete(Long id) {
    sysDictTypeMapper.deleteById(id);
  }


  @Override
  public void save(SysDictTypeAddDto sysDictTypeAddDto) throws ServiceException {
    LambdaQueryWrapper<SysDictType> query = Wrappers.lambdaQuery();
    query.eq(SysDictType::getDictType, sysDictTypeAddDto.getDictType());
    if (this.count(query) > 0) {
      throw new ServiceException("字典标签，请勿重复添加");
    }
//    sysAuthIpAddDto.setCreateBy(SecurityUtil.getUserName());
    sysDictTypeAddDto.setCreateTime(new Date());
//    sysAuthIpAddDto.setUpdateBy(SecurityUtil.getUserName());
    sysDictTypeAddDto.setUpdateTime(new Date());
    if (!this.save(sysDictTypeConvert.toEntity(sysDictTypeAddDto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public IPage<SysDictTypeVo> queryPage(IPage<SysDictType> page, SysDictTypeQueryDto queryDto) {
    LambdaQueryWrapper<SysDictType> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotBlank(queryDto.getDictName())){
      query.like(SysDictType::getDictName,queryDto.getDictName());
    }
    return this.page(page, query).convert(sysDictTypeConvert::toVo);
  }

  @Override
  public void update(SysDictTypeEditDto sysDictTypeEditDto) throws ServiceException {
    sysDictTypeEditDto.setUpdateTime(new Date());
    if (!this.updateById(sysDictTypeConvert.toEntity(sysDictTypeEditDto))) {
      throw new ServiceException("更新失败!");
    }
  }
}
