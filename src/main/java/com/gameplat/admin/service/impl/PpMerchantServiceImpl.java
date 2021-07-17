package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PpMerchantConvert;
import com.gameplat.admin.dao.PpMerchantMapper;
import com.gameplat.admin.model.bean.ProxyPayMerBean;
import com.gameplat.admin.model.dto.PpMerchantAddDTO;
import com.gameplat.admin.model.dto.PpMerchantEditDTO;
import com.gameplat.admin.model.entity.PpMerchant;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.admin.service.PpMerchantService;
import com.gameplat.admin.utils.EncryptUtils;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.security.util.SecurityUtil;
import java.util.Date;
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
public class PpMerchantServiceImpl extends ServiceImpl<PpMerchantMapper, PpMerchant>
    implements PpMerchantService {

  @Autowired private PpMerchantConvert ppMerchantConvert;

  @Autowired private PpMerchantMapper ppMerchantMapper;

  @Autowired private PpInterfaceService ppInterfaceService;

  @Override
  public void update(PpMerchantEditDTO dto) {
    PpMerchant ppMerchant = this.getById(dto.getId());
    PpInterfaceVO ppInterfaceVO = ppInterfaceService.queryPpInterface(dto.getPpInterfaceCode());
    Map<String, String> oriMerchantParameters = JsonUtils.toMapObject(ppMerchant.getParameters());
    List<String> ppInterfaceParameters =
        JSON.parseArray(ppInterfaceVO.getParameters(), String.class);
    Map<String, String> parametersMap = JsonUtils.toMapObject(dto.getParameters());
    // 如果加密字段没变，过滤字段不加密,如果加密字段在原基础上修改，抛异常重新处理
    if (CollectionUtils.isNotEmpty(ppInterfaceParameters)) {
      ppInterfaceParameters.forEach(
          paramJsonStr -> {
            Map<String, Object> config = JsonUtils.toMapObject(paramJsonStr);
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
    this.getById(id).deleteById();
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
    Map<String, String> merchantParameters = JsonUtils.toMapObject(ppMerchantVO.getParameters());
    List<String> ppInterfaceParameters =
        JSON.parseArray(ppInterfaceVO.getParameters(), String.class);
    // 商户号秘钥等信息加密展示处理
    if (CollectionUtils.isNotEmpty(ppInterfaceParameters)) {
      ppInterfaceParameters.forEach(
          jsonStrParam -> {
            Map<String, Object> config = JsonUtils.toMapObject(jsonStrParam);
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
    update.set(PpMerchant::getStatus, status);
    update.eq(PpMerchant::getId, id);
    this.update(update);
  }

  @Override
  public IPage<PpMerchantVO> queryPage(Page<PpMerchantVO> page, Integer status, String name) {
    return ppMerchantMapper.findPpMerchantPage(page, status, name);
  }

  @Override
  public List<PpMerchantVO> queryAllMerchant(Integer status) {
    LambdaQueryWrapper<PpMerchant> query = Wrappers.lambdaQuery();
    query.eq(PpMerchant::getStatus, status);
    return this.list(query).stream()
        .map(e -> ppMerchantConvert.toVo(e))
        .collect(Collectors.toList());
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

  private void conver2MerVo(PpMerchantVO vo) {
    ProxyPayMerBean merBean = ProxyPayMerBean.conver2Bean(vo.getMerLimits());
    vo.setMaxLimitCash(merBean.getMaxLimitCash());
    vo.setMinLimitCash(merBean.getMinLimitCash());
    vo.setUserLever(merBean.getUserLever());
  }
}
