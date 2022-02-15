package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PpMerchantConvert;
import com.gameplat.admin.mapper.PpMerchantMapper;
import com.gameplat.admin.model.bean.ProxyPayMerBean;
import com.gameplat.admin.model.domain.PpMerchant;
import com.gameplat.admin.model.dto.PpMerchantAddDTO;
import com.gameplat.admin.model.dto.PpMerchantEditDTO;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.admin.service.PpMerchantService;
import com.gameplat.admin.util.EncryptUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class PpMerchantServiceImpl extends ServiceImpl<PpMerchantMapper, PpMerchant>
    implements PpMerchantService {

  @Autowired
  private PpMerchantConvert ppMerchantConvert;

  @Autowired
  private PpMerchantMapper ppMerchantMapper;

  @Autowired
  private PpInterfaceService ppInterfaceService;

  @Override
  public void update(PpMerchantEditDTO dto) {
    PpMerchant ppMerchant = this.getById(dto.getId());
    PpInterfaceVO ppInterfaceVO = ppInterfaceService.queryPpInterface(dto.getPpInterfaceCode());
    if(null == ppInterfaceVO){
      throw new ServiceException("代付接口不存在或已被删除，请删除商户重新配置");
    }
    Map<String, String> oriMerchantParameters = JSONObject
        .parseObject(ppMerchant.getParameters(), Map.class);
    List<String> ppInterfaceParameters =
        JSON.parseArray(ppInterfaceVO.getParameters(), String.class);
    Map<String, String> parametersMap = JSONObject.parseObject(dto.getParameters(), Map.class);
    // 如果加密字段没变，过滤字段不加密,如果加密字段在原基础上修改，抛异常重新处理
    if (CollectionUtils.isNotEmpty(ppInterfaceParameters)) {
      ppInterfaceParameters.forEach(
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
    this.conver2PpMerchant(dto);
    if (!this.updateById(ppMerchantConvert.toEntity(dto))) {
      throw new ServiceException("商户信息更新失败!");
    }
  }

  @Override
  public void save(PpMerchantAddDTO dto) {
    this.conver2PpMerchant(dto);
    if (!this.save(ppMerchantConvert.toEntity(dto))) {
      throw new ServiceException("接口添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    this.removeById(id);
  }

  @Override
  public PpMerchantVO getPpMerchantById(Long id) {
    PpMerchantVO ppMerchantVO = ppMerchantConvert.toVo(this.getById(id));
    // 设置限制信息
    this.conver2MerVo(ppMerchantVO);
    if (null == ppMerchantVO) {
      throw new ServiceException("商户不存在!");
    }
    PpInterfaceVO ppInterfaceVO =
        ppInterfaceService.queryPpInterface(ppMerchantVO.getPpInterfaceCode());
    ppMerchantVO.setPpInterfaceVO(ppInterfaceVO);
    if(null == ppInterfaceVO){
      throw new ServiceException("代付接口不存在或已被删除，请删除商户重新配置");
    }
    Map<String, String> merchantParameters = JSONObject
        .parseObject(ppMerchantVO.getParameters(), Map.class);
    List<String> ppInterfaceParameters =
        JSON.parseArray(ppInterfaceVO.getParameters(), String.class);
    // 商户号秘钥等信息加密展示处理
    if (CollectionUtils.isNotEmpty(ppInterfaceParameters)) {
      ppInterfaceParameters.forEach(
          jsonStrParam -> {
            Map<String, Object> config = JSONObject.parseObject(jsonStrParam, Map.class);
            if (config != null && !"0".equals(String.valueOf(config.get("encrypted")))) {
              String key = String.valueOf(config.get("name"));
              String result = EncryptUtils.summaryEncrypt(merchantParameters.get(key));
              merchantParameters.put(key, result);
            }
          });
    }
    ppMerchantVO.setParameters(JsonUtils.toJson(merchantParameters));
    return ppMerchantVO;
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<PpMerchant> update = Wrappers.lambdaUpdate();
    update.set(PpMerchant::getStatus, status).eq(PpMerchant::getId, id);
    this.update(new PpMerchant(),update);
  }

  @Override
  public IPage<PpMerchantVO> queryPage(Page<PpMerchant> page, Integer status, String name) {
    return ppMerchantMapper.findPpMerchantPage(page, status, name);
  }

  @Override
  public List<PpMerchant> queryAllMerchant(Integer status) {
    LambdaQueryWrapper<PpMerchant> query = Wrappers.lambdaQuery();
    query.eq(PpMerchant::getStatus, status);
    return this.list(query);
  }

  private PpMerchantAddDTO conver2PpMerchant(PpMerchantAddDTO ppMerchantAddDTO) {
    ProxyPayMerBean merBean =
        new ProxyPayMerBean(
            ppMerchantAddDTO.getMaxLimitCash(),
            ppMerchantAddDTO.getMinLimitCash(),
            ppMerchantAddDTO.getUserLever());
    ppMerchantAddDTO.setMerLimits(ProxyPayMerBean.conver2LimitStr(merBean));
    return ppMerchantAddDTO;
  }

  private PpMerchantEditDTO conver2PpMerchant(PpMerchantEditDTO ppMerchantEditDTO) {
    ProxyPayMerBean merBean =
        new ProxyPayMerBean(
            ppMerchantEditDTO.getMaxLimitCash(),
            ppMerchantEditDTO.getMinLimitCash(),
            ppMerchantEditDTO.getUserLever());
    ppMerchantEditDTO.setMerLimits(ProxyPayMerBean.conver2LimitStr(merBean));
    return ppMerchantEditDTO;
  }

  public PpMerchantVO conver2MerVo(PpMerchantVO vo) {
    ProxyPayMerBean merBean = ProxyPayMerBean.conver2Bean(vo.getMerLimits());
    vo.setMaxLimitCash(merBean.getMaxLimitCash());
    vo.setMinLimitCash(merBean.getMinLimitCash());
    vo.setUserLever(merBean.getUserLever());
    return vo;
  }
}
