package com.gameplat.admin.controller.open.finance;

import com.gameplat.admin.service.ProxyPayService;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.basepay.pay.bean.NameValuePair;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/internal/admin/finance/asyncCallback")
public class WithdrawAsyncCallbackController {

  @Autowired private ProxyPayService proxyPayService;

  @SneakyThrows
  @RequestMapping("/onlineProxyPayAsyncCallback/{cashOrderNo}")
  public void onlineProxyPayAsyncCallback(
      @PathVariable String cashOrderNo,
      @RequestBody(required = false) String requestBody,
      HttpServletRequest request,
      HttpServletResponse response) {
    List<NameValuePair> headers = getHeaders(request);
    Map<String, String> requestParameters = getRequestParameterMap(request);

    String result =
        proxyPayService.proxyPayAsyncCallback(
            cashOrderNo,
            request.getRequestURI(),
            request.getMethod(),
            headers,
            IPUtils.getIpAddress(request),
            requestParameters,
            requestBody);

    response.getWriter().print(result);
  }

  /** 获取请求头数据 */
  private List<NameValuePair> getHeaders(HttpServletRequest request) {
    List<NameValuePair> headers = new ArrayList<>();
    Enumeration<String> e = request.getHeaderNames();
    while (e.hasMoreElements()) {
      String name = e.nextElement();
      Stream.of(StringUtils.split(request.getHeader(name), ";"))
          .forEach(v -> headers.add(new NameValuePair(name, v)));
    }
    return headers;
  }

  /** 获取请求数据 */
  private Map<String, String> getRequestParameterMap(HttpServletRequest request) {
    Map<String, String> parameters = new HashMap<>();
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String parameterName = parameterNames.nextElement();
      parameters.put(parameterName, request.getParameter(parameterName));
    }
    return parameters;
  }
}
