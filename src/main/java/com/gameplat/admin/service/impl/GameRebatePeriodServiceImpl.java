package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameRebatePeriodConvert;
import com.gameplat.admin.enums.GameRebatePeriodStatus;
import com.gameplat.admin.mapper.GameRebatePeriodMapper;
import com.gameplat.admin.model.dto.GameRebatePeriodQueryDTO;
import com.gameplat.admin.model.dto.OperGameRebatePeriodDTO;
import com.gameplat.admin.model.vo.GameRebatePeriodVO;
import com.gameplat.admin.service.GameRebateDetailService;
import com.gameplat.admin.service.GameRebatePeriodService;
import com.gameplat.admin.service.GameRebateReportService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.game.GameRebatePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameRebatePeriodServiceImpl
    extends ServiceImpl<GameRebatePeriodMapper, GameRebatePeriod>
    implements GameRebatePeriodService {

  @Autowired private GameRebatePeriodMapper gameRebatePeriodMapper;

  @Autowired private GameRebatePeriodConvert gameRebatePeriodConvert;

  @Autowired private GameRebateReportService gameRebateReportService;

  @Autowired private GameRebateDetailService gameRebateDetailService;

  @Autowired private MemberService memberService;

  @Override
  public IPage<GameRebatePeriodVO> queryGameRebatePeriod(
      Page<GameRebatePeriod> page, GameRebatePeriodQueryDTO dto) {
    QueryWrapper<GameRebatePeriod> queryWrapper = Wrappers.query();
    queryWrapper
        .eq(ObjectUtils.isNotEmpty(dto.getName()), "name", dto.getName())
        .eq(ObjectUtils.isNotEmpty(dto.getStatus()), "status", dto.getStatus());
    if (StringUtils.isNotBlank(dto.getStartTime())) {
      queryWrapper.apply("begin_date >= STR_TO_DATE({0}, '%Y-%m-%d')", dto.getStartTime());
    }
    if (StringUtils.isNotBlank(dto.getEndTime())) {
      queryWrapper.apply("end_date <= STR_TO_DATE({0}, '%Y-%m-%d')", dto.getEndTime());
    }
    queryWrapper.orderByDesc("end_date");
    IPage<GameRebatePeriodVO> gameRebatePeriodVoPage =
        gameRebatePeriodMapper
            .selectPage(page, queryWrapper)
            .convert(gameRebatePeriodConvert::toVo);

    List<GameRebatePeriodVO> gameRebatePeriods = gameRebatePeriodMapper.queryGameRebateCount();
    for (GameRebatePeriodVO gameRebatePeriodVO : gameRebatePeriodVoPage.getRecords()) {
      for (GameRebatePeriodVO gameRebateCount : gameRebatePeriods) {
        if (gameRebatePeriodVO.getId().equals(gameRebateCount.getId())) {
          gameRebatePeriodVO.setLiveRebateCount(gameRebateCount.getLiveRebateCount());
          gameRebatePeriodVO.setRealRebateMoney(gameRebateCount.getRealRebateMoney());
        }
      }
    }
    return gameRebatePeriodVoPage;
  }

  @Override
  public void addGameRebatePeriod(OperGameRebatePeriodDTO dto) {
    GameRebatePeriod gameRebatePeriod = gameRebatePeriodConvert.toEntity(dto);
    gameRebatePeriod.setStatus(GameRebatePeriodStatus.UNSETTLED.getValue());
    // ??????????????????????????????????????????????????????
    //    prePersist(gameRebatePeriod);
    if (!this.save(gameRebatePeriod)) {
      throw new ServiceException("????????????????????????????????????!");
    }
  }

  @Override
  public void updateGameRebatePeriod(OperGameRebatePeriodDTO dto) {
    GameRebatePeriod gameRebatePeriod = gameRebatePeriodConvert.toEntity(dto);
    prePersist(gameRebatePeriod);
    if (!this.updateById(gameRebatePeriod)) {
      throw new ServiceException("????????????????????????????????????!");
    }
  }

  @Override
  public void deleteGameRebatePeriod(Long id, boolean only) {
    if (!this.removeById(id)) {
      throw new ServiceException("????????????????????????????????????!");
    }
    if (!only) {
      // ??????????????????????????????
      gameRebateReportService.deleteByPeriodId(id);
      // ??????????????????????????????
      gameRebateDetailService.deleteByPeriodId(id);
    }
  }

  @Override
  public void settle(Long periodId) {
    GameRebatePeriod gameRebatePeriod = this.getById(periodId);
    if (gameRebatePeriod == null) {
      throw new ServiceException("?????????????????????");
    }
    // ?????????????????????
    LambdaUpdateWrapper<GameRebatePeriod> wrapper = Wrappers.lambdaUpdate();
    wrapper
        .eq(GameRebatePeriod::getId, periodId)
        .set(GameRebatePeriod::getStatus, GameRebatePeriodStatus.SETTLED.getValue());
    if (!this.update(wrapper)) {
      throw new ServiceException("?????????????????????????????????");
    }
    // ???????????????????????????
    gameRebateReportService.deleteByPeriodId(gameRebatePeriod.getId());
    gameRebateReportService.createForGameRebatePeriod(gameRebatePeriod);
    // ???????????????????????????????????????
    gameRebateDetailService.deleteByPeriodId(gameRebatePeriod.getId());
    gameRebateDetailService.createGameRebateDetail(gameRebatePeriod);
  }

  private void prePersist(GameRebatePeriod gameRebatePeriod) {
    if (StringUtils.isNotEmpty(gameRebatePeriod.getBlackAccounts())) {
      String[] accountArray = gameRebatePeriod.getBlackAccounts().split(StrUtil.COMMA);
      Arrays.stream(accountArray)
          .filter(StringUtils::isNotEmpty)
          .forEach(
              account -> {
                memberService
                    .getByAccount(account)
                    .orElseThrow(() -> new ServiceException("??????" + account + "????????????????????????"));
              });
    }
    Date beginDate = DateUtil.beginOfDay(gameRebatePeriod.getBeginDate());
    Date endDate = DateUtil.beginOfDay(gameRebatePeriod.getEndDate());
    if (beginDate.getTime() > endDate.getTime()) {
      throw new ServiceException("???????????????????????????????????????");
    }
    if (countByDateRange(gameRebatePeriod.getId(), beginDate, endDate) > 0) {
      throw new ServiceException("?????????????????????????????????");
    }
    gameRebatePeriod.setBeginDate(beginDate);
    gameRebatePeriod.setEndDate(endDate);
    gameRebatePeriod.setName(
        beginDate.getTime() == endDate.getTime()
            ? DateUtils.format(beginDate)
            : DateUtils.format(beginDate) + " ~ " + DateUtils.format(endDate));
  }

  public Long countByDateRange(Long id, Date beginDate, Date endDate) {
    return this.lambdaQuery()
        .ne(ObjectUtil.isNotEmpty(id), GameRebatePeriod::getId, id)
        .and(
            wrapper ->
                wrapper.and(
                    i ->
                        i.le(
                                ObjectUtils.isNotEmpty(beginDate),
                                GameRebatePeriod::getBeginDate,
                                beginDate)
                            .ge(
                                ObjectUtils.isNotEmpty(beginDate),
                                GameRebatePeriod::getEndDate,
                                beginDate)))
        .or()
        .and(
            i ->
                i.le(ObjectUtils.isNotEmpty(endDate), GameRebatePeriod::getBeginDate, endDate)
                    .ge(ObjectUtils.isNotEmpty(endDate), GameRebatePeriod::getEndDate, endDate))
        .count();
  }
}
