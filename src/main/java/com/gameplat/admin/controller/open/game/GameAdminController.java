package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.redis.api.RedisService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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

  @Autowired private GamePlatformService gamePlatformService;

  /** 查询单个真人平台余额 */
  @GetMapping(value = "/selectGameBalance")
  public GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto) throws Exception {
    GameBalanceVO gameBalanceVO = new GameBalanceVO();
    gameBalanceVO.setPlatformCode(dto.getPlatform().get("platformCode"));
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.isNull(member, "会员账号不存在");
    gameBalanceVO.setBalance(
        gameAdminService.getBalance(dto.getPlatform().get("platformCode"), member));
    return gameBalanceVO;
  }

  /** 转账到真人 */
  @PostMapping(value = "/transferToGame")
  public void transferToGame(@RequestBody OperGameTransferRecordDTO record) throws Exception {
    String key = "self_" + record.getPlatformCode() + '_' + record.getAccount();
    try {
      redisService.getStringOps().setEx(key, "game_transfer", 3, TimeUnit.MINUTES);
      Member member = memberService.getMemberAndFillGameAccount(record.getAccount());
      Assert.isNull(member, "会员账号不存在");
      gameAdminService.transferOut(record.getPlatformCode(), record.getAmount(), member, false);
    } finally {
      redisService.getKeyOps().delete(key);
    }
  }

  /** 回收金额 */
  @PostMapping(value = "/recyclingAmount")
  public Map<String, String> recyclingAmount(@RequestBody GameBalanceQueryDTO dto) {
    Map<String, String> map = new HashMap();
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

  /** 没收真人余额 */
  @RequestMapping(value = "/confiscated", method = RequestMethod.POST)
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'没收会员游戏金额，id='+#memberInfo.id")
  public Map<String, String> confiscated(@RequestBody GameBalanceQueryDTO dto) throws Exception {
    Map<String, String> map = new HashMap();
    Member member = memberService.getMemberAndFillGameAccount(dto.getAccount());
    Assert.isNull(member, "会员账号不存在");
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

  /** 查询所有真人游戏平台余额 */
  @GetMapping(value = "/selectGameAllBalance")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'查询会员游戏金额，id='+#memberInfo.id")
  public Map<String, BigDecimal> selectGameAllBalance(MemberInfo memberInfo) {
    Member member = memberService.getById(memberInfo.getMemberId());
    Member memberAccount = memberService.getMemberAndFillGameAccount(member.getAccount());
    Assert.isNull(memberAccount, "会员账号不存在");
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

  /** 回收金额 */
  @PostMapping(value = "/recyclingAmountByAccount")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'回收会员游戏金额，account='+#dto.account")
  public Map<String, Object> recyclingAmountByAccount(@RequestBody GameBalanceQueryDTO dto) {
    Map<String, Object> map = new HashMap();
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
        // 记录日志
        StringBuffer buffer = new StringBuffer();
        buffer.append("真人额度回收:会员账号:" + dto.getAccount() + ",");
        buffer.append("真人类型：" + platformCode + ",");
        buffer.append("金额：" + transferMoney);
        log.info(buffer.toString());
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

  /** 查询单个真人平台余额 */
  @PostMapping(value = "/selectGameBalanceByAccount")
  public Map<String, Object> selectGameBalanceByAccount(@RequestBody GameBalanceQueryDTO dto)
      throws Exception {
    Map<String, Object> map = new HashMap();
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
}
