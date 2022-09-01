package com.gameplat.admin.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gameplat.admin.model.dto.GameBizDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.GameBalanceService;
import com.gameplat.admin.service.GameService;
import com.gameplat.admin.service.GameTransferInfoService;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.TrueFalse;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameTransferInfo;
import com.gameplat.model.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.gameplat.common.enums.GameRedisEnums.GAME_BALANCE_QUERY;


@Slf4j
@Service
public class GameBalanceServiceImpl implements GameBalanceService {

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @Resource
  private GameTransferInfoService gameTransferInfoService;

  @Resource
  private GameAdminService gameAdminService;

  @Resource(name = "asyncGameExecutor")
  private Executor asyncGameExecutor;

  @Resource
  private GameService gameService;

  @Override
  public String getUserGameBalanceCacheKey(Long memberId) {
    return MessageFormat.format(GAME_BALANCE_QUERY.getRedisKey(), String.valueOf(memberId));
  }

  @Override
  public void removeUserGameBalanceCache(String account) {

    String isBalanceQuery = MessageFormat.format(GAME_BALANCE_QUERY.getRedisKey(), account);
    redisTemplate.delete(isBalanceQuery);

  }

  @Override
  public List<GameTransferInfo> getAndUpdateGameBalance(List<GamePlatform> platforms, Member member) {

    String userGameBalanceCacheKey = this.getUserGameBalanceCacheKey(member.getId());
    // 如果最近5-10秒内没有查询过该玩家的游戏余额，则进行游戏余额查询和更新
    if (!Optional.ofNullable(redisTemplate.hasKey(userGameBalanceCacheKey)).orElse(Boolean.FALSE)) {
      this.asyncGetAndUpdateGameBalance(platforms, member);
      redisTemplate.opsForValue().set(userGameBalanceCacheKey, member.getAccount(), RandomUtil.randomInt(5, 10), TimeUnit.SECONDS);
    }
    //如果最近已经查询过游戏余额, 直接返回game_transfer_info表查询结果
    return gameTransferInfoService.getGameTransferInfoList(member.getId());
  }

  /**
   * 异步获取游戏余额
   *
   * @param platforms List
   * @param member 会员信息
   */
  private List<GameBalanceVO> asyncGetAndUpdateGameBalance(List<GamePlatform> platforms, Member member) {
    return platforms.stream()
      .map(platform -> CompletableFuture.supplyAsync(
        () -> this.getOrUpdateGetGameBalance(platform, member, Boolean.FALSE), asyncGameExecutor))
      .map(CompletableFuture::join)
      .collect(Collectors.toList());
  }

  private Boolean isGameBalanceValid(GameTransferInfo transfer) {

    return !(new Date().getTime() - transfer.getUpdateTime().getTime() > 10000);

  }

  private GameBalanceVO gameBalanceHandler(GameTransferInfo gameTransferInfo) {

    return GameBalanceVO.builder()
      .balance(gameTransferInfo.getLastBalance())
      .platformCode(gameTransferInfo.getPlatformCode())
      .platformName(GamePlatformEnum.getName(gameTransferInfo.getPlatformCode()))
      .build();

  }

  /**
   * @param platform 游戏平台信息
   * @param member 会员信息
   * @param isRefreshNow 是否立刻刷新游戏余额，true 是， false 否。
   * @return
   */
  private GameBalanceVO getOrUpdateGetGameBalance(GamePlatform platform, Member member, Boolean isRefreshNow) {

    // 如果不是需要立刻刷新游戏余额，则根据game_transfer_info表中更新时间决定是否重新查询游戏。
    if (!isRefreshNow) {
      GameTransferInfo transfer = gameTransferInfoService.getGameTransferInfo(member.getId(), platform.getCode());
      if (ObjectUtils.isNotEmpty(transfer) && isGameBalanceValid(transfer)) {
        return this.gameBalanceHandler(transfer);
      }
    }

    GameBalanceVO vo = new GameBalanceVO();
    vo.setPlatformCode(platform.getCode());
    vo.setPlatformName(platform.getName());

    // 查看是否在维护
    if (gameService.checkTransferMainTime(platform.getCode())) {
      vo.setErrorMsg("游戏通道正在维护中");
      vo.setBalance(BigDecimal.ZERO);
      return vo;
    }

    if (platform.getTransfer() != null
      && platform.getTransfer().equals(TrueFalse.FALSE.getValue())) {
      vo.setErrorMsg(platform.getName() + "查询余额通道正在维护中,请耐心等待");
      vo.setBalance(BigDecimal.ZERO);
      return vo;
    }

    BigDecimal gameBalance = BigDecimal.ZERO;
    try {
      gameBalance = gameAdminService.getBalance(platform.getCode(), member);
      vo.setBalance(gameBalance);
    } catch (Exception e) {
      log.error("查询余额错误：", e);
      vo.setErrorMsg("查询游戏余额失败");
      vo.setBalance(gameBalance);
    }

    // 更新游戏余额
    GameTransferInfo transferInfo = new GameTransferInfo();
    transferInfo.setMemberId(member.getId());
    transferInfo.setAccount(member.getAccount());
    transferInfo.setPlatformCode(platform.getCode());
    transferInfo.setLastBalance(gameBalance);
    gameTransferInfoService.insertOrUpdate(transferInfo);

    return vo;
  }
}
