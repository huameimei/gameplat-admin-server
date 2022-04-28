package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysFloatTypeMapper;
import com.gameplat.admin.model.vo.SysFloatTypeVo;
import com.gameplat.admin.service.SysFloatTypeService;
import com.gameplat.model.entity.setting.SysFloatSetting;
import com.gameplat.model.entity.setting.SysFloatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysFloatTypeServiceImpl extends ServiceImpl<SysFloatTypeMapper, SysFloatType>
    implements SysFloatTypeService {

  @Autowired private SysFloatTypeMapper sysFloatTypeMapper;

  @Override
  public List<SysFloatTypeVo> selectSysFloatTypeList(SysFloatTypeVo sysFloatTypeVo) {
    List<SysFloatTypeVo> result =
        sysFloatTypeMapper.selectSysFloatTypeList(sysFloatTypeVo);
    if (!CollectionUtils.isEmpty(result)) {
      for (SysFloatTypeVo type : result) {
        if (null != type && !StringUtils.isEmpty(type.getShowPosition())) {
          JSONArray typeArray = JSONArray.parseArray(type.getShowPosition());
          List<String> showPositionList =
              JSONObject.parseArray(typeArray.toJSONString(), String.class);
          type.setShowPositionList(showPositionList);
        }
      }
    }
    return result;
  }

  @Override
  public void insertSysFloat(SysFloatSetting sysFloatSetting) {
    sysFloatTypeMapper.insertSysFloatSetting(sysFloatSetting);
  }

  @Override
  public void updateFloat(SysFloatSetting sysFloatSetting) {
    sysFloatTypeMapper.updateSysFloat(sysFloatSetting);
  }

  @Override
  public void updateBatch(List<SysFloatSetting> sysFloatSettings) {
    sysFloatTypeMapper.updateBatch(sysFloatSettings);
  }

  @Override
  public void updateFloatType(SysFloatType sysFloatType) {
    this.updateById(sysFloatType);
  }

  @Override
  public void updateShowPosition(String showPositions) {
    sysFloatTypeMapper.updateShowPosition(showPositions);
  }
}
