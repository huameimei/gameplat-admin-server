package com.gameplat.admin.service.game.api.ae.feign;


import com.gameplat.admin.service.game.config.FeignFormConfig;
import com.gameplat.common.game.api.ae.bean.AeCheckTransferOperationReq;
import com.gameplat.common.game.api.ae.bean.AeCheckTransferOperationResp;
import com.gameplat.common.game.api.ae.bean.AeCreateMemberReq;
import com.gameplat.common.game.api.ae.bean.AeDepositReq;
import com.gameplat.common.game.api.ae.bean.AeDepositResp;
import com.gameplat.common.game.api.ae.bean.AeGetBalanceRep;
import com.gameplat.common.game.api.ae.bean.AeGetBalanceReq;
import com.gameplat.common.game.api.ae.bean.AeLoginAndLaunchRep;
import com.gameplat.common.game.api.ae.bean.AeLoginAndLaunchReq;
import com.gameplat.common.game.api.ae.bean.AeResponse;
import com.gameplat.common.game.api.ae.bean.AeWithdrawReq;
import com.gameplat.common.game.api.ae.bean.AeWithdrawResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "aeFeignClient",
    url = "https://tttint.onlinegames22.com",
//    url = "${public.proxy.host}",
//    path ="/ae/",
    configuration = FeignFormConfig.class)
public interface AeFeignClient {

  @PostMapping(value = "/wallet/getBalance", produces = MediaType.APPLICATION_JSON_VALUE)
  AeGetBalanceRep getBalance(@SpringQueryMap AeGetBalanceReq req);


  @PostMapping(value = "/wallet/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
  AeWithdrawResp withdraw(@SpringQueryMap AeWithdrawReq req);


  @PostMapping(value = "/wallet/deposit", produces = MediaType.APPLICATION_JSON_VALUE)
  AeDepositResp deposit(@SpringQueryMap AeDepositReq req);


  @PostMapping(value = "/wallet/checkTransferOperation", produces = MediaType.APPLICATION_JSON_VALUE)
  AeCheckTransferOperationResp checkTransferOperation(
      @SpringQueryMap AeCheckTransferOperationReq req);


  @PostMapping(value = "/wallet/createMember", produces = MediaType.APPLICATION_JSON_VALUE)
  AeResponse createMember(@SpringQueryMap AeCreateMemberReq req);

  @PostMapping(value = "/wallet/doLoginAndLaunchGame", produces = MediaType.APPLICATION_JSON_VALUE)
  AeLoginAndLaunchRep doLoginAndLaunchGame(@SpringQueryMap AeLoginAndLaunchReq req);


}
