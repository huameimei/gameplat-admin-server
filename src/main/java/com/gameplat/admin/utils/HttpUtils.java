package com.gameplat.admin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/** @describe: http工具类 */
@Slf4j
public class HttpUtils {

  public static final String DEVICE_TYPE_ANDROID = "Android";
  public static final String DEVICE_TYPE_IPHONE = "iPhone";
  public static final String DEVICE_TYPE_IPAD = "iPad";
  public static final String DEVICE_TYPE_MAC = "Mac";
  public static final String DEVICE_TYPE_LINUX = "Linux";
  public static final String DEVICE_TYPE_WINDOWS = "Windows";
  public static final String DEVICE_TYPE_UNKNOWN = "unknown";
  public static final String DEVICE_TYPE_ANDROIDH5 = "native_android";
  public static final String DEVICE_TYPE_IPHONEH5 = "native_ios";
  public static final String DEVICE_TYPE_WUKONG = "Wukong";

  private static final String X_REAL_IP = "X-Real-IP";
  private static final String X_FORWARDED_FOR = "X-Forwarded-For";
  public static final String X_REQUESTED_WITH = "X-Requested-With";
  public static final String CONTENT_TYPE = "text/html;charset=UTF-8";
  public static final String CONTENT_TYPE_NAME = "Content-Type";
  public static final String ENCODING = "utf-8";
  public static final Pattern PATTERN_JSONP = Pattern.compile("^[a-zA-Z_\\$][0-9a-zA-Z_\\$]*$");
  public static final String IP_REG =
      "((?:(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d))\\.){3}(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d)))";

  static String phoneReg =
      "\\b(ip(hone|od)|android|opera m(ob|in)i"
          + "|windows (phone|ce)|blackberry"
          + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
          + "|laystation portable)|nokia|fennec|htc[-_]"
          + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
  static String tableReg =
      "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

  // 移动设备正则匹配：手机端、平板
  static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
  static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

  /**
   * 获取远端ip地址
   *
   * @param request
   * @return
   */
  public static String getRemoteIP(HttpServletRequest request) {
    //		LogUtil.info("X-Real-IP=" + request.getHeader(X_REAL_IP) + ",X-Forwarded-For="
    //				+ request.getHeader(X_FORWARDED_FOR));
    String ip = request.getHeader(X_REAL_IP);
    if (!StringUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
      return ip;
    }
    ip = request.getHeader(X_FORWARDED_FOR);
    if (!StringUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个IP值，第一个为真实IP。
      int index = ip.indexOf(',');
      if (index != -1) {
        ip = ip.substring(0, index);
      }
    } else {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * 获取代理ip，高防中转时获取原ip需要从代理中获取
   *
   * @param request
   * @return
   */
  public static String getforwardedForIP(HttpServletRequest request) {
    String ip = request.getHeader(X_FORWARDED_FOR);
    if (!StringUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个IP值，第一个为真实IP。
      int index = ip.indexOf(',');
      if (index != -1) {
        ip = ip.substring(0, index);
      }
    } else {
      ip = request.getHeader(X_REAL_IP); // 真实ip
      if (!StringUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
        return ip;
      } else {
        ip = request.getRemoteAddr();
      }
    }
    return ip;
  }

  /**
   * 获取url根域名以及端口号
   *
   * @param request
   * @return
   */
  public static final String getBaseUrl(HttpServletRequest request) {
    String serverName = request.getServerName();
    String baseUrl = request.getScheme() + "://" + serverName;
    if (serverName.matches(IP_REG)) {
      int port = request.getServerPort();
      baseUrl += port == 80 ? "" : (":" + port);
    }
    return baseUrl;
  }

  /**
   * 获取系统根路径
   *
   * @param request
   * @return
   */
  public static final String getBasePath(HttpServletRequest request) {
    return getBaseUrl(request) + request.getContextPath();
  }

  /**
   * 获取web根路径 物理路径
   *
   * @param request
   * @return
   */
  public static String getWebRealPath(HttpServletRequest request) {
    return request.getSession().getServletContext().getRealPath("");
  }

  /**
   * 获取当前地址
   *
   * @param request
   * @return
   */
  public static String getCurUrl(HttpServletRequest request) {
    return getBaseUrl(request) + request.getRequestURI();
  }

  public static String getUri(HttpServletRequest request) {
    String uri = request.getServletPath();
    if (StringUtil.isBlank(uri)) {
      uri = request.getRequestURI();
    }
    return uri;
  }

  /**
   * 获取根域名
   *
   * @param request
   * @return
   */
  public static String getRootdomain(HttpServletRequest request) {
    String domain = null;
    // 获取顶级域名
    domain = request.getServerName();
    try {
      Pattern p =
          Pattern.compile(
              "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv|co)",
              Pattern.CASE_INSENSITIVE);
      Matcher matcher = p.matcher(domain);
      matcher.find();
      domain = matcher.group();
      log.info("根域名:" + domain);
    } catch (Exception e) {
      log.warn("no find domain");
    }
    return domain;
  }

  /**
   * 设置cookie
   *
   * @param request
   * @param response
   * @param domain 指定域名
   * @param key 名称
   * @param value 值
   * @param expiry 过期时间，如果<=0则为会话级别
   */
  public static void setCookie(
      HttpServletRequest request,
      HttpServletResponse response,
      String domain,
      String key,
      String value,
      int expiry) {
    // 保存到cookie中
    value = htmlUTf8Encode(value);
    Cookie cookie = new Cookie(key, value);
    if (StringUtil.isNotBlank(domain)) {
      // 设置域名
      cookie.setDomain("." + domain);
    }
    // logger.info("====" + domain);
    // 设置过期时间,如果<=0则为会话级别
    if (expiry >= 0) cookie.setMaxAge(expiry);
    // 设置路径
    cookie.setPath("/");
    // cookie.setSecure(true);
    // cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  /**
   * html转码（utf-8）
   *
   * @param value
   * @return
   */
  public static String htmlUTf8Encode(String value) {
    return htmlEncode(value, ENCODING);
  }

  /**
   * html转码（utf-8）
   *
   * @param value
   * @return
   */
  public static String htmlUTf8Decode(String value) {
    return htmlDecode(value, ENCODING);
  }

  /**
   * html转码（utf-8）
   *
   * @param value
   * @return
   */
  public static String htmlEncode(String value, String encoding) {
    if (StringUtil.isNotBlank(value)) {
      try {
        value = URLEncoder.encode(value, encoding);
      } catch (UnsupportedEncodingException e1) {
        log.warn(value + "转码异常");
      }
    } else {
      value = "";
    }
    return value;
  }

  /**
   * html转码（utf-8）
   *
   * @param value
   * @return
   */
  public static String htmlDecode(String value, String encoding) {
    if (StringUtil.isNotBlank(value)) {
      try {
        value = URLDecoder.decode(value, encoding);
      } catch (UnsupportedEncodingException e) {
        log.warn(value + "反转码异常");
      }
    } else {
      value = "";
    }
    return value;
  }

  /**
   * 设置会话级别cookie
   *
   * @param request
   * @param response
   * @param key
   * @param value
   * @param domain
   */
  public static void setSessionCookie(
      HttpServletRequest request,
      HttpServletResponse response,
      String domain,
      String key,
      String value) {
    setCookie(request, response, domain, key, value, -1);
  }

  /**
   * 获取cookie值
   *
   * @param request
   * @param key
   * @return
   */
  public static String getCookie(HttpServletRequest request, String key) {
    // 到cookie中查询，判断cookie是否为空，不为空时取出账号、密码
    Cookie[] cookies = request.getCookies();
    String value = "";
    if (cookies != null) {
      for (Cookie c : cookies) {
        if (c.getName().equals(key)) {
          value = c.getValue().trim();
          break;
        }
      }
    }
    if (StringUtil.isNotBlank(value)) {
      value = htmlUTf8Decode(value);
    }
    return value;
  }

  /**
   * 获取指定域名下的cookie值
   *
   * @param request
   * @param key
   * @param domain
   * @return
   */
  public static String getCookie(HttpServletRequest request, String key, String domain) {
    // 到cookie中查询，判断cookie是否为空，不为空时取出账号、密码
    Cookie[] cookies = request.getCookies();
    String value = "";
    domain = "." + domain;
    if (cookies != null) {
      for (Cookie c : cookies) {
        if (c.getName().equals(key)) {
          if (StringUtil.isNotBlank(domain)) {
            if (domain.equals(c.getDomain())) {
              value = c.getValue().trim();
              break;
            }
          } else {
            value = c.getValue().trim();
            break;
          }
        }
      }
    }
    if (StringUtil.isNotBlank(value)) {
      value = htmlUTf8Decode(value);
    }
    return value;
  }

  /**
   * 清空cookie值
   *
   * @param request
   * @param response
   * @param key
   */
  public static void removeCookie(
      HttpServletRequest request, HttpServletResponse response, String key) {
    // setCookie(request, response, request.getServerName(), key, "", 0);
    setCookie(request, response, key, "", 0);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getLoginSession(
      HttpServletRequest request, Class<T> clazz, String sessionName) {
    return (T) request.getSession().getAttribute(sessionName);
  }

  public static void removeLoginSession(HttpServletRequest request, String sessionName) {
    request.getSession().removeAttribute(sessionName);
  }

  public static Map<String, Object> getRequestPara(HttpServletRequest request) {
    Map<String, Object> paras = new HashMap<String, Object>();
    Map<String, String[]> map = request.getParameterMap();
    for (Iterator<Entry<String, String[]>> it = map.entrySet().iterator(); it.hasNext(); ) {
      Entry<String, String[]> element = it.next();
      Object strKey = element.getKey();
      String[] value = (String[]) element.getValue();
      for (int i = 0; i < value.length; i++) {
        paras.put(strKey.toString(), value[i]);
      }
    }
    return paras;
  }

  public static String logRequestPara(HttpServletRequest request) {
    StringBuffer sb = new StringBuffer();
    Map<String, String[]> map = request.getParameterMap();
    for (Iterator<Entry<String, String[]>> it = map.entrySet().iterator(); it.hasNext(); ) {
      Entry<String, String[]> element = it.next();
      Object strKey = element.getKey();
      String[] value = (String[]) element.getValue();
      for (int i = 0; i < value.length; i++) {
        sb.append(strKey.toString()).append("=").append(value[i] + "&");
      }
    }
    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public static void setCookie(
      HttpServletRequest request,
      HttpServletResponse response,
      String key,
      String value,
      int expiry) {
    try {
      value = URLEncoder.encode(value, "utf-8");
    } catch (UnsupportedEncodingException e1) {
      log.warn("setCookie warn:" + e1.getMessage());
    }
    Cookie cookaccount = new Cookie(key, value);

    if (expiry >= 0) cookaccount.setMaxAge(expiry);

    cookaccount.setPath("/");
    response.addCookie(cookaccount);
  }

  public static void setSessionCookie(
      HttpServletRequest request, HttpServletResponse response, String key, String value) {
    setCookie(request, response, key, value, -1);
  }

  /**
   * 一级域名与二级域名分开，不共享
   *
   * @param request
   * @param response
   * @param key
   * @param value
   */
  public static void setSessionCookieNotDomain(
      HttpServletRequest request, HttpServletResponse response, String key, String value) {
    setCookie(request, response, key, value, -1);
  }

  /**
   * 判断是否为微信客户端
   *
   * @param request
   * @return
   */
  public static boolean isWeChatClient(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    return userAgent.contains("MicroMessenger");
  }

  /**
   * 检测是否是移动设备访问
   *
   * @param request
   * @return
   */
  public static boolean isMobileClient(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    if (userAgent != null) {
      // 匹配
      Matcher matcherPhone = phonePat.matcher(userAgent);
      Matcher matcherTable = tablePat.matcher(userAgent);
      if (matcherPhone.find() || matcherTable.find()) {
        return true;
      }
    }
    return false;
  }

  public static String parseDeviceType(String userAgent) {
    if (StringUtil.isBlank(userAgent)) {
      return DEVICE_TYPE_UNKNOWN;
    }
    if (userAgent.contains(DEVICE_TYPE_ANDROID)) {
      if (userAgent.toLowerCase().contains(DEVICE_TYPE_ANDROIDH5)) {
        return DEVICE_TYPE_ANDROIDH5;
      }
      return DEVICE_TYPE_ANDROID;
    } else if (userAgent.contains(DEVICE_TYPE_IPHONE)) {
      if (userAgent.toLowerCase().contains(DEVICE_TYPE_IPHONEH5)) {
        return DEVICE_TYPE_IPHONEH5;
      }
      return DEVICE_TYPE_IPHONE;
    } else if (userAgent.contains(DEVICE_TYPE_IPAD)) {
      return DEVICE_TYPE_IPAD;
    } else if (userAgent.contains(DEVICE_TYPE_WINDOWS)) {
      return DEVICE_TYPE_WINDOWS;
    } else if (userAgent.contains(DEVICE_TYPE_MAC)) {
      return DEVICE_TYPE_MAC;
    } else if (userAgent.contains(DEVICE_TYPE_LINUX)) {
      return DEVICE_TYPE_LINUX;
    } else if (userAgent.contains(DEVICE_TYPE_WUKONG)) {
      return DEVICE_TYPE_WUKONG;
    } else {
      return DEVICE_TYPE_UNKNOWN;
    }
  }
}
