package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.TpPayChannelConvert;
import com.gameplat.admin.mapper.TpPayChannelMapper;
import com.gameplat.admin.model.bean.ChannelLimitsBean;
import com.gameplat.admin.model.domain.TpPayChannel;
import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import com.gameplat.admin.service.TpPayChannelService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TpPayChannelServiceImpl extends ServiceImpl<TpPayChannelMapper, TpPayChannel>
    implements TpPayChannelService {

  @Autowired private TpPayChannelConvert tpPayChannelConvert;

  @Autowired private TpPayChannelMapper tpPayChannelMapper;

  @Override
  public void deleteByMerchantId(Long merchantId) {
    LambdaQueryWrapper<TpPayChannel> query = Wrappers.lambdaQuery();
    query.eq(TpPayChannel::getMerchantId, merchantId);
    List<TpPayChannel> list = this.list(query);
    if (null != list && list.size() > 0) {
      if (0 == tpPayChannelMapper.delete(query)) {
        throw new ServiceException("支付通道删除失败!");
      }
    }
  }

  @Override
  public void update(TpPayChannelEditDTO dto) {
    /** 检验风控金额的合法性 */
    String riskControlValue =
        ChannelLimitsBean.validateRiskControlValue(
            dto.getRiskControlValue(), dto.getRiskControlType());
    dto.setRiskControlValue(riskControlValue);
    conver2TpPayChannel(dto);
    if (!this.updateById(tpPayChannelConvert.toEntity(dto))) {
      throw new ServiceException("更新失败!");
    }
  }

  @Override
  public void updateStatus(Long id, Integer status) {
    if (null == status) {
      throw new ServiceException("状态不能为空!");
    }
    LambdaUpdateWrapper<TpPayChannel> update = Wrappers.lambdaUpdate();
    update.set(TpPayChannel::getStatus, status).eq(TpPayChannel::getId, id);
    this.update(update);
  }

  @Override
  public void save(TpPayChannelAddDTO dto) {
    /** 检验风控金额的合法性 */
    String riskControlValue =
        ChannelLimitsBean.validateRiskControlValue(
            dto.getRiskControlValue(), dto.getRiskControlType());
    dto.setRiskControlValue(riskControlValue);
    conver2TpPayChannel(dto);
    dto.setStatus(0);
    dto.setRechargeTimes(0L);
    dto.setRechargeAmount(BigDecimal.ZERO);
    if (!this.save(tpPayChannelConvert.toEntity(dto))) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public void delete(Long id) {
    this.removeById(id);
  }

  @Override
  public IPage<TpPayChannelVO> findTpPayChannelPage(
      Page<TpPayChannel> page, TpPayChannelQueryDTO dto) {
    IPage<TpPayChannelVO> ipage = tpPayChannelMapper.findTpPayChannelPage(page, dto);
    List<TpPayChannelVO> list = ipage.getRecords();
    list.stream()
        .forEach(
            (a -> {
              this.conver2LimitInfo(a);
            }));
    return ipage;
  }

  private void conver2LimitInfo(TpPayChannelVO vo) {
    JSONObject limitInfo = JSONObject.parseObject(vo.getLimitInfo());
    vo.setLimitStatus(limitInfo.getInteger("limitStatus"));
    vo.setLimitAmount(limitInfo.getLong("limitAmount"));
    vo.setChannelTimeStatus(limitInfo.getInteger("channelTimeStatus"));
    vo.setChannelTimeStart(limitInfo.getInteger("channelTimeStart"));
    vo.setChannelTimeEnd(limitInfo.getInteger("channelTimeEnd"));
    vo.setChannelShows(limitInfo.getString("channelShows"));
    vo.setMinAmountPerOrder(limitInfo.getBigDecimal("minAmountPerOrder"));
    vo.setMaxAmountPerOrder(limitInfo.getBigDecimal("maxAmountPerOrder"));
    vo.setRiskControlType(limitInfo.getInteger("riskControlType"));
    vo.setRiskControlValue(limitInfo.getString("riskControlValue"));
  }

  private void conver2TpPayChannel(TpPayChannelAddDTO dto) {
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

  private void conver2TpPayChannel(TpPayChannelEditDTO dto) {
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
