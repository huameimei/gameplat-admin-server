package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.TenantFloatTypeMapper;
import com.gameplat.admin.model.vo.TenantFloatTypeVo;
import com.gameplat.admin.service.TenantFloatTypeService;
import com.gameplat.model.entity.setting.TenantFloatSetting;
import com.gameplat.model.entity.setting.TenantFloatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TenantFloatTypeServiceImpl extends ServiceImpl<TenantFloatTypeMapper, TenantFloatType>
    implements TenantFloatTypeService {

  @Autowired private TenantFloatTypeMapper tenantFloatTypeMapper;

  /**
   * 查询游戏浮窗类型列表
   *
   * @param tenantFloatTypeVo
   *            游戏浮窗类型
   * @return 游戏浮窗类型
   */
  @Override
  public List<TenantFloatTypeVo> selectSysFloatTypeList(TenantFloatTypeVo tenantFloatTypeVo) {
    List<TenantFloatTypeVo> result = tenantFloatTypeMapper.selectSysFloatTypeList(tenantFloatTypeVo);
    if(!CollectionUtils.isEmpty(result)){
      for (TenantFloatTypeVo type : result) {
        if(null != type && !StringUtils.isEmpty(type.getShowPosition())){
          JSONArray typeArray = JSONArray.parseArray(type.getShowPosition());
          List<String> showPositionList = JSONObject.parseArray(typeArray.toJSONString(), String.class);
          type.setShowPositionList(showPositionList);
        }
      }
    }
    return result;
  }

  /**
   * 新增游戏浮窗
   *
   * @param tenantFloatSetting
   *            游戏浮窗类型
   * @return 结果
   */
  @Override
  public void insertSysFloat(TenantFloatSetting tenantFloatSetting) {
    tenantFloatTypeMapper.insertSysFloatSetting(tenantFloatSetting);
  }

  /**
   * 删除游戏浮窗类型信息
   *
   * @param id
   *            游戏浮窗类型ID
   * @return 结果
   */
  @Override
  public void deleteSysFloatById(Integer id) {
    this.removeById(id);
  }

  /**
   * 修改游戏浮窗类型
   *
   * @param tenantFloatSetting
   *            游戏浮窗类型
   * @return 结果
   */
  @Override
  public void updateFloat(TenantFloatSetting tenantFloatSetting) {
    tenantFloatTypeMapper.updateSysFloat(tenantFloatSetting);
  }

  @Override
  public void updateBatch(List<TenantFloatSetting> tenantFloatSettings) {
    tenantFloatTypeMapper.updateBatch(tenantFloatSettings);
  }

  @Override
  public void updateFloatType(TenantFloatType tenantFloatType) {
    this.updateById(tenantFloatType);
  }

  @Override
  public void updateShowPosition(String showPositions) {
    tenantFloatTypeMapper.updateShowPosition(showPositions);
  }
}
