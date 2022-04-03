package com.gameplat.admin.controller.open.finance;

import com.gameplat.admin.model.bean.ReturnMessage;
import com.gameplat.admin.service.ProxyPayService;
import com.gameplat.base.common.util.ServletUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.UserEquipment;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/api/admin/finance/proxyPay")
public class ProxyPayController {

  @Autowired
  private ProxyPayService proxyPayService;
  @Autowired
  private DistributedLocker distributedLocker;

  @SneakyThrows
  @PostMapping("/relProxyPay")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:relProxyPay')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'第三方代付出款商户:'#ppMerchantId + ',订单id:' #id ")
  public void proxyPay(
      Long id,
      Long ppMerchantId,
      HttpServletRequest request) {
    String lock_key = "recharge_rw_" + id;
    distributedLocker.lock(lock_key);
    String sysPath = request.getSession().getServletContext().getRealPath("");
    String urL = ServletUtils.getRequestDomain(request);
    String scheme = request.getHeader("X-Forwarded-Scheme");
    UserCredential userCredential = SecurityUserHolder.getCredential();
    String realUrl;
    if (StringUtils.isNotBlank(scheme) && "https".equals(scheme)) {
      realUrl = urL.replace("http:", "https:");
    } else {
      realUrl = urL;
    }
    try {
      proxyPayService.proxyPay(id, ppMerchantId, realUrl, sysPath, userCredential);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      distributedLocker.unlock(lock_key);
    }
  }

  @PostMapping("/queryProxyOrder")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:queryProxyOrder')")
  public ReturnMessage queryProxyOrder(Long id, Long ppMerchantId) throws Exception {
    return proxyPayService.queryProxyOrder(id, ppMerchantId);
  }
}
