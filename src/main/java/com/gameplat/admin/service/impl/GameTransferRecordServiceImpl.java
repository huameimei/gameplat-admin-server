package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameTransferRecordMapper;
import com.gameplat.admin.model.dto.GameTransferRecordQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameTransferRecordService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.GameTransferStatus;
import com.gameplat.model.entity.game.GameTransferRecord;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameTransferRecordServiceImpl
    extends ServiceImpl<GameTransferRecordMapper, GameTransferRecord>
    implements GameTransferRecordService {

  @Autowired private GameTransferRecordMapper gameTransferRecordMapper;

  @Override
  public PageDtoVO<GameTransferRecord> queryGameTransferRecord(
      Page<GameTransferRecord> page, GameTransferRecordQueryDTO dto) {
    PageDtoVO<GameTransferRecord> pageDtoVO = new PageDtoVO();
    QueryWrapper<GameTransferRecord> queryWrapper = Wrappers.query();
    if (StringUtils.isNotBlank(dto.getAccount())) {
      if (Boolean.TRUE.equals(dto.getAccountFuzzy())) {
        queryWrapper.like("account", dto.getAccount());
      } else {
        queryWrapper.eq("account", dto.getAccount());
      }
    }
    queryWrapper.eq(StringUtils.isNotBlank(dto.getOrderNo()), "order_no", dto.getOrderNo());
    queryWrapper.eq(
        StringUtils.isNotBlank(dto.getPlatformCode()), "platform_code", dto.getPlatformCode());
    queryWrapper.eq(
        ObjectUtils.isNotNull(dto.getTransferType()), "transfer_type", dto.getTransferType());
    queryWrapper.eq(ObjectUtils.isNotNull(dto.getStatus()), "status", dto.getStatus());
    queryWrapper.eq(
        ObjectUtils.isNotNull(dto.getTransferStatus()), "transfer_status", dto.getTransferStatus());
    if (StringUtils.isNotBlank(dto.getCreateTimeStart())) {
      queryWrapper.apply(
          "create_time >= STR_TO_DATE({0},'%Y-%m-%d %H:%i:%S')",
          DateUtil.beginOfDay(DateUtil.parse(dto.getCreateTimeStart())));
    }
    if (StringUtils.isNotBlank(dto.getCreateTimeEnd())) {
      queryWrapper.apply(
          "create_time <= STR_TO_DATE({0},'%Y-%m-%d %H:%i:%S')",
          DateUtil.endOfDay(DateUtil.parse(dto.getCreateTimeEnd())));
    }
    queryWrapper.orderByDesc("create_time");
    Page<GameTransferRecord> liveRebatePeriodVoPage =
        gameTransferRecordMapper.selectPage(page, queryWrapper);

    queryWrapper.select(
        "SUM(CASE WHEN transfer_type=1 AND STATUS=3 THEN amount ELSE 0 END)-SUM(CASE WHEN transfer_type=2 AND STATUS=3 THEN amount ELSE 0 END) AS amount");
    GameTransferRecord total =
        Optional.ofNullable(gameTransferRecordMapper.selectOne(queryWrapper))
            .orElse(new GameTransferRecord());
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", total);
    pageDtoVO.setPage(liveRebatePeriodVoPage);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public void fillOrders(OperGameTransferRecordDTO dto) {
    UpdateWrapper<GameTransferRecord> updateWrapper = new UpdateWrapper();
    updateWrapper.set(ObjectUtils.isNotNull(dto.getStatus()), "status", dto.getStatus());
    updateWrapper.set(StringUtils.isNotBlank(dto.getRemark()), "remark", dto.getRemark());
    updateWrapper.in(
        "status",
        Lists.newArrayList(
            GameTransferStatus.OUT.getValue(),
            GameTransferStatus.IN.getValue(),
            GameTransferStatus.IN_GAME_FAIL.getValue()));
    updateWrapper.eq("id", dto.getId());
    if (gameTransferRecordMapper.update(new GameTransferRecord(), updateWrapper) < 0) {
      throw new ServiceException("额度转换记录更新失败");
    }
  }

  @Override
  public boolean findTransferRecordCount(GameTransferRecord dto) {
    QueryWrapper<GameTransferRecord> queryWrapper = Wrappers.query();
    queryWrapper.eq(
        StringUtils.isNotBlank(dto.getPlatformCode()), "platform_code", dto.getPlatformCode());
    queryWrapper.eq(StringUtils.isNotBlank(dto.getAccount()), "account", dto.getAccount());
    return gameTransferRecordMapper.selectCount(queryWrapper) > 0;
  }

  @Override
  public List<GameTransferRecord> findPlatformCodeList(Long memberId) {
    QueryWrapper<GameTransferRecord> queryWrapper = Wrappers.query();
    queryWrapper.select("distinct platform_code");
    queryWrapper.eq("member_id", memberId);
    return gameTransferRecordMapper.selectList(queryWrapper);
  }

  @Override
  @Async
  public void saveTransferRecord(GameTransferRecord transferRecord) {
    try {
      log.info(
          "游戏转换插入数据:"
              + transferRecord.getAccount()
              + ",金额:"
              + transferRecord.getAmount()
              + ",订单号:"
              + transferRecord.getOrderNo()
              + ",备注:"
              + transferRecord.getRemark());
      this.save(transferRecord);
    } catch (Exception e) {
      log.error("游戏转换插入数据:订单号=" + transferRecord.getOrderNo(), e);
    }
  }

  @Override
  public long findGameTransferFailRecord() {
    // 失败的记录
    return this.lambdaQuery()
            .eq(GameTransferRecord::getStatus, GameTransferStatus.OUT.getValue())
            .or()
            .eq(GameTransferRecord::getStatus, GameTransferStatus.IN.getValue())
            .or()
            .eq(GameTransferRecord::getStatus, GameTransferStatus.IN_GAME_FAIL.getValue())
            .or()
            .count();
  }
}
