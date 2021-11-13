package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.DictDataConvert;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.model.vo.MemberWithdrawDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.base.UserCredential;
import com.gameplat.common.enums.SystemCodeType;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.util.StringUtils;
import com.gameplat.common.web.Result;
import com.gameplat.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 字典数据 服务实现层
 *
 * @author three
 */
@Service
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData>
    implements SysDictDataService {

  private final DictDataConvert dictDataConvert;

  private final SysDictDataMapper dictDataMapper;

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
  public SysDictData getDictDataByType(String dictType) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, SystemCodeType.ENABLE.getCode())
        .eq(SysDictData::getDictType, dictType)
        .one();
  }

  @Override
  @SentinelResource(value = "getDictDataByTypes")
  public List<SysDictData> getDictDataByTypes(List<String> dictTypes) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, SystemCodeType.ENABLE.getCode())
        .in(SysDictData::getDictType, dictTypes)
        .list();
  }

  @Override
  @SentinelResource(value = "getDictData")
  public <T> T getDictData(DictTypeEnum dictType, Class<T> t) {
    SysDictData dictData = this.lambdaQuery().eq(SysDictData::getDictType, dictType.getKey()).one();
    if (StringUtils.isNotNull(dictData)) {
      return JSONUtil.toBean(dictData.getDictValue(), t);
    }
    return null;
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
        .eq(
            ObjectUtils.isNotNull(dictData.getStatus()),
            SysDictData::getStatus,
            dictData.getStatus())
        .list();
  }

  @Override
  @SentinelResource(value = "selectDictData")
  public DictDataVo selectDictData(SysDictDataDTO dictDataDTO) {
    SysDictData dictData =
        this.lambdaQuery()
            .eq(
                ObjectUtils.isNotEmpty(dictDataDTO.getDictName()),
                SysDictData::getDictName,
                dictDataDTO.getDictName())
            .eq(
                ObjectUtils.isNotEmpty(dictDataDTO.getDictLabel()),
                SysDictData::getDictLabel,
                dictDataDTO.getDictLabel())
            .eq(
                ObjectUtils.isNotEmpty(dictDataDTO.getDictType()),
                SysDictData::getDictType,
                dictDataDTO.getDictType())
            .one();

    return StringUtils.isNotNull(dictData) ? dictDataConvert.toVo(dictData) : null;
  }

  @Override
  @SentinelResource(value = "insertDictData")
  public void insertDictData(OperDictDataDTO operDictDataDTO) {
    int count =
        this.lambdaQuery().eq(SysDictData::getDictLabel, operDictDataDTO.getDictLabel()).count();
    if (count > 0) {
      throw new ServiceException("字典标签，请勿重复添加");
    }

    SysDictData dictData = dictDataConvert.toEntity(operDictDataDTO);
    if (!this.save(dictData)) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateDictData")
  public void updateDictData(OperDictDataDTO operDictDataDTO) {
    SysDictData dictData = dictDataConvert.toEntity(operDictDataDTO);
    if (!this.updateById(dictData)) {
      throw new ServiceException("修改失败!");
    }
  }

  @Override
  @SentinelResource(value = "deleteDictDataById")
  public void deleteDictDataById(String id) {
    if (!this.removeById(Long.parseLong(id))) {
      throw new ServiceException("删除失败!");
    }
  }

  @Override
  @SentinelResource(value = "deleteDictDataByIds")
  public void deleteDictDataByIds(List<Long> ids) {
    if (!this.removeByIds(ids)) {
      throw new ServiceException("删除失败!");
    }
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<SysDictData> update = Wrappers.lambdaUpdate();
    update.set(SysDictData::getStatus, status);
    update.eq(SysDictData::getId, id);
    this.update(update);
  }

  @Override
  @SentinelResource(value = "insertOrUpdate")
  public void insertOrUpdate(UserWithdrawLimitInfo userWithdrawLimitInfo) {
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query.eq(
        SysDictData::getDictLabel,
        DictTypeEnum.USER_WITHDRAW_LIMIT.getValue()
            + userWithdrawLimitInfo.getTimesForWithdrawal());
    if (this.count(query) > 0) {
      LambdaUpdateWrapper<SysDictData> update = Wrappers.lambdaUpdate();
      update.set(
          SysDictData::getDictLabel,
          DictTypeEnum.USER_WITHDRAW_LIMIT.getValue()
              + userWithdrawLimitInfo.getTimesForWithdrawal());
      update.set(SysDictData::getDictValue, JSONObject.toJSONString(userWithdrawLimitInfo));
      update.eq(
          SysDictData::getDictLabel,
          DictTypeEnum.USER_WITHDRAW_LIMIT.getValue()
              + userWithdrawLimitInfo.getTimesForWithdrawal());
      this.update(update);
    } else {
      OperDictDataDTO operDictDataDTO = new OperDictDataDTO();
      operDictDataDTO.setDictLabel(
          DictTypeEnum.USER_WITHDRAW_LIMIT.getValue()
              + userWithdrawLimitInfo.getTimesForWithdrawal());
      operDictDataDTO.setDictValue(JSONObject.toJSONString(userWithdrawLimitInfo));
      operDictDataDTO.setDictType(DictTypeEnum.USER_WITHDRAW_LIMIT.getKey());
      SysDictData dictData = dictDataConvert.toEntity(operDictDataDTO);
      this.save(dictData);
    }
  }

  @Override
  @SentinelResource(value = "deleteByDictLabel")
  public void deleteByDictLabel(Long timesForWithdrawal) {
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query.eq(
        SysDictData::getDictLabel,
        DictTypeEnum.USER_WITHDRAW_LIMIT.getValue() + timesForWithdrawal);
    query.eq(SysDictData::getDictType, DictTypeEnum.USER_WITHDRAW_LIMIT.getKey());
    if (!this.remove(query)) {
      throw new ServiceException("删除失败！");
    }
  }

  @Override
  public SysDictData getDictList(String dictType, String dictLabel) {
    return this.lambdaQuery()
        .eq(SysDictData::getStatus, SystemCodeType.ENABLE.getCode())
        .eq(SysDictData::getDictType, dictType)
        .eq(SysDictData::getDictLabel, dictLabel)
        .one();
  }

  @Override
  public List<SysDictData> getDictListAll(SysDictDataDTO dictData) {
    return this.getDictList(dictDataConvert.toEntity(dictData));
  }

  @Override
  public IPage<MemberWithdrawDictDataVo> queryWithdrawPage(Page<SysDictData> page) {
    return this.lambdaQuery()
        .eq(SysDictData::getDictType, DictTypeEnum.USER_WITHDRAW_LIMIT.getKey())
        .page(page)
        .convert(e -> JsonUtils.parse(e.getDictValue(), MemberWithdrawDictDataVo.class));
  }
}
