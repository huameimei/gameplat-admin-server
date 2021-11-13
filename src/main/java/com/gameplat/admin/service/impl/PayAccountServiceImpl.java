package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PayAccountConvert;
import com.gameplat.admin.mapper.PayAccountMapper;
import com.gameplat.admin.model.bean.ChannelLimitsBean;
import com.gameplat.admin.model.domain.PayAccount;
import com.gameplat.admin.model.dto.PayAccountAddDTO;
import com.gameplat.admin.model.dto.PayAccountEditDTO;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.vo.PayAccountVO;
import com.gameplat.admin.service.PayAccountService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class PayAccountServiceImpl extends ServiceImpl<PayAccountMapper, PayAccount>
    implements PayAccountService {

  @Autowired private PayAccountConvert payAccountConvert;

  @Autowired private PayAccountMapper payAccountMapper;

  @Override
  public void deleteByPayType(String payType) {
    LambdaQueryWrapper<PayAccount> query = Wrappers.lambdaQuery();
    query.eq(PayAccount::getPayType, payType);
    List<PayAccount> list = this.list(query);
    if (null != list && list.size() > 0) {
      if (0 == payAccountMapper.delete(query)) {
        throw new ServiceException("支付账号删除失败!");
      }
    }
  }

  @Override
  public void update(PayAccountEditDTO dto) {
    /** 检验风控金额的合法性 */
    String riskControlValue =
        ChannelLimitsBean.validateRiskControlValue(
            dto.getRiskControlValue(), dto.getRiskControlType());
    dto.setRiskControlValue(riskControlValue);
    PayAccount usedPayaccount = payAccountMapper.selectById(dto.getId());
    if (null == usedPayaccount) {
      throw new ServiceException("收款账号不存在，请刷新页面重试！");
    }
    conver2PayAccount(dto);
    if (!this.updateById(payAccountConvert.toEntity(dto))) {
      throw new ServiceException("更新失败!");
    }
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<PayAccount> update = Wrappers.lambdaUpdate();
    update.set(PayAccount::getStatus, status).eq(PayAccount::getId, id);
    this.update(update);
  }

  @Override
  public void save(PayAccountAddDTO dto) {
    // 检验风控金额的合法性
    String riskControlValue =
        ChannelLimitsBean.validateRiskControlValue(
            dto.getRiskControlValue(), dto.getRiskControlType());
    dto.setRiskControlValue(riskControlValue);
    conver2PayAccount(dto);
    dto.setStatus(0);
    dto.setRechargeTimes(0L);
    dto.setRechargeAmount(BigDecimal.ZERO);
    if (!this.save(payAccountConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    this.removeById(id);
  }

  @Override
  public IPage<PayAccountVO> findPayAccountPage(Page<PayAccount> page, PayAccountQueryDTO dto) {
    return payAccountMapper.findPayAccountPage(page, dto).convert(this::conver2LimitInfo);
  }

  @Override
  public List<String> queryOwners() {
    QueryWrapper<PayAccount> query = Wrappers.query();
    query.select("distinct owner");
    return this.list(query).stream().map(PayAccount::getOwner).collect(Collectors.toList());
  }

  private PayAccountVO conver2LimitInfo(PayAccountVO vo) {
    JSONObject limitInfo = JSONObject.parseObject(vo.getLimitInfo());
    vo.setLimitStatus(limitInfo.getInteger("limitStatus"));
    vo.setLimitAmount(limitInfo.getBigDecimal("limitAmount"));
    vo.setChannelTimeStatus(limitInfo.getInteger("channelTimeStatus"));
    vo.setChannelTimeStart(limitInfo.getInteger("channelTimeStart"));
    vo.setChannelTimeEnd(limitInfo.getInteger("channelTimeEnd"));
    vo.setChannelShows(limitInfo.getString("channelShows"));
    vo.setMinAmountPerOrder(limitInfo.getBigDecimal("minAmountPerOrder"));
    vo.setMaxAmountPerOrder(limitInfo.getBigDecimal("maxAmountPerOrder"));
    vo.setRiskControlType(limitInfo.getInteger("riskControlType"));
    vo.setRiskControlValue(limitInfo.getString("riskControlValue"));
    return vo;
  }

  private void conver2PayAccount(PayAccountAddDTO dto) {
    ChannelLimitsBean merBean =
        new ChannelLimitsBean(
            dto.getLimitStatus(),
            dto.getLimitAmount(),
            dto.getChannelTimeStatus(),
            dto.getChannelTimeStart(),
            dto.getChannelTimeEnd(),
            dto.getChannelShows(),
            dto.getMinAmountPerOrder(),
            dto.getMaxAmountPerOrder(),
            dto.getRiskControlType(),
            dto.getRiskControlValue(),
            null);
    dto.setLimitInfo(JsonUtils.toJson(merBean));
  }

  private void conver2PayAccount(PayAccountEditDTO dto) {
    ChannelLimitsBean merBean =
        new ChannelLimitsBean(
            dto.getLimitStatus(),
            dto.getLimitAmount(),
            dto.getChannelTimeStatus(),
            dto.getChannelTimeStart(),
            dto.getChannelTimeEnd(),
            dto.getChannelShows(),
            dto.getMinAmountPerOrder(),
            dto.getMaxAmountPerOrder(),
            dto.getRiskControlType(),
            dto.getRiskControlValue(),
            null);
    dto.setLimitInfo(JsonUtils.toJson(merBean));
  }
}
