package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.TpMerchantConvert;
import com.gameplat.admin.mapper.TpMerchantMapper;
import com.gameplat.admin.model.domain.TpMerchant;
import com.gameplat.admin.model.dto.TpMerchantAddDTO;
import com.gameplat.admin.model.dto.TpMerchantEditDTO;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.model.vo.TpMerchantPayTypeVO;
import com.gameplat.admin.model.vo.TpMerchantVO;
import com.gameplat.admin.service.TpInterfaceService;
import com.gameplat.admin.service.TpMerchantService;
import com.gameplat.admin.service.TpPayTypeService;
import com.gameplat.admin.util.EncryptUtils;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TpMerchantServiceImpl extends ServiceImpl<TpMerchantMapper, TpMerchant>
    implements TpMerchantService {

  @Autowired
  private TpMerchantConvert tpMerchantConvert;

  @Autowired
  private TpMerchantMapper tpMerchantMapper;

  @Autowired
  private TpPayTypeService tpPayTypeService;

  @Autowired
  private TpInterfaceService tpInterfaceService;

  @Override
  public void update(TpMerchantEditDTO dto) {
    TpMerchant tpMerchant = this.getById(dto.getId());
    TpInterfaceVO tpInterfaceVO =
        tpInterfaceService.queryTpInterface(tpMerchant.getTpInterfaceCode());
    Map<String, String> oriMerchantParameters = JSONObject
        .parseObject(tpMerchant.getParameters(), Map.class);
    List<String> tpInterfaceParameters =
        JSON.parseArray(tpInterfaceVO.getParameters(), String.class);
    Map<String, String> parametersMap = JSONObject.parseObject(dto.getParameters(), Map.class);
    // 如果加密字段没变，过滤字段不加密,如果加密字段在原基础上修改，抛异常重新处理
    if (CollectionUtils.isNotEmpty(tpInterfaceParameters)) {
      tpInterfaceParameters.forEach(
          paramJsonStr -> {
            Map<String, Object> config = JSONObject.parseObject(paramJsonStr, Map.class);
            if (config != null) {
              String key = String.valueOf(config.get("name"));
              String result = oriMerchantParameters.get(key);
              if (!"0".equals(config.get("encrypted"))) {
                result = EncryptUtils.summaryEncrypt(result);
              }
              if (!StringUtils.equals(result, parametersMap.get(key))) {
                if (parametersMap.get(key).contains("******")) {
                  throw new ServiceException(
                      "编辑后的" + config.get("label") + "存在******，请重新编辑" + config.get("label"));
                }
                oriMerchantParameters.put(key, parametersMap.get(key));
              }
            }
          });
    }
    dto.setParameters(JsonUtils.toJson(oriMerchantParameters));
    if (!this.updateById(tpMerchantConvert.toEntity(dto))) {
      throw new ServiceException("商户信息更新失败!");
    }
  }

  @Override
  public void save(TpMerchantAddDTO dto) {
    if (!this.save(tpMerchantConvert.toEntity(dto))) {
      throw new ServiceException("接口添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    this.removeById(id);
//    tpPayChannelService.deleteByMerchantId(id);
  }

  @Override
  public TpMerchantPayTypeVO getTpMerchantById(Long id) {
    TpMerchantPayTypeVO tpMerchantPayTypeVO = tpMerchantConvert.toPayTypeVo(this.getById(id));
    if (null == tpMerchantPayTypeVO) {
      throw new ServiceException("商户不存在!");
    }
    tpMerchantPayTypeVO.setTpPayTypeVOList(
        tpPayTypeService.queryTpPayTypes(tpMerchantPayTypeVO.getTpInterfaceCode()));
    TpInterfaceVO tpInterfaceVO =
        tpInterfaceService.queryTpInterface(tpMerchantPayTypeVO.getTpInterfaceCode());
    tpMerchantPayTypeVO.setTpInterfaceVO(tpInterfaceVO);
    Map<String, String> merchantParameters = JSONObject
        .parseObject(tpMerchantPayTypeVO.getParameters(), Map.class);
    List<String> tpInterfaceParameters =
        JSON.parseArray(tpInterfaceVO.getParameters(), String.class);
    // 商户号秘钥等信息加密展示处理
    if (CollectionUtils.isNotEmpty(tpInterfaceParameters)) {
      tpInterfaceParameters.forEach(
          jsonStrParam -> {
            Map<String, Object> config = JSONObject.parseObject(jsonStrParam, Map.class);
            if (config != null && !"0".equals(String.valueOf(config.get("encrypted")))) {
              String key = String.valueOf(config.get("name"));
              String result = EncryptUtils.summaryEncrypt(merchantParameters.get(key));
              merchantParameters.put(key, result);
            }
          });
    }
    tpMerchantPayTypeVO.setParameters(JsonUtils.toJson(merchantParameters));
    return tpMerchantPayTypeVO;
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<TpMerchant> update = Wrappers.lambdaUpdate();
    update.set(TpMerchant::getStatus, status).eq(TpMerchant::getId, id);
    this.update(update);
  }

  @Override
  public IPage<TpMerchantVO> queryPage(Page<TpMerchant> page, Integer status, String name) {
    return tpMerchantMapper.findTpMerchantPage(page, status, name);
  }

  @Override
  public List<TpMerchantVO> queryAllMerchant(Integer status) {
    LambdaQueryWrapper<TpMerchant> query = Wrappers.lambdaQuery();
    query.eq(TpMerchant::getStatus, status);
    return this.list(query).stream()
        .map(e -> tpMerchantConvert.toVo(e))
        .collect(Collectors.toList());
  }

}
