package com.gameplat.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@Service
public class RoutingDelegate {

  public ResponseEntity<String> redirect(
      HttpServletRequest request, HttpServletResponse response, String routeUrl, String prefix) {
    try {
      String redirectUrl = createRedirectUrl(request, routeUrl, prefix);
      RequestEntity requestEntity = createRequestEntity(request, redirectUrl);
      return route(requestEntity);
    } catch (Exception e) {
      log.error("redirect error");
      return new ResponseEntity<>("REDIRECT ERROR", HttpStatus.INSUFFICIENT_STORAGE);
    }
  }

  public ResponseEntity<String> redirect(
      HttpServletRequest request, Map<String, String> addHeaders, String routeUrl, String prefix) {
    try {
      String redirectUrl = createRedirectUrl(request, routeUrl, prefix);
      redirectUrl = redirectUrl.split("\\?")[0];
      RequestEntity requestEntity = createRequestEntity(request, addHeaders, redirectUrl);
      return route(requestEntity);
    } catch (Exception e) {
      log.error("redirect error");
      return new ResponseEntity<>("REDIRECT ERROR", HttpStatus.INSUFFICIENT_STORAGE);
    }
  }

  private String createRedirectUrl(HttpServletRequest request, String routeUrl, String prefix) {
    String queryString = request.getQueryString();
    return routeUrl
        + request.getRequestURI().replace(prefix, "/api-manage/manager")
        + (queryString != null ? "?" + queryString : "");
  }

  private RequestEntity createRequestEntity(HttpServletRequest request, String url)
      throws URISyntaxException, IOException {
    String method = request.getMethod();
    HttpMethod httpMethod = HttpMethod.resolve(method);
    MultiValueMap<String, String> headers = parseRequestHeader(request, null);
    MultiValueMap<String, String> body = parseRequestParams(request);
    return new RequestEntity(body, headers, httpMethod, new URI(url));
  }

  private RequestEntity createRequestEntity(
      HttpServletRequest request, Map<String, String> addHeaders, String url)
      throws URISyntaxException, IOException {
    String method = request.getMethod();
    HttpMethod httpMethod = HttpMethod.resolve(method);
    MultiValueMap<String, String> headers = parseRequestHeader(request, addHeaders);
    MultiValueMap<String, String> body = parseRequestParams(request);
    return new RequestEntity(body, headers, httpMethod, new URI(url));
  }

  private ResponseEntity<String> route(RequestEntity requestEntity) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(requestEntity, String.class);
  }

  public MultiValueMap<String, String> parseRequestParams(HttpServletRequest request) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    Map<String, String[]> map = request.getParameterMap();
    for (Iterator<Entry<String, String[]>> itr = map.entrySet().iterator(); itr.hasNext(); ) {
      Entry<String, String[]> entry = itr.next();
      String key = entry.getKey();
      String[] value = entry.getValue();
      params.put(key, Arrays.asList(value));
    }
    return params;
  }

  private MultiValueMap<String, String> parseRequestHeader(
      HttpServletRequest request, Map<String, String> addHeaders) {
    HttpHeaders headers = new HttpHeaders();
    List<String> headerNames = Collections.list(request.getHeaderNames());
    for (String headerName : headerNames) {
      List<String> headerValues = Collections.list(request.getHeaders(headerName));
      for (String headerValue : headerValues) {
        headers.add(headerName, headerValue);
      }
    }
    if (addHeaders != null && !addHeaders.isEmpty()) {
      addHeaders.forEach(headers::add);
    }
    return headers;
  }
}
