package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheInvalidateContainer;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DictDataConvert;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.model.vo.MemberWithdrawDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.DictTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据 服务实现层
 *
 * @author three
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData>
    implements SysDictDataService {

  @Autowired private DictDataConvert dictDataConvert;

  @Override
  @SentinelResource(value = "selectDictDataList")
  public IPage<DictDataVo> selectDictDataList(PageDTO<SysDictData> page, SysDictDataDTO dictData) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(dictData.getDictName()),
            SysDictData::getDictName,
            dictData.getDictName())
        .eq(
            ObjectUtils.isNotEmpty(dictData.getDictLabel()),
            SysDictData::getDictLabel,
            dictData.getDictLabel())
        .eq(
            ObjectUtils.isNotEmpty(dictData.getDictType()),
            SysDictData::getDictType,
            dictData.getDictType())
        .eq(
            ObjectUtils.isNotNull(dictData.getStatus()),
            SysDictData::getStatus,
            dictData.getStatus())
        .page(page)
        .convert(dictDataConvert::toVo);
  }

  @Override
  @SentinelResource(value = "getDictDataByType")
  @Cached(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType", expire = 3600)
  public List<SysDictData> getDictDataByType(String dictType) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, EnableEnum.ENABLED.code())
        .eq(SysDictData::getDictType, dictType)
        .list();
  }

  @Override
  @SentinelResource(value = "getDictDataByTypes")
  @Cached(name = CachedKeys.DICT_DATA_CACHE, key = "#dictTypes", expire = 3600)
  public List<SysDictData> getDictDataByTypes(List<String> dictTypes) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, EnableEnum.ENABLED.code())
        .in(SysDictData::getDictType, dictTypes)
        .list();
  }

  @Override
  @SentinelResource(value = "getDictData")
  public SysDictData getDictData(String dictType, String dictLabel) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, EnableEnum.ENABLED.code())
        .eq(SysDictData::getDictType, dictType)
        .eq(SysDictData::getDictLabel, dictLabel)
        .one();
  }

  @Override
  @SentinelResource(value = "getDictDataValue")
  @Cached(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType + ':' + #dictLabel", expire = 7200)
  public String getDictDataValue(String dictType, String dictLabel) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, EnableEnum.ENABLED.code())
        .eq(SysDictData::getDictType, dictType)
        .eq(SysDictData::getDictLabel, dictLabel)
        .oneOpt()
        .map(SysDictData::getDictValue)
        .orElse(null);
  }

  @Override
  @SentinelResource(value = "getDictList")
  public List<SysDictData> getDictList(SysDictData dictData) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(dictData.getDictName()),
            SysDictData::getDictName,
            dictData.getDictName())
        .eq(
            ObjectUtils.isNotEmpty(dictData.getDictLabel()),
            SysDictData::getDictLabel,
            dictData.getDictLabel())
        .eq(
            ObjectUtils.isNotEmpty(dictData.getDictType()),
            SysDictData::getDictType,
            dictData.getDictType())
        .eq(SysDictData::getStatus, 1)
        .list();
  }

  @Override
  @SentinelResource(value = "insertDictData")
  public void insertDictData(OperDictDataDTO dto) {
    if (this.lambdaQuery().eq(SysDictData::getDictLabel, dto.getDictLabel()).exists()) {
      throw new ServiceException("字典标签已存在，请勿重复添加");
    }

    if (!this.save(dictDataConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateDictData")
  @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dto.dictType + ':' + #dto.dictLabel")
  public void updateDictData(OperDictDataDTO dto) {
    SysDictData dictData = dictDataConvert.toEntity(dto);
    if (!this.updateById(dictData)) {
      throw new ServiceException("修改失败!");
    }
  }

  @Override
  @SentinelResource(value = "deleteDictDataByIds")
  @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#ids", multi = true)
  public void deleteDictDataByIds(List<Long> ids) {
    if (!this.removeByIds(ids)) {
      throw new ServiceException("删除失败!");
    }
  }

  @Override
  @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#id")
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    this.lambdaUpdate()
        .set(SysDictData::getStatus, status)
        .eq(SysDictData::getId, id)
        .update(new SysDictData());
  }

  @Override
  @SentinelResource(value = "addOrUpdateUserWithdrawLimit")
  @CacheInvalidateContainer(
      value = {
        @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType"),
        @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType + ':' + #dictLabel")
      })
  public void addOrUpdateUserWithdrawLimit(
      String dictType, String dictLabel, UserWithdrawLimitInfo limitInfo) {
    if (this.lambdaQuery()
        .eq(SysDictData::getDictType, dictType)
        .eq(SysDictData::getDictLabel, dictLabel)
        .exists()) {
      this.lambdaUpdate()
          .set(SysDictData::getDictValue, JsonUtils.toJson(limitInfo))
          .eq(SysDictData::getDictType, dictType)
          .eq(SysDictData::getDictLabel, dictLabel)
          .update();
    } else {
      OperDictDataDTO operDictDataDTO = new OperDictDataDTO();
      operDictDataDTO.setDictLabel(dictLabel);
      operDictDataDTO.setDictType(dictType);
      operDictDataDTO.setDictValue(JsonUtils.toJson(limitInfo));
      SysDictData dictData = dictDataConvert.toEntity(operDictDataDTO);
      this.save(dictData);
    }
  }

  @Override
  @SentinelResource(value = "deleteByDictLabel")
  @CacheInvalidateContainer(
      value = {
        @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType"),
        @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType + ':' + #dictLabel")
      })
  public void delete(String dictType, String dictLabel) {
    if (!this.lambdaUpdate()
        .eq(SysDictData::getDictType, dictType)
        .eq(SysDictData::getDictLabel, dictLabel)
        .remove()) {
      throw new ServiceException("删除失败！");
    }
  }

  @Override
  public List<SysDictData> getDictListAll(SysDictDataDTO dictData) {
    return this.getDictList(dictDataConvert.toEntity(dictData));
  }

  @Override
  public IPage<MemberWithdrawDictDataVo> queryWithdrawPage(Page<SysDictData> page) {
    return this.lambdaQuery()
        .eq(SysDictData::getDictType, DictTypeEnum.USER_WITHDRAW_LIMIT.getValue())
        .page(page)
        .convert(e -> JsonUtils.parse(e.getDictValue(), MemberWithdrawDictDataVo.class));
  }

  @Override
  @CacheInvalidate(
      name = CachedKeys.DICT_DATA_CACHE,
      key = "#entity.dictType + ':' + #entity.label")
  public boolean updateById(SysDictData entity) {
    return super.updateById(entity);
  }

  @Override
  @SentinelResource(value = "saveOrUpdate")
  @CacheInvalidate(
      name = CachedKeys.DICT_DATA_CACHE,
      key = "#entity.dictType + ':' + #entity.dictLabel")
  public boolean saveOrUpdate(SysDictData entity) {
    return super.saveOrUpdate(entity);
  }
}
