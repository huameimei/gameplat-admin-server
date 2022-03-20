package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.model.vo.GameRecycleVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameTransferRecordService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameTransferRecord;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.redis.api.RedisService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Api("游戏相关")
@RestController
@RequestMapping("/api/admin/game")
public class GameAdminController {

  @Autowired private GameAdminService gameAdminService;

  @Autowired private MemberService memberService;

  @Autowired private RedisService redisService;

  @Autowired private GamePlatformService gamePlatformService;

  @Autowired private GameTransferRecordService gameTransferRecordService;

  /** 查询单个真人平台余额 */
  @SneakyThrows
  @GetMapping(value = "/selectGameBalance")
  public GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto) {
    GameBalanceVO gameBalanceVO = new GameBalanceVO();
    gameBalanceVO.setPlatformCode(dto.getPlatform().get("platformCode"));
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在");
    gameBalanceVO.setBalance(
        gameAdminService.getBalance(dto.getPlatform().get("platformCode"), member));
    return gameBalanceVO;
  }

  @SneakyThrows
  @ApiOperation("转账到真人")
  @PostMapping(value = "/transferToGame")
  public void transferToGame(@RequestBody OperGameTransferRecordDTO record) {
    String key = "self_" + record.getPlatformCode() + '_' + record.getAccount();
    try {
      redisService.getStringOps().setEx(key, "game_transfer", 3, TimeUnit.MINUTES);
      Member member = memberService.getMemberAndFillGameAccount(record.getAccount());
      Assert.notNull(member, "会员账号不存在");
      gameAdminService.transferOut(record.getPlatformCode(), record.getAmount(), member, false);
    } finally {
      redisService.getKeyOps().delete(key);
    }
  }

  @ApiOperation("回收金额")
  @PostMapping(value = "/recyclingAmount")
  public Map<String, String> recyclingAmount(@RequestBody GameBalanceQueryDTO dto) {
    Map<String, String> map = new HashMap<>();
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.isNull(member, "会员账号不存在");
    dto.getPlatform()
        .forEach(
            (key, value) -> {
              try {
                if (StringUtils.isNotBlank(value)
                    && new BigDecimal(value).compareTo(BigDecimal.ZERO) > 0) {
                  gameAdminService.transfer(
                      key, TransferTypesEnum.SELF.getCode(), new BigDecimal(value), member, false);
                  dto.getPlatform().put(key, (int) Double.parseDouble(value) + "");
                } else {
                  dto.getPlatform().put(key, "0");
                  map.put(key, "金额必须大于零");
                }
              } catch (Exception e) {
                log.info(e.getMessage());
                map.put(key, e.getMessage());
              }
            });
    return map;
  }

  @ApiOperation("没收")
  @RequestMapping(value = "/confiscated", method = RequestMethod.POST)
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'没收会员游戏金额，id='+#memberInfo.id")
  public Map<String, String> confiscated(@RequestBody GameBalanceQueryDTO dto) throws Exception {
    Map<String, String> map = new HashMap<>();
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在");
    dto.getPlatform()
        .forEach(
            (key, value) -> {
              try {
                if (StringUtils.isNotBlank(value)
                    && new BigDecimal(value).compareTo(BigDecimal.ZERO) > 0) {
                  gameAdminService.confiscated(key, new BigDecimal(value), member);
                } else {
                  map.put(key, "金额必须大于零");
                  dto.getPlatform().put(key, "0");
                }
              } catch (Exception e) {
                log.info(e.getMessage());
                e.printStackTrace();
                map.put(key, e.getMessage());
              }
            });
    return map;
  }

  @ApiOperation("查询游戏平台余额")
  @GetMapping(value = "/selectGameAllBalance")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'查询会员游戏金额，id='+#memberInfo.id")
  public Map<String, BigDecimal> selectGameAllBalance(MemberInfo memberInfo) {
    Member member = memberService.getById(memberInfo.getMemberId());
    Member memberAccount = memberService.getMemberAndFillGameAccount(member.getAccount());
    Assert.notNull(memberAccount, "会员账号不存在");
    Map<String, BigDecimal> map = new HashMap<>();
    List<GamePlatform> gamePlatformList = gamePlatformService.queryByTransfer();
    if (!CollectionUtils.isEmpty(gamePlatformList)) {
      gamePlatformList.stream()
          .parallel()
          .forEach(
              gamePlatform -> {
                try {
                  map.put(
                      gamePlatform.getCode(),
                      gameAdminService.getBalance(gamePlatform.getCode(), memberAccount));
                } catch (Exception e) {
                  log.info(gamePlatform.getCode() + "游戏查询异常：", e);
                  map.put(gamePlatform.getCode(), BigDecimal.ZERO);
                }
              });
    }
    return map;
  }

  @ApiOperation("回收金额")
  @PostMapping(value = "/recyclingAmountByAccount")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'回收会员游戏金额，account='+#dto.account")
  public Map<String, Object> recyclingAmountByAccount(@RequestBody GameBalanceQueryDTO dto) {
    Map<String, Object> map = new HashMap<>();
    if (null == dto.getPlatform() || null == dto.getPlatform().get("platformCode")) {
      map.put("errorCode", "真人类型不正确");
      return map;
    }
    if (StringUtils.isBlank(dto.getAccount())) {
      map.put("errorCode", "会员账号不能为空");
      return map;
    }
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    if (null == member) {
      map.put("errorCode", "会员账号不存在，请重新检查");
      return map;
    }
    String platformCode = dto.getPlatform().get("platformCode");
    try {
      BigDecimal money = gameAdminService.getBalance(platformCode, member);
      if (null != money && money.compareTo(BigDecimal.ONE) > 0) {
        BigDecimal transferMoney = new BigDecimal((int) Double.parseDouble(money.toString()));
        gameAdminService.transfer(
            platformCode, TransferTypesEnum.SELF.getCode(), transferMoney, member, false);
        map.put("balance", transferMoney);
        log.info("真人额度回收 => 会员账号：{}，真人类型：{}，金额：{}", dto.getAccount(), platformCode, transferMoney);
      } else {
        map.put("errorCode", "回收金额必须大于0");
        return map;
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "获取真人信息错误，请联系客服查看");
    }
    return map;
  }

  @ApiOperation("查询单个游戏平台余额")
  @PostMapping(value = "/selectGameBalanceByAccount")
  public Map<String, Object> selectGameBalanceByAccount(@RequestBody GameBalanceQueryDTO dto) {
    Map<String, Object> map = new HashMap<>();
    if (null == dto.getPlatform() || null == dto.getPlatform().get("platformCode")) {
      map.put("errorCode", "真人类型不正确");
      return map;
    }
    if (StringUtils.isBlank(dto.getAccount())) {
      map.put("errorCode", "会员账号不能为空");
      return map;
    }
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    if (null == member) {
      map.put("errorCode", "会员账号不存在，请重新检查");
      return map;
    }
    try {
      map.put("platformCode", dto.getPlatform().get("platformCode"));
      map.put(
          "balance", gameAdminService.getBalance(dto.getPlatform().get("platformCode"), member));
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "获取真人信息错误，请联系客服查看");
    }
    return map;
  }

  @PostMapping(value = "/reclaimLiveAmountAsync")
  public Result<List<GameRecycleVO>> reclaimLiveAmountAsync(@RequestBody GameBalanceQueryDTO dto) {
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    List<GameTransferRecord> recordList =
        gameTransferRecordService.findPlatformCodeList(member.getId());
    List<String> platformList = new ArrayList<>();
    recordList.forEach(item -> platformList.add(item.getPlatformCode()));
    List<GamePlatform> gamePlatformList = gamePlatformService.list();
    // 仅获取当前会员玩过得游戏
    List<GamePlatform> playedPlatformList =
        gamePlatformList.stream()
            .filter(item -> platformList.contains(item.getCode()))
            .collect(Collectors.toList());
    if (org.apache.commons.collections.CollectionUtils.isEmpty(playedPlatformList)) {
      return Result.failed("游戏平台额度转换通道维护中");
    }
    String key = "user_game_balance_" + member.getId();
    try {
      redisService.getStringOps().setEx(key, "user_game_balance", 300, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("{}一键回收游戏平台额度操作频繁", dto.getAccount());
      return Result.failed("一键回收游戏平台额度操作频繁");
    }
    List<GameRecycleVO> resultList = new ArrayList<>();
    try {
      StopWatch stopwatch = new StopWatch();
      stopwatch.start();
      // 每10个切分成一个list去请求
      List<List<GamePlatform>> allList = Lists.partition(playedPlatformList, 10);
      List<CompletableFuture<GameRecycleVO>> taskList;

      for (List<GamePlatform> partList : allList) {
        taskList = new ArrayList<>();
        for (GamePlatform item : partList) {
          CompletableFuture<GameRecycleVO> completableFuture =
              CompletableFuture.supplyAsync(
                  () -> {
                    GameRecycleVO vo = new GameRecycleVO();
                    vo.setPlatfromCode(item.getCode());
                    vo.setPlatformName(item.getName());
                    vo.setStatus(0);
                    try {
                      gameAdminService.transferIn(item.getCode(), null, member, false, true);
                    } catch (Exception e) {
                      vo.setStatus(1);
                      vo.setErrorMsg(e.getMessage());
                      log.error("回收失败：{}", e.getMessage());
                    }
                    return vo;
                  });
          taskList.add(completableFuture);
        }

        // 等待10次异步请求结果
        CompletableFuture.allOf(taskList.toArray(new CompletableFuture[0])).join();
        taskList.forEach(
            item -> {
              try {
                resultList.add(item.get());
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
              }
            });
      }
      stopwatch.stop();
    } finally {
      redisService.getKeyOps().delete(key);
    }
    return Result.succeedData(resultList);
  }
}
