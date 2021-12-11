package com.gameplat.admin.service.live.api.ae.feign;


import com.gameplat.admin.service.live.api.ae.bean.AeCheckTransferOperationReq;
import com.gameplat.admin.service.live.api.ae.bean.AeCheckTransferOperationResp;
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
import com.gameplat.admin.service.live.config.FeignFormConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "aeFeignClient",
    url = "https://tttint.onlinegames22.com",
    configuration = FeignFormConfig.class)
public interface AeFeignClient {

  @PostMapping(value = "/wallet/getBalance",
      headers = HttpHeaders.ACCEPT+"="+MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE ,
      produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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
