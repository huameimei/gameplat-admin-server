package com.gameplat.admin.controller.open.finance;


import com.gameplat.admin.model.bean.NameValuePair;
import com.gameplat.admin.model.bean.UserEquipment;
import com.gameplat.admin.service.ProxyPayService;
import com.gameplat.admin.util.WebUtils;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/admin/finance/asyncCallback")
public class WithdrawAsyncCallbackController {

  @Autowired
  private ProxyPayService proxyPayService;

  @RequestMapping("/onlineProxyPayAsyncCallback/{cashOrderNo}")
  @ResponseBody
  public void onlineProxyPayAsyncCallback(
      @PathVariable String cashOrderNo,
      @RequestBody(required = false) String requestBody,
      HttpServletRequest request, HttpServletResponse response,
      String userAgentString,
      UserAgent clientUserAgent)
      throws Exception {
    String url = WebUtils.getCurUrl(request);
    //获取请求头数据
    List<NameValuePair> headers = getHeaders(request);
    //获取请求来源服务器数据
    UserEquipment clientInfo = UserEquipment
        .create(userAgentString, clientUserAgent, request);
    // 获取请求数据
    Map<String, String> requestParameters = getRequestParameterMap(request);
    String msg = proxyPayService.proxyPayAsyncCallback(
        cashOrderNo, url, request.getMethod(), headers, clientInfo.getIpAddress(),
        requestParameters, requestBody);
    response.getWriter().print(msg);
  }

  /**
   * 获取请求头数据
   */
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

  /**
   * 获取请求数据
   */
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
