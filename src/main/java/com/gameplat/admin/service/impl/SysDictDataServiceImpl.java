package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysDictDataConvert;
import com.gameplat.admin.dao.SysDictDataMapper;
import com.gameplat.admin.model.dto.SysDictDataAddDto;
import com.gameplat.admin.model.dto.SysDictDataEditDto;
import com.gameplat.admin.model.dto.SysDictDataQueryDto;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.exception.ServiceException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper,SysDictData> implements SysDictDataService {

  @Autowired
  private SysDictDataConvert sysDictDataConvert;

  @Autowired
  private SysDictDataMapper sysDictDataMapper;

  @Override
  public List<SysDictData> selectDictDataListByType(String dictType) {
    return this.lambdaQuery().eq(SysDictData::getDictType,dictType).list();
  }

  @Override
  public <T> T getDictData(String dictType, Class<T> t) {
    return (T) JSONUtil.toBean(JSONUtil.toJsonStr(this.selectDictDataListByType(dictType).stream().collect(
        Collectors.toMap(SysDictData::getDictLabel,SysDictData::getDictValue))), t);
  }

  @Override
  public SysDictData getDictValue(String dictType, String dictLabel) {
    return this.lambdaQuery().eq(SysDictData::getDictType,dictType)
        .eq(SysDictData::getDictLabel,dictLabel).one();
  }

  @Override
  public IPage<SysDictDataVo> queryPage(IPage<SysDictData> page, SysDictDataQueryDto queryDto) {
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotBlank(queryDto.getDictLabel())){
      query.like(SysDictData::getDictLabel,queryDto.getDictLabel());
    }
    if (queryDto.getStatus() != null){
      query.eq(SysDictData::getStatus,queryDto.getStatus());
    }
    return this.page(page, query).convert(sysDictDataConvert::toVo);
  }

  @Override
  public void save(SysDictDataAddDto sysDictDataAddDto) {
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query.eq(SysDictData::getDictLabel, sysDictDataAddDto.getDictLabel());
    if (this.count(query) > 0) {
      throw new ServiceException("字典标签，请勿重复添加");
    }
//    sysAuthIpAddDto.setCreateBy(SecurityUtil.getUserName());
    sysDictDataAddDto.setCreateTime(new Date());
//    sysAuthIpAddDto.setUpdateBy(SecurityUtil.getUserName());
    sysDictDataAddDto.setUpdateTime(new Date());
    if (!this.save(sysDictDataConvert.toEntity(sysDictDataAddDto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    sysDictDataMapper.deleteById(id);
  }

  @Override
  public void update(SysDictDataEditDto sysDictDataEditDto) {
    sysDictDataEditDto.setUpdateTime(new Date());
    if (!this.updateById(sysDictDataConvert.toEntity(sysDictDataEditDto))) {
      throw new ServiceException("更新失败!");
    }
  }
}
