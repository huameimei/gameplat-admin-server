package com.gameplat.admin.controller.open.finance;


import com.gameplat.admin.model.bean.ReturnMessage;
import com.gameplat.admin.service.ProxyPayService;
import com.gameplat.common.util.ServletUtils;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/finance/proxyPay")
public class ProxyPayController {

  @Autowired
  private ProxyPayService proxyPayService;

  @PostMapping("/relProxyPay")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:relProxyPay')")
  public void proxyPay(@NotNull(message = "{NoNull}") Long id,
      @NotNull(message = "{NoNull}") Long ppMerchantId, HttpServletRequest request)
      throws Exception {
    String sysPath = request.getSession().getServletContext().getRealPath("");
    String urL = ServletUtils.getRequestDomain(request);
    String realUrl = "";
    String scheme = request.getHeader("X-Forwarded-Scheme");
    UserCredential userCredential = SecurityUserHolder.getCredential();
    if (StringUtils.isNotBlank(scheme) && scheme.equals("https")) {
      realUrl = urL.replace("http:", "https:");
    } else {
      realUrl = urL;
    }
    proxyPayService.proxyPay(id, ppMerchantId, realUrl, sysPath, userCredential);
  }

  @PostMapping("/queryProxyOrder")
  @PreAuthorize("hasAuthority('finance:memberWithdraw:queryProxyOrder')")
  public ReturnMessage queryProxyOrder(Long id, Long ppMerchantId) throws Exception {
    return proxyPayService.queryProxyOrder(id, ppMerchantId);
  }

}
