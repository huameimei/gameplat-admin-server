package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheInvalidateContainer;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DictDataConvert;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.dto.DictParamDTO;
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
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字典数据 服务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
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
        .orderByAsc(SysDictData::getDictSort)
        .orderByDesc(SysDictData::getCreateTime)
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
  public String getDirectChargeValue(String dictType, String dictLabel) {
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
        .eq(SysDictData::getStatus, EnableEnum.ENABLED.code())
        .list();
  }

  @Override
  @SentinelResource(value = "insertDictData")
  @CacheInvalidateContainer({
    @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dto.dictType"),
    @CacheInvalidate(
        name = CachedKeys.DICT_DATA_CACHE,
        key = "#dto.dictType + ':' + #dto.dictLabel")
  })
  public void insertDictData(OperDictDataDTO dto) {
    if (this.lambdaQuery()
        .eq(ObjectUtils.isNotNull(dto.getDictType()), SysDictData::getDictType, dto.getDictType())
        .eq(SysDictData::getDictLabel, dto.getDictLabel())
        .exists()) {
      throw new ServiceException("字典标签已存在，请勿重复添加");
    }

    if (!this.save(dictDataConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  @SentinelResource(value = "insertBank")
  public void insertBank(OperDictDataDTO dto) {
    if (this.lambdaQuery().eq(SysDictData::getDictValue, dto.getDictValue()).exists()) {
      throw new ServiceException("银行标识已存在，请勿重复添加");
    }

    if (!this.save(dictDataConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateDictData")
  @CacheInvalidateContainer({
    @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dto.dictType"),
    @CacheInvalidate(
        name = CachedKeys.DICT_DATA_CACHE,
        key = "#dto.dictType + ':' + #dto.dictLabel")
  })
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
  @CacheInvalidateContainer({
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
  @CacheInvalidateContainer(
      value = {@CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dictParamDTO.dictType")})
  public void batchUpdateDictData(DictParamDTO dictParamDTO) {
    List<SysDictData> list = new ArrayList<>();

    // json遍历
    for (Map.Entry<String, Object> entry : dictParamDTO.getJsonData().entrySet()) {
      SysDictData sysDictData = new SysDictData();
      sysDictData.setDictType(dictParamDTO.getDictType());
      sysDictData.setDictLabel(entry.getKey());
      sysDictData.setDictValue(entry.getValue().toString());
      list.add(sysDictData);
    }
    for (SysDictData sysDictData : list) {
      if (this.lambdaQuery()
          .eq(SysDictData::getDictType, sysDictData.getDictType())
          .eq(SysDictData::getDictLabel, sysDictData.getDictLabel())
          .exists()) {
        this.lambdaUpdate()
            .set(SysDictData::getDictValue, sysDictData.getDictValue())
            .eq(SysDictData::getDictType, sysDictData.getDictType())
            .eq(SysDictData::getDictLabel, sysDictData.getDictLabel())
            .update();
      } else {
        OperDictDataDTO operDictDataDTO = new OperDictDataDTO();
        operDictDataDTO.setDictLabel(sysDictData.getDictLabel());
        operDictDataDTO.setDictType(sysDictData.getDictType());
        operDictDataDTO.setDictValue(sysDictData.getDictValue());
        SysDictData dictData = dictDataConvert.toEntity(operDictDataDTO);
        this.save(dictData);
      }
    }
  }

  @Override
  @SentinelResource(value = "deleteByDictLabel")
  @CacheInvalidateContainer({
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
  @CacheInvalidateContainer({
    @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#entity.dictType"),
    @CacheInvalidate(
        name = CachedKeys.DICT_DATA_CACHE,
        key = "#entity.dictType + ':' + #entity.dictLabel")
  })
  public boolean updateById(SysDictData entity) {
    return super.updateById(entity);
  }

  @Override
  @SentinelResource(value = "saveOrUpdate")
  @CacheInvalidateContainer({
    @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#entity.dictType"),
    @CacheInvalidate(
        name = CachedKeys.DICT_DATA_CACHE,
        key = "#entity.dictType + ':' + #entity.dictLabel")
  })
  public boolean saveOrUpdate(SysDictData entity) {
    return super.saveOrUpdate(entity);
  }

  @Override
  @SentinelResource(value = "saveOrUpdate")
  @CacheInvalidateContainer({
    @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#entity.dictType"),
    @CacheInvalidate(
        name = CachedKeys.DICT_DATA_CACHE,
        key = "#entity.dictType + ':' + #entity.dictLabel")
  })
  public boolean saveOrUpdate(SysDictData entity, Wrapper<SysDictData> updateWrapper) {
    return super.saveOrUpdate(entity, updateWrapper);
  }

  @Override
  @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#data.dictType")
  @CacheInvalidate(
      name = CachedKeys.DICT_DATA_CACHE,
      key = "#data.dictType + ':' + #data.dictLabel")
  public void updateByTypeAndLabel(SysDictData data) {
    this.lambdaUpdate()
        .eq(SysDictData::getDictLabel, data.getDictLabel())
        .eq(SysDictData::getDictType, data.getDictType())
        .set(SysDictData::getDictValue, data.getDictValue())
        .update();
  }

  @Override
  public JSONObject getData(String dictType) {
    List<SysDictData> queryResult =
        this.lambdaQuery()
            .eq(SysDictData::getDictType, dictType)
            .eq(SysDictData::getStatus, 1)
            .list();
    JSONObject json = new JSONObject();
    JSONArray objects = JSONUtil.parseArray(queryResult);
    for (Object o : objects) {
      String str = JSON.toJSONString(o);
      JSONObject parse = JSONObject.parseObject(str);
      String key = parse.getString("dictLabel");
      String value = parse.getString("dictValue");
      json.put(key, value);
    }
    return json;
  }
}
