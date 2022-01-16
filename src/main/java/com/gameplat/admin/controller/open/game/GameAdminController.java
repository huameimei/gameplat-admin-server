package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.model.dto.GameBalanceQueryDTO;
import com.gameplat.admin.model.dto.OperGameTransferRecordDTO;
import com.gameplat.admin.model.vo.GameBalanceVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameAdminService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.redis.api.RedisService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game")
public class GameAdminController {

  @Resource
  private GameAdminService gameAdminService;

  @Resource
  private MemberService memberService;

  @Resource
  private RedisService redisService;

  @Resource
  private GamePlatformService gamePlatformService;

  /**
   * 查询单个真人平台余额
   */
  @GetMapping(value = "/selectGameBalance")
  public GameBalanceVO selectGameBalance(GameBalanceQueryDTO dto) throws Exception {
    GameBalanceVO gameBalanceVO = new GameBalanceVO();
    gameBalanceVO.setLiveType(dto.getPlatform().get("liveType"));
    Member member = memberService.getByAccount(dto.getAccount()).orElse(null);
    if (member == null) {
      throw new ServiceException("会员账号不存在");
    }
    gameBalanceVO
        .setBalance(gameAdminService.getBalance(dto.getPlatform().get("liveType"), member));
    return gameBalanceVO;
  }

  /**
   * 转账到真人
   */
  @PostMapping(value = "/transferToGame")
  public void transferToGame(@RequestBody OperGameTransferRecordDTO record) throws Exception {
    String key = "self_" + record.getPlatformCode() + '_' + record.getAccount();
    try {
      redisService.getStringOps().setEx(key, "game_transfer", 3, TimeUnit.MINUTES);
      Member member = memberService.getByAccount(record.getAccount()).orElse(null);
      if (member == null) {
        throw new ServiceException("会员账号不存在");
      }
      gameAdminService.transferOut(record.getPlatformCode(), record.getAmount(), member, true);
    } finally {
      redisService.getKeyOps().delete(key);
    }
  }

  /**
   * 回收金额
   */
  @PostMapping(value = "/recyclingAmount")
  public Map<String, String> recyclingAmount(@RequestBody GameBalanceQueryDTO dto) {
    Map<String, String> map = new HashMap();
    Member member = memberService.getByAccount(dto.getAccount()).orElse(null);
    if (member == null) {
      throw new ServiceException("会员账号不存在");
    }
    dto.getPlatform()
        .forEach((key, value) -> {
              try {
                if (StringUtils.isNotBlank(value) && (int) Double.parseDouble(value) >= 1) {
                  gameAdminService
                      .transfer(key, TransferTypesEnum.SELF.getCode(), new BigDecimal(value), member,
                          false);
                  dto.getPlatform().put(key, (int) Double.parseDouble(value) + "");
                } else {
                  dto.getPlatform().put(key, "0");
                  map.put(key, "金额必须大于或等于1");
                }
              } catch (Exception e) {
                log.info(e.getMessage());
                map.put(key, e.getMessage());
              }
            }
        );
    return map;
  }


  /**
   * 没收真人余额
   */
  @RequestMapping(value = "/confiscated", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> confiscated(@RequestBody GameBalanceQueryDTO dto) throws Exception {
    Map<String, String> map = new HashMap();
    Member member = memberService.getByAccount(dto.getAccount()).orElse(null);
    if (member == null) {
      throw new ServiceException("会员账号不存在");
    }
    dto.getPlatform()
        .forEach((key, value) -> {
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


  /**
   * 查询所有真人游戏平台余额
   */
  @GetMapping(value = "/selectGameAllBalance")
  public Map<String, BigDecimal> selectGameAllBalance(MemberInfo memberInfo) {
    Member member = memberService.getById(memberInfo.getMemberId());
    if (member == null) {
      throw new ServiceException("会员账号不存在");
    }
    Map<String, BigDecimal> map = new HashMap<>();
    List<GamePlatform> gamePlatformList = gamePlatformService.queryByTransfer();
    if (!CollectionUtils.isEmpty(gamePlatformList)) {
      gamePlatformList.stream().parallel().forEach(gamePlatform -> {
        try {
          map.put(gamePlatform.getCode(), gameAdminService.getBalance(gamePlatform.getCode(), member));
        } catch (Exception e) {
          log.info(gamePlatform.getCode() + "游戏查询异常：", e);
          map.put(gamePlatform.getCode(), BigDecimal.ZERO);
        }
      });
    }
    return map;
  }

  /**
   * 回收金额
   */
  @PostMapping(value = "/recyclingAmountByAccount")
  public Map<String, Object> recyclingAmountByAccount(@RequestBody GameBalanceQueryDTO dto) throws Exception {
    Map<String, Object> map = new HashMap();
    if (null == dto.getPlatform() || null == dto.getPlatform().get("liveType")) {
      map.put("errorCode", "真人类型不正确");
      return map;
    }
    if (StringUtils.isBlank(dto.getAccount())) {
      map.put("errorCode", "会员账号不能为空");
      return map;
    }
    Member member = memberService.getByAccount(dto.getAccount()).orElse(null);
    if (null == member) {
      map.put("errorCode", "会员账号不存在，请重新检查");
      return map;
    }
    String liveCode = dto.getPlatform().get("liveType");
    try {
      BigDecimal money = gameAdminService.getBalance(liveCode, member);
      if (null != money && money.compareTo(BigDecimal.ONE) > 0) {
        BigDecimal transferMoney = new BigDecimal((int) Double.parseDouble(money.toString()));
        gameAdminService
            .transfer(liveCode, TransferTypesEnum.SELF.getCode(), transferMoney, member, false);
        map.put("balance", transferMoney);
        //记录日志
        StringBuffer log = new StringBuffer();
        log.append("真人额度回收:会员账号:" + dto.getAccount() + ",");
        log.append("真人类型：" + liveCode + ",");
        log.append("金额：" + transferMoney);
        // TODO 日志记录
      } else {
        map.put("errorCode", "回收金额必须大于或等于1");
        return map;
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "获取真人信息错误，请联系客服查看");
    }
    return map;
  }

  /**
   * 查询单个真人平台余额
   */
  @PostMapping(value = "/selectGameBalanceByAccount")
  public Map<String,Object> selectGameBalanceByAccount(@RequestBody GameBalanceQueryDTO dto) throws Exception{
    Map<String,Object> map = new HashMap();
    if(null == dto.getPlatform() || null == dto.getPlatform().get("liveType")){
      map.put("errorCode","真人类型不正确");
      return map;
    }
    if(StringUtils.isBlank(dto.getAccount())){
      map.put("errorCode","会员账号不能为空");
      return map;
    }
    Member member = memberService.getByAccount(dto.getAccount()).orElse(null);
    if(null == member) {
      map.put("errorCode","会员账号不存在，请重新检查");
      return map;
    }
    try{
      map.put("liveType",dto.getPlatform().get("liveType"));
      map.put("balance",gameAdminService.getBalance(dto.getPlatform().get("liveType"),member));
    }catch (Exception e){
      e.printStackTrace();
      map.put("errorCode","获取真人信息错误，请联系客服查看");
    }
    return map;
  }
}