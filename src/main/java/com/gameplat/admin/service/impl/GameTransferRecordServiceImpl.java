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
import com.gameplat.model.entity.game.GameTransferRecord;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
  public void fillOrders(OperGameTransferRecordDTO liveTransferRecord) {
    UpdateWrapper<GameTransferRecord> updateWrapper = new UpdateWrapper();
    updateWrapper.set(
        ObjectUtils.isNotNull(liveTransferRecord.getStatus()),
        "status",
        liveTransferRecord.getStatus());
    updateWrapper.set(
        StringUtils.isNotBlank(liveTransferRecord.getRemark()),
        "remark",
        liveTransferRecord.getRemark());
    updateWrapper.in("status", Lists.newArrayList(1, 2, 5));
    updateWrapper.eq("id", liveTransferRecord.getId());
    if (gameTransferRecordMapper.update(null, updateWrapper) < 0) {
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
}
