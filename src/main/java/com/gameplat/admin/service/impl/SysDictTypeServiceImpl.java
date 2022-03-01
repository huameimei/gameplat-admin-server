package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DictTypeConvert;
import com.gameplat.admin.mapper.SysDictTypeMapper;
import com.gameplat.admin.model.dto.OperDictTypeDTO;
import com.gameplat.admin.model.vo.DictTypeVO;
import com.gameplat.admin.service.SysDictTypeService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.model.entity.sys.SysDictType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型 业务层处理
 *
 * @author three
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType>
    implements SysDictTypeService {

  @Autowired private DictTypeConvert dictTypeConvert;

  @Override
  @SentinelResource(value = "selectDictTypeList", fallback = "sentineFallBack")
  public List<DictTypeVO> selectDictTypeList(OperDictTypeDTO typeDTO) {
    LambdaQueryWrapper<SysDictType> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper
        .like(
            ObjectUtils.isNotEmpty(typeDTO.getDictType()),
            SysDictType::getDictType,
            typeDTO.getDictType())
        .like(
            ObjectUtils.isNotEmpty(typeDTO.getDictName()),
            SysDictType::getDictName,
            typeDTO.getDictName())
        .eq(ObjectUtils.isNotNull(typeDTO.getStatus()), SysDictType::getStatus, typeDTO.getStatus())
        .orderByAsc(SysDictType::getOrderNum);

    return this.list(queryWrapper).stream().map(dictTypeConvert::toVo).collect(Collectors.toList());
  }

  @Override
  @SentinelResource(value = "addDictType", fallback = "sentineFallBack")
  public void addDictType(OperDictTypeDTO dto) {
    SysDictType dictType = dictTypeConvert.toEntity(dto);
    if (this.lambdaQuery().eq(SysDictType::getDictType, dto.getDictType()).exists()) {
      throw new ServiceException("字典类型已存在!");
    }

    dictType.setDictId(IdGeneratorSnowflake.getInstance().nextId());
    if (!this.save(dictType)) {
      throw new ServiceException("添加字典类型失败!");
    }
  }

  @Override
  @SentinelResource(value = "editDictType", fallback = "sentineFallBack")
  public void editDictType(OperDictTypeDTO dto) {
    SysDictType dictType = dictTypeConvert.toEntity(dto);
    if (!this.updateById(dictType)) {
      throw new ServiceException("修改字典类型失败!");
    }
  }

  @Override
  @SentinelResource(value = "removeDictTypeByIds", fallback = "sentineFallBack")
  public void removeDictTypeById(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("批量删除字典类型失败!");
    }
  }
}
