package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityRedPacketConvert;
import com.gameplat.admin.mapper.ActivityRedPacketMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.model.vo.ActivityRedPacketVO;
import com.gameplat.admin.model.vo.ActivityTurntablePrizeConfigVO;
import com.gameplat.admin.model.vo.MemberActivityPrizeVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.model.entity.activity.ActivityRedPacket;
import com.gameplat.model.entity.activity.ActivityRedPacketCondition;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动红包雨业务处理
 *
 * @author kenvin
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ActivityRedPacketServiceImpl
    extends ServiceImpl<ActivityRedPacketMapper, ActivityRedPacket>
    implements ActivityRedPacketService {

  @Autowired private ActivityRedPacketConvert activityRedPacketConvert;

  @Autowired private ActivityPrizeService activityPrizeService;

  @Autowired private ActivityRedPacketConditionService activityRedPacketConditionService;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private ConfigService configService;

  @Override
  public IPage<ActivityRedPacketVO> redPacketList(
      PageDTO<ActivityRedPacket> page, ActivityRedPacketQueryDTO activityRedPacketQueryDTO) {
    return this.lambdaQuery()
        .eq(
            activityRedPacketQueryDTO.getPacketType() != null,
            ActivityRedPacket::getPacketType,
            activityRedPacketQueryDTO.getPacketType())
        .page(page)
        .convert(activityRedPacketConvert::toVo);
  }

  @Override
  public void add(ActivityRedPacketAddDTO dto) {
    ActivityRedPacket activityRedPacket = activityRedPacketConvert.toEntity(dto);
    this.save(activityRedPacket);
  }

  @Override
  public void edit(ActivityRedPacketUpdateDTO activityRedPacketUpdateDTO) {
    ActivityRedPacket activityRedPacket =
        activityRedPacketConvert.toEntity(activityRedPacketUpdateDTO);
    this.updateById(activityRedPacket);
  }

  @Override
  public void updateStatus(Long packetId) {
    if (packetId == null || packetId == 0) {
      throw new ServiceException("packetId不能为空");
    }
    ActivityRedPacket activityRedPacket = this.getById(packetId);
    if (activityRedPacket == null) {
      throw new ServiceException("该红包雨配置不存在");
    }
    if (activityRedPacket.getStatus() == 0) {
      throw new ServiceException("该数据已下线");
    }
    if (activityRedPacket.getStatus() == 2) {
      throw new ServiceException("该数据未上线");
    }

    LambdaUpdateChainWrapper<ActivityRedPacket> updateChainWrapper =
        lambdaUpdate()
            .set(ActivityRedPacket::getStatus, 0)
            .eq(ActivityRedPacket::getPacketId, packetId);
    if (!this.update(updateChainWrapper)) {
      throw new ServiceException("更新红包雨状态失败");
    }
  }

  @Override
  public void delete(String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    String[] idArr = ids.split(",");
    List<Long> idList = new ArrayList<>();
    for (String idStr : idArr) {
      idList.add(Long.parseLong(idStr));
    }
    this.removeByIds(idList);
  }

  @Override
  public Object discountList(ActivityRedPacketDiscountDTO activityRedPacketDiscountDTO) {
    if (activityRedPacketDiscountDTO.getType() == 1) {
      MemberActivityPrizeVO memberActivityPrizeBean = new MemberActivityPrizeVO();
      memberActivityPrizeBean.setActivityId(activityRedPacketDiscountDTO.getId());
      memberActivityPrizeBean.setType(1);
      return activityPrizeService.findActivityPrizeList(memberActivityPrizeBean);
    } else if (activityRedPacketDiscountDTO.getType() == 2) {
      return activityRedPacketConditionService
          .lambdaQuery()
          .eq(ActivityRedPacketCondition::getRedPacketId, activityRedPacketDiscountDTO.getId())
          .list();
    }
    return new ArrayList<>();
  }

  @Override
  public ActivityRedPacketConfigVO getConfig() {
    String redPacketConfig = configService.getValue(DictDataEnum.REDPACKET);
    if (StringUtils.isBlank(redPacketConfig)) {
      throw new ServiceException("活动红包配置没有配置，请先配置红包数据");
    }
    ActivityRedPacketConfigVO configVO =
        JSON.parseObject(redPacketConfig, ActivityRedPacketConfigVO.class);
    if (configVO == null) {
      throw new ServiceException("活动红包配置没有配置，请先配置红包数据");
    }
    return configVO;
  }

  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void updateConfig(ActivityRedPacketConfigDTO configDTO) {
    SysDictData sysDictData =
        sysDictDataService.getDictData(
            DictDataEnum.REDPACKET.getType().getValue(), DictDataEnum.REDPACKET.getLabel());
    if (sysDictData == null || StringUtils.isBlank(sysDictData.getDictValue())) {
      throw new ServiceException("活动红包配置没有配置，请先配置红包数据");
    }
    // 更新配置信息
    sysDictData.setDictValue(JSON.toJSONString(configDTO));
    boolean result = sysDictDataService.updateById(sysDictData);
    if (!result) {
      throw new ServiceException("更新红包配置失败");
    }
  }

  @Override
  public List<ActivityTurntablePrizeConfigVO> getTurntablePrizeConfig() {
    String configValue = configService.getValue(DictDataEnum.TURNTABLE_PRIZE);
    if (StringUtils.isBlank(configValue)) {
      throw new ServiceException("活动红包配置没有配置，请先配置红包数据");
    }
    return JSON.parseArray(configValue, ActivityTurntablePrizeConfigVO.class);
  }

  @Override
  public void updateTurntablePrizeConfig(ActivityTurntablePrizeConfigDTO dto) {
    List<ActivityTurntablePrizeConfigVO> list = getTurntablePrizeConfig();
    for (ActivityTurntablePrizeConfigVO vo : list) {
      if (vo.getPrizeId().equals(dto.getPrizeId())) {
        // 中将概率不能修改
        Double probability = vo.getProbability();
        BeanUtils.copyProperties(dto, vo);
        vo.setProbability(probability);
      }
    }
    SysDictData sysDictData =
        sysDictDataService.getDictData(
            DictDataEnum.TURNTABLE_PRIZE.getType().getValue(),
            DictDataEnum.TURNTABLE_PRIZE.getLabel());
    sysDictData.setDictValue(JSON.toJSONString(list));
    boolean result = sysDictDataService.updateById(sysDictData);
    if (!result) {
      throw new ServiceException("更新转盘奖品配置失败");
    }
  }
}
