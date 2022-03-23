package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.model.vo.GameConfiscatedVO;
import com.gameplat.admin.model.vo.GameRecycleVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.ResultStatusEnum;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.Member;
import com.gameplat.redis.api.RedisService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/game")
public class GameAdminController {
  @Autowired private GameAdminService gameAdminService;
  @Autowired private MemberService memberService;
  @Autowired private RedisService redisService;

  /** 查询单个游戏平台余额 */
  @GetMapping(value = "/selectGameBalance")
  public GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto) throws Exception {
    GameBalanceVO gameBalanceVO = new GameBalanceVO();
    Assert.notEmpty(dto.getAccount(), "会员账号不能为空");
    Assert.notEmpty(dto.getPlatformCode(), "游戏平台编码不能为空");
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在");

    gameBalanceVO.setPlatformCode(dto.getPlatformCode());
    gameBalanceVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
    try {
      BigDecimal amount =
          gameAdminService.getBalance(dto.getPlatformCode(), member).setScale(2, RoundingMode.DOWN);
      gameBalanceVO.setBalance(amount);
    } catch (Exception e) {
      log.error("查询失败：{}", e.getMessage());
      gameBalanceVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameBalanceVO.setErrorMsg("游戏余额查询失败");
    }

    return gameBalanceVO;
  }

  /** 转账到游戏 */
  @PostMapping(value = "/transferToGame")
  public void transferToGame(@RequestBody OperGameTransferRecordDTO record) throws Exception {
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
  /** 回收单个游戏金额 */
  @PostMapping(value = "/recyclingAmount")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'回收会员游戏金额，账号='+#dto.account")
  public GameRecycleVO recyclingAmount(@RequestBody GameBalanceQueryDTO dto) {
    GameRecycleVO gameRecycleVO = new GameRecycleVO();
    Assert.notEmpty(dto.getAccount(), "会员账号不能为空");
    Assert.notEmpty(dto.getPlatformCode(), "游戏平台编码不能为空");
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在，请重新检查");
    gameRecycleVO.setPlatformCode(dto.getPlatformCode());
    gameRecycleVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
    try {
      BigDecimal amount =
          gameAdminService.getBalance(dto.getPlatformCode(), member).setScale(2, RoundingMode.DOWN);
      gameRecycleVO.setBalance(amount);
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        gameAdminService.transfer(
            dto.getPlatformCode(), TransferTypesEnum.SELF.getCode(), amount, member, false);
      } else {
        gameRecycleVO.setStatus(ResultStatusEnum.WARNING.getValue());
        gameRecycleVO.setErrorMsg(dto.getPlatformCode() + "游戏余额不足，忽略操作");
        log.info("回收玩家{}在{}平台的余额，余额为{}，跳过处理。", member.getAccount(), dto.getPlatformCode(), amount);
      }
    } catch (Exception e) {
      log.error("回收失败：{}", e.getMessage());
      gameRecycleVO.setStatus(ResultStatusEnum.FAILED.getValue());
      gameRecycleVO.setErrorMsg("游戏余额回收失败");
    }
    return gameRecycleVO;
  }

  /** 没收游戏余额 */
  @RequestMapping(value = "/confiscated", method = RequestMethod.POST)
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'没收会员游戏金额，账号='+#dto.account")
  public GameConfiscatedVO confiscated(@RequestBody GameBalanceQueryDTO dto) {
    GameConfiscatedVO gameConfiscatedVO = new GameConfiscatedVO();
    Assert.notEmpty(dto.getAccount(), "会员账号不能为空");
    Assert.notEmpty(dto.getPlatformCode(), "游戏平台编码不能为空");
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.notNull(member, "会员账号不存在，请重新检查");
    gameConfiscatedVO.setPlatformName(dto.getPlatformCode());
    gameConfiscatedVO.setStatus(ResultStatusEnum.SUCCESS.getValue());
    try {
      // 先获取游戏余额
      BigDecimal amount =
          gameAdminService.getBalance(dto.getPlatformCode(), member).setScale(2, RoundingMode.DOWN);
      gameConfiscatedVO.setBalance(amount);
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        gameAdminService.confiscated(dto.getPlatformCode(), member, amount);
      } else {
        gameConfiscatedVO.setErrorMsg(dto.getPlatformCode() + "游戏余额不足，忽略操作");
        gameConfiscatedVO.setStatus(ResultStatusEnum.WARNING.getValue());
        log.info("没收玩家{}在{}平台的余额，余额为{}，跳过处理。", member.getAccount(), dto.getPlatformCode(), amount);
      }
    } catch (Exception e) {
      log.info(e.getMessage());
      gameConfiscatedVO.setErrorMsg("没收会员游戏金额失败");
      gameConfiscatedVO.setStatus(ResultStatusEnum.FAILED.getValue());
    }
    return gameConfiscatedVO;
  }

  /** 没收所有游戏平台余额 */
  @PostMapping(value = "/confiscatedGameByAccount")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'没收会员游戏金额，账号='+#dto.account")
  public List<GameConfiscatedVO> confiscatedGameByAccount(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.confiscatedGameByAccount(dto.getAccount());
  }

  /** 查询所有平台余额 一键查询 */
  @PostMapping(value = "/selectGameBalanceByAccount")
  public List<GameBalanceVO> selectGameBalanceByAccount(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.selectGameBalanceByAccount(dto.getAccount());
  }

  /** 一键回收 */
  @PostMapping(value = "/recyclingAmountByAccount")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'回收会员游戏金额，account='+#dto.account")
  public List<GameRecycleVO> reclaimLiveAmountAsync(@RequestBody GameBalanceQueryDTO dto) {
    return gameAdminService.recyclingAmountByAccount(dto.getAccount());
  }
}
