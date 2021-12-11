package com.gameplat.admin.service.live.api.ae;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.gameplat.admin.service.live.GameApi;
import com.gameplat.admin.service.live.api.ae.bean.AeCheckTransferOperationReq;
import com.gameplat.admin.service.live.api.ae.bean.AeCheckTransferOperationResp;
import com.gameplat.admin.service.live.api.ae.bean.AeConfig;
import com.gameplat.admin.service.live.api.ae.bean.AeCreateMemberReq;
import com.gameplat.admin.service.live.api.ae.bean.AeDepositReq;
import com.gameplat.admin.service.live.api.ae.bean.AeDepositResp;
import com.gameplat.admin.service.live.api.ae.bean.AeLoginAndLaunchRep;
import com.gameplat.admin.service.live.api.ae.bean.AeLoginAndLaunchReq;
import com.gameplat.admin.service.live.api.ae.bean.AeResponse;
import com.gameplat.admin.service.live.api.ae.bean.AeGetBalanceRep;
import com.gameplat.admin.service.live.api.ae.bean.AeGetBalanceReq;
import com.gameplat.admin.service.live.api.ae.bean.AeWithdrawReq;
import com.gameplat.admin.service.live.api.ae.bean.AeWithdrawResp;
import com.gameplat.admin.service.live.api.ae.enums.API;
import com.gameplat.admin.service.live.api.ae.enums.AeStatus;
import com.gameplat.admin.service.live.api.ae.enums.WithdrawType;
import com.gameplat.admin.service.live.api.ae.feign.AeFeignClient;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.live.TransferResource;
import com.gameplat.common.live.exception.LiveException;
import com.gameplat.common.live.exception.LiveNoRollbackTransferException;
import com.gameplat.common.live.exception.LiveTimeOutException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component(AeApi.LIVE_CODE + "Api")
public class AeApi implements GameApi {

  public static final String LIVE_CODE = "ae";

  public static final String LIVE_NAME = "AE真人";

  private static final String DEFAULT_GAME_TYPE = "LIVE";

  private static final String DEFAULT_GAME_PLATFORM = "SEXYBCRT";

  private static final String BET_LIMIT_ID = "limitId";

  private static final String DEFAULT_LANGUAGE = "cn";

  private static final int RETRY_DELAY = 1000;

  private static final int[] DEFAULT_BET_LIMIT_IDS = {
      260301, 260302, 260303, 260304, 260305, 260306
  };

  @Autowired
  private AeConfig aeConfig;

  @Autowired
  private AeFeignClient aeFeignClient;

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = RETRY_DELAY))
  public BigDecimal getBalance(String account) throws Exception {
    AeGetBalanceReq aeGetBalanceReq = AeGetBalanceReq.builder()
        .cert(aeConfig.getCert())
        .agentId(aeConfig.getAgentId())
        .userIds(aeConfig.getAccount(account))
        .build();
    AeGetBalanceRep aeGetBalanceRep = aeFeignClient.getBalance(aeGetBalanceReq);
    log.info("AE请求获取余额，参数:{}，响应:{}", JSONUtil.toJsonStr(aeGetBalanceReq),
        JSONUtil.toJsonStr(aeGetBalanceRep));
    if (AeStatus.SUCCESS.getValue().equals(aeGetBalanceRep.getStatus())) {
      return aeGetBalanceRep.getResults().stream()
          .findFirst()
          .map(AeGetBalanceRep.Result::getBalance)
          .map(NumberUtil::toBigDecimal)
          .orElse(BigDecimal.ZERO);
    } else {
      throw new LiveException(aeGetBalanceRep.getStatus(), "", API.GET_BALANCE.getUrl(),
          JSONUtil.toJsonStr(aeGetBalanceReq), aeGetBalanceRep);
    }
  }

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = RETRY_DELAY))
  public TransferResource transfer(String account, BigDecimal amount, String orderNum)
      throws Exception {
    return amount.compareTo(BigDecimal.ZERO) > 0
        ? this.deposit(account, amount, orderNum)
        : this.withdraw(account, amount, orderNum);
  }

  @Override
  public String free(String gameType, String ip, Boolean isMobile, String url) throws Exception {
    throw new ServiceException(LIVE_NAME + "不支持试玩");
  }

  @Override
  public String play(String account, String gameCode, String ip, Boolean isMobile, String url)
      throws Exception {
    //检查或创建用户
    this.createMember(account);
    AeLoginAndLaunchReq params = AeLoginAndLaunchReq.builder()
        .cert(aeConfig.getCert())
        .agentId(aeConfig.getAgentId())
        .userId(aeConfig.getAccount(account))
        .gameCode(gameCode)
        .gameType(DEFAULT_GAME_TYPE)
        .platform(DEFAULT_GAME_PLATFORM)
        .externalURL(url)
        .language(DEFAULT_LANGUAGE)
        .isMobileLogin(isMobile.toString()).build();
    try {
      AeLoginAndLaunchRep aeLoginAndLaunchRep = aeFeignClient.doLoginAndLaunchGame(params);
      log.info("AE请求登录启动游戏，参数:{}，响应:{}", JSONUtil.toJsonStr(params),
          JSONUtil.toJsonStr(aeLoginAndLaunchRep));
      if (AeStatus.SUCCESS.getValue().equals(aeLoginAndLaunchRep.getStatus())) {
        return aeLoginAndLaunchRep.getUrl();
      } else {
        throw new LiveException(aeLoginAndLaunchRep.getStatus(), "", API.LOGIN_AND_LAUNCH.getUrl(),
            JSONUtil.toJsonStr(params), aeLoginAndLaunchRep);
      }
    } catch (LiveException e) {
      log.error("跳转AE发生异常:{}", e.getDesc());
      if (AeStatus.ACCOUNT_NOT_EXISTS.getValue().equals(e.getCode())) {
        this.createMember(account);
      }
      throw e;
    }
  }

  @Override
  public void isOpen() throws Exception {
    if (!aeConfig.isOpen()) {
      throw new ServiceException("游戏未接入");
    }
  }

  /**
   * 转入
   *
   * @param account  String
   * @param amount   Double
   * @param orderNum String
   * @return TransferResource
   * @throws Exception Exception
   */
  private TransferResource deposit(String account, BigDecimal amount, String orderNum)
      throws Exception {
    //检查或创建用户
    this.createMember(account);
    AeDepositReq aeDepositReq = AeDepositReq.builder()
        .cert(aeConfig.getCert())
        .agentId(aeConfig.getAgentId())
        .userId(aeConfig.getAccount(account))
        .txCode(orderNum)
        .transferAmount(amount.toString())
        .build();
    try {
      AeDepositResp aeDepositResp = aeFeignClient.deposit(aeDepositReq);
      log.info("AE请求转入，参数:{}，响应:{}", JSONUtil.toJsonStr(aeDepositReq),
          JSONUtil.toJsonStr(aeDepositResp));
      if (AeStatus.SUCCESS.getValue().equals(aeDepositResp.getStatus())) {
        TransferResource result = new TransferResource();
        result.setOrderNo(orderNum);
        return result;
      } else {
        throw new LiveException(aeDepositResp.getStatus(), aeDepositResp.getDesc(),
            API.TRANSER_IN.getUrl(),
            JSONUtil.toJsonStr(aeDepositReq), aeDepositResp);
      }
    } catch (LiveException e) {
      log.error("AE转入余额异常:{}", e.getDesc());
      if (AeStatus.TXCODE_ALREADY_OPERATION.getValue().equals(e.getCode())) {
        throw new LiveNoRollbackTransferException(e);
      }
      if (queryOrderStatus(orderNum, account, amount)) {
        return new TransferResource();
      }
    }
    return new TransferResource();
  }

  /**
   * 转出
   *
   * @param account  String
   * @param amount   Double
   * @param orderNum String
   * @return TransferResource
   * @throws Exception Exception
   */
  private TransferResource withdraw(String account, BigDecimal amount, String orderNum)
      throws Exception {
    //检查或创建用户
    this.createMember(account);
    AeWithdrawReq aeWithdrawReq =
        AeWithdrawReq.builder().cert(aeConfig.getCert())
            .agentId(aeConfig.getAgentId())
            .userId(aeConfig.getAccount(account))
            .txCode(orderNum)
            .transferAmount(String.valueOf(Math.abs(amount.intValue())))
            .withdrawType(WithdrawType.PARTIAL.getValue())
            .build();
    try {
      AeWithdrawResp aeWithdrawResp = aeFeignClient.withdraw(aeWithdrawReq);
      log.info("AE请求转出，参数:{}，响应:{}", JSONUtil.toJsonStr(aeWithdrawReq),
          JSONUtil.toJsonStr(aeWithdrawResp));
      if (AeStatus.SUCCESS.getValue().equals(aeWithdrawResp.getStatus())) {
        TransferResource result = new TransferResource();
        result.setOrderNo(orderNum);
        return result;
      } else {
        throw new LiveException(aeWithdrawResp.getStatus(), "",
            API.TRANSER_OUT.getUrl(),
            JSONUtil.toJsonStr(aeWithdrawReq), aeWithdrawResp);
      }
    } catch (LiveException e) {
      log.error("AE转出余额异常:{}", e.getDesc());
      if (AeStatus.INVALID_USER_ID.getValue().equals(e.getCode())) {
        this.createMember(account);
      } else if (AeStatus.TXCODE_ALREADY_OPERATION.getValue().equals(e.getCode())) {
        throw new LiveNoRollbackTransferException(e);
      }
      if (queryOrderStatus(orderNum, account, amount)) {
        return new TransferResource();
      }
    }
    return new TransferResource();
  }


  /**
   * 创建账号
   *
   * @throws Exception Exception
   */

  public void createMember(String account) throws Exception {
    Map<String, Object> limitId = new LinkedHashMap<>();
    limitId.put(BET_LIMIT_ID, DEFAULT_BET_LIMIT_IDS);

    Map<String, Object> betLimitLive = new LinkedHashMap<>();
    betLimitLive.put(DEFAULT_GAME_TYPE, limitId);

    Map<String, Object> betLimit = new LinkedHashMap<>();
    betLimit.put(DEFAULT_GAME_PLATFORM, betLimitLive);

    AeCreateMemberReq aeCreateMemberReq = AeCreateMemberReq.builder()
        .cert(aeConfig.getCert())
        .agentId(aeConfig.getAgentId())
        .userId(aeConfig.getAccount(account))
        .currency(aeConfig.getCurrency())
        .betLimit(JSONUtil.toJsonStr(betLimit))
        .language(DEFAULT_LANGUAGE)
        .build();

    int retryCount = 0;
    while (true) {
      try {
        AeResponse aeResponse = aeFeignClient.createMember(aeCreateMemberReq);
        log.info("AE请求创建会员，参数:{}，响应:{}", JSONUtil.toJsonStr(aeCreateMemberReq),
            JSONUtil.toJsonStr(aeResponse));
        if (AeStatus.SUCCESS.getValue().equals(aeResponse.getStatus())) {
          return;
        } else if (AeStatus.ACCOUNT_EXISTED.getValue().equals(aeResponse.getStatus())) {
          log.info("AE请求创建会员，账号已经存在");
          return;
        } else {
          throw new LiveException(aeResponse.getStatus(), aeResponse.getDesc(), "",
              JSONUtil.toJsonStr(aeCreateMemberReq), aeResponse);
        }
      } catch (Exception e) {
        retryCount++;
        log.error("AE创建会员发生异常:{}", e.getMessage());
        if (retryCount > WHILE_COUNT) {
          throw new LiveTimeOutException(e.getMessage(), API.CREATE_MEMBER.getUrl(),
              JSONUtil.toJsonStr(aeCreateMemberReq));
        }
        Thread.sleep(SLEEP_TIME);
      }
    }
  }

  @Override
  public boolean queryOrderStatus(String orderNum, String account, BigDecimal amount) throws Exception {
    AeCheckTransferOperationReq aeCheckTransferOperationReq =
        AeCheckTransferOperationReq.builder().cert(aeConfig.getCert())
            .agentId(aeConfig.getAgentId())
            .txCode(orderNum)
            .build();
    int retryCount = 0;
    while (true) {
      try {
        AeCheckTransferOperationResp response = aeFeignClient
            .checkTransferOperation(aeCheckTransferOperationReq);
        log.info("AE请求查询交易订单信息，参数:{}，响应:{}", JSONUtil.toJsonStr(aeCheckTransferOperationReq),
            JSONUtil.toJsonStr(response));
        if (AeStatus.SUCCESS.getValue().equals(response.getStatus())) {
          // 交易类型 0:转帐失败   1:转帐成功
          if ("1".equals(response.getTxStatus())) {
            return true;
          }
          return false;
        } else {
          throw new LiveException(response.getStatus(), response.getDesc(),
              API.CHECK_TRANSFER_OPERATION.getUrl(),
              JSONUtil.toJsonStr(aeCheckTransferOperationReq), response);
        }
      } catch (Exception e) {
        retryCount++;
        if (retryCount > WHILE_COUNT) {
          log.error("[AE]转账是否成功查询，异常：", e);
          throw new LiveTimeOutException(e.getMessage(), "queryOrderStatus", orderNum);
        }
        Thread.sleep(SLEEP_TIME);
      }
    }
  }
}
