package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.constant.ConfigConstant;
import com.gameplat.admin.convert.SysFileConfigConvert;
import com.gameplat.admin.convert.SysSmsAreaConvert;
import com.gameplat.admin.convert.SysSmsConfigConvert;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysFileConfig;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.domain.SysSmsConfig;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.SysFileConfigVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.model.vo.SysSmsConfigVO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysSmsAreaService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.security.SecurityUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private SysSmsConfigConvert sysSmsConfigConvert;

  @Autowired private SysFileConfigConvert sysFileConfigConvert;

  @Autowired private SysSmsAreaService sysSmsAreaService;

  @Autowired private SysSmsAreaConvert sysSmsAreaConvert;

  @Override
  public List<SysSmsConfigVO> findSmsList() {
    List<SysSmsConfigVO> list = new ArrayList<>();
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query
        .eq(SysDictData::getDictType, ConfigConstant.SMS_CONFIG)
        .eq(SysDictData::getDictLabel, ConfigConstant.SMS);
    SysDictData sysDictData = sysDictDataService.getOne(query);
    if (sysDictData != null && StringUtils.isNotBlank(sysDictData.getDictValue())) {
      // 得到本地短信运营商
      list = JSONArray.parseArray(sysDictData.getDictValue(), SysSmsConfigVO.class);
    }
    return list;
  }

  @Override
  public List<SysFileConfigVO> findFileList() {
    List<SysFileConfigVO> list = new ArrayList<>();
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query
        .eq(SysDictData::getDictType, ConfigConstant.FILE_CONFIG)
        .eq(SysDictData::getDictLabel, ConfigConstant.FILE);
    SysDictData sysDictData = sysDictDataService.getOne(query);
    if (sysDictData != null && StringUtils.isNotBlank(sysDictData.getDictValue())) {
      // 得到本地短信运营商
      list = JSONArray.parseArray(sysDictData.getDictValue(), SysFileConfigVO.class);
    }
    return list;
  }

  @Override
  public void updateSmsConfig(SysSmsConfigDTO sysSmsConfigDTO) {
    // 查询短信配置
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query
        .eq(SysDictData::getDictType, ConfigConstant.SMS_CONFIG)
        .eq(SysDictData::getDictLabel, ConfigConstant.SMS);
    SysDictData sysDictData = sysDictDataService.getOne(query);
    List<SysSmsConfig> sysSmsConfigList = new ArrayList<>();

    SysSmsConfig sysSmsConfig = sysSmsConfigConvert.toEntity(sysSmsConfigDTO);
    // 如果查询短信配置为空，则新增一条短信记录
    if (sysDictData == null) {
      sysSmsConfigList.add(sysSmsConfig);
      sysDictData = new SysDictData();
      sysDictData.setDictType(ConfigConstant.SMS_CONFIG);
      sysDictData.setDictName(ConfigConstant.SMS);
      sysDictData.setDictValue(JSONUtil.toJsonStr(sysSmsConfigList));
      if (!sysDictDataService.save(sysDictData)) {
        throw new ServiceException("新增短信配置失败!");
      }
    } else {
      // 查询短信配置不为空，但值为空，加入[]
      if (StringUtils.isBlank(sysDictData.getDictValue())) {
        sysDictData.setDictValue("[]");
      }
      sysSmsConfigList = JSONArray.parseArray(sysDictData.getDictValue(), SysSmsConfig.class);
      if (!CollectionUtils.isEmpty(sysSmsConfigList)) {
        boolean flag = true;
        for (SysSmsConfig item : sysSmsConfigList) {
          if (sysSmsConfig.getOperator().equals(item.getOperator())) {
            flag = false;
            BeanUtils.copyBeanProp(item, sysSmsConfig);
          }
        }
        if (flag) {
          // TODO: 2021/11/3 默认状态设置为 正常可用
          sysSmsConfigList.add(sysSmsConfig);
        }
      } else {
        // 2.短信配置值为空,将传入值放入list
        sysSmsConfigList.add(sysSmsConfig);
      }
      // 将list数据set进短信值，修改短信配置
      sysDictData.setDictValue(JSON.toJSONString(sysSmsConfigList));
      sysDictData.setUpdateBy(SecurityUserHolder.getUsername());
      sysDictData.setUpdateTime(new Date());
      if (!sysDictDataService.updateById(sysDictData)) {
        throw new ServiceException("新增短信配置失败!");
      }
    }
  }

  @Override
  public void updateFileConfig(SysFileConfigDTO sysFileConfigDTO) {
    // 查询短信配置
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query
        .eq(SysDictData::getDictType, ConfigConstant.FILE_CONFIG)
        .eq(SysDictData::getDictLabel, ConfigConstant.FILE);
    SysDictData sysDictData = sysDictDataService.getOne(query);
    List<SysFileConfig> sysFileConfigList = new ArrayList<>();

    SysFileConfig sysFileConfig = sysFileConfigConvert.toEntity(sysFileConfigDTO);
    // 如果查询短信配置为空，则新增一条短信记录
    if (sysDictData == null) {
      sysFileConfigList.add(sysFileConfig);
      sysDictData = new SysDictData();
      sysDictData.setDictType(ConfigConstant.FILE_CONFIG);
      sysDictData.setDictName(ConfigConstant.FILE);
      sysDictData.setDictValue(JSONUtil.toJsonStr(sysFileConfigList));
      sysDictData.setCreateBy(SecurityUserHolder.getUsername());
      sysDictData.setCreateTime(new Date());
      if (!sysDictDataService.save(sysDictData)) {
        throw new ServiceException("新增文件配置失败!");
      }
    } else {
      // 查询短信配置不为空，但值为空，加入[]
      if (StringUtils.isBlank(sysDictData.getDictValue())) {
        sysDictData.setDictValue("[]");
      }
      sysFileConfigList = JSONArray.parseArray(sysDictData.getDictValue(), SysFileConfig.class);
      if (!CollectionUtils.isEmpty(sysFileConfigList)) {
        boolean flag = true;
        for (SysFileConfig item : sysFileConfigList) {
          if (sysFileConfig.getServiceProvider().equals(item.getServiceProvider())) {
            flag = false;
            BeanUtils.copyBeanProp(item, sysFileConfig);
          }
        }
        if (flag) {
          // TODO: 2021/11/3 默认状态设置为 正常可用
          sysFileConfigList.add(sysFileConfig);
        }
      } else {
        // 2.短信配置值为空,将传入值放入list
        sysFileConfigList.add(sysFileConfig);
      }
      // 将list数据set进短信值，修改短信配置
      sysDictData.setDictValue(JSON.toJSONString(sysFileConfigList));
      sysDictData.setUpdateBy(SecurityUserHolder.getUsername());
      sysDictData.setUpdateTime(new Date());
      if (!sysDictDataService.updateById(sysDictData)) {
        throw new ServiceException("更新文件配置失败!");
      }
    }
  }

  @Override
  public void configDataEdit(OperSystemConfigDTO dto) {
    JSONObject json = dto.getJsonData();
    // json遍历
    for (Map.Entry<String, Object> entry : json.entrySet()) {
      SysDictData sysDictData = new SysDictData();
      sysDictData.setDictType(dto.getDictType());
      sysDictData.setDictLabel(entry.getKey());
      sysDictData.setDictValue(entry.getValue().toString());
      sysDictData.setUpdateBy(SecurityUserHolder.getUsername());
      sysDictData.setUpdateTime(new Date());

      LambdaQueryWrapper<SysDictData> queryWrapper = Wrappers.lambdaQuery();
      queryWrapper
          .eq(SysDictData::getDictType, dto.getDictType())
          .eq(SysDictData::getDictLabel, entry.getKey());
      if (!sysDictDataService.update(sysDictData, queryWrapper)) {
        throw new ServiceException("更新配置失败!");
      }
    }
  }

  @Override
  public IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SysSmsAreaQueryDTO dto) {
    LambdaQueryWrapper<SysSmsArea> query = Wrappers.lambdaQuery();
    query
        .eq(ObjectUtils.isNotEmpty(dto.getCode()), SysSmsArea::getCode, dto.getCode())
        .eq(ObjectUtils.isNotEmpty(dto.getName()), SysSmsArea::getName, dto.getName());
    return sysSmsAreaService.page(page, query).convert(sysSmsAreaConvert::toVo);
  }

  @Override
  public void smsAreaEdit(OperSysSmsAreaDTO dto) {
    SysSmsArea sysSmsArea = sysSmsAreaConvert.toEntity(dto);
    if (sysSmsArea.getId() != null && sysSmsArea.getId() > 0) {
      sysSmsArea.setUpdateBy(SecurityUserHolder.getUsername());
      sysSmsArea.setUpdateTime(new Date());
      if (!sysSmsAreaService.updateById(sysSmsArea)) {
        throw new ServiceException("更新区号配置失败!");
      }
    }
    sysSmsArea.setCreateBy(SecurityUserHolder.getUsername());
    sysSmsArea.setCreateTime(new Date());
    sysSmsArea.setStatus("1");
    if (!sysSmsAreaService.save(sysSmsArea)) {
      throw new ServiceException("新增区号配置失败");
    }
  }

  @Override
  public void smsAreaDelete(Long id) {
    if (!sysSmsAreaService.removeById(id)) {
      throw new ServiceException("删除区号配置失败!");
    }
  }

  @Override
  public SysDictData findActivityTypeCodeList(String language) {
    LambdaQueryWrapper<SysDictData> query = Wrappers.lambdaQuery();
    query
            .eq(SysDictData::getDictType, ConfigConstant.ACTIVITY_TYPE_CONFIG)
            .eq(SysDictData::getDictLabel, ConfigConstant.ACTIVITY_TYPE_CONFIG);
    return sysDictDataService.getOne(query);
  }
}
