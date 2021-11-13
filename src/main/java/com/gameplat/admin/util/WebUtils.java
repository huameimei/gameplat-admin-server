package com.gameplat.admin.util;

import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络功能
 * @author three
 */
@Slf4j
public class WebUtils {

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
    public static final String IP_REG = "((?:(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d))\\.){3}(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d)))";
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i|windows (phone|ce)|blackberry|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp|laystation portable)|nokia|fennec|htc[-_]|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static Pattern phonePat;
    static Pattern tablePat;

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        } else {
            ip = request.getHeader("X-Forwarded-For");
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(44);
                if (index != -1) {
                    ip = ip.substring(0, index);
                }
            } else {
                ip = request.getRemoteAddr();
            }

            return ip;
        }
    }

    public static String getforwardedForIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(44);
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        } else {
            ip = request.getHeader("X-Real-IP");
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }

            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static final String getBaseUrl(HttpServletRequest request) {
        String serverName = request.getServerName();
        String baseUrl = request.getScheme() + "://" + serverName;
        if (serverName.matches("((?:(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d))\\.){3}(?:25[0-5]|2[0-4]\\d|(?:1\\d{2}|[1-9]?\\d)))")) {
            int port = request.getServerPort();
            baseUrl = baseUrl + (port == 80 ? "" : ":" + port);
        }

        return baseUrl;
    }

    public static final String getBasePath(HttpServletRequest request) {
        return getBaseUrl(request) + request.getContextPath();
    }

    public static String getWebRealPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("");
    }

    public static String getCurUrl(HttpServletRequest request) {
        return getBaseUrl(request) + request.getRequestURI();
    }

    public static String getUri(HttpServletRequest request) {
        String uri = request.getServletPath();
        if (StringUtils.isBlank(uri)) {
            uri = request.getRequestURI();
        }

        return uri;
    }

    public static String getRootdomain(HttpServletRequest request) {
        String domain = null;
        domain = request.getServerName();

        try {
            Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv|co)", 2);
            Matcher matcher = p.matcher(domain);
            matcher.find();
            domain = matcher.group();
            log.info("根域名:" + domain);
        } catch (Exception var4) {
            log.warn("no find domain");
        }

        return domain;
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String domain, String key, String value, int expiry) {
        value = htmlUTf8Encode(value);
        Cookie cookie = new Cookie(key, value);
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain("." + domain);
        }

        if (expiry >= 0) {
            cookie.setMaxAge(expiry);
        }

        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String htmlUTf8Encode(String value) {
        return htmlEncode(value, "utf-8");
    }

    public static String htmlUTf8Decode(String value) {
        return htmlDecode(value, "utf-8");
    }

    public static String htmlEncode(String value, String encoding) {
        if (StringUtils.isNotBlank(value)) {
            try {
                value = URLEncoder.encode(value, encoding);
            } catch (UnsupportedEncodingException var3) {
                log.warn(value + "转码异常");
            }
        } else {
            value = "";
        }

        return value;
    }

    public static String htmlDecode(String value, String encoding) {
        if (StringUtils.isNotBlank(value)) {
            try {
                value = URLDecoder.decode(value, encoding);
            } catch (UnsupportedEncodingException var3) {
                log.warn(value + "反转码异常");
            }
        } else {
            value = "";
        }

        return value;
    }

    public static void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String domain, String key, String value) {
        setCookie(request, response, domain, key, value, -1);
    }

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        String value = "";
        if (cookies != null) {
            Cookie[] var4 = cookies;
            int var5 = cookies.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Cookie c = var4[var6];
                if (c.getName().equals(key)) {
                    value = c.getValue().trim();
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(value)) {
            value = htmlUTf8Decode(value);
        }

        return value;
    }

    public static String getCookie(HttpServletRequest request, String key, String domain) {
        Cookie[] cookies = request.getCookies();
        String value = "";
        domain = "." + domain;
        if (cookies != null) {
            Cookie[] var5 = cookies;
            int var6 = cookies.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Cookie c = var5[var7];
                if (c.getName().equals(key)) {
                    if (!StringUtils.isNotBlank(domain)) {
                        value = c.getValue().trim();
                        break;
                    }

                    if (domain.equals(c.getDomain())) {
                        value = c.getValue().trim();
                        break;
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(value)) {
            value = htmlUTf8Decode(value);
        }

        return value;
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        setCookie(request, response, key, "", 0);
    }

    public static <T> T getSession(HttpServletRequest request, Class<T> clazz, String sessionName) {
        return (T) request.getSession().getAttribute(sessionName);
    }

    public static void removeSession(HttpServletRequest request, String sessionName) {
        request.getSession().removeAttribute(sessionName);
    }

    public static Map<String, Object> getRequestPara(HttpServletRequest request) {
        Map<String, Object> paras = new HashMap();
        Map<String, String[]> map = request.getParameterMap();
        Iterator it = map.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, String[]> element = (Map.Entry)it.next();
            Object strKey = element.getKey();
            String[] value = (String[])((String[])element.getValue());

            for(int i = 0; i < value.length; ++i) {
                paras.put(strKey.toString(), value[i]);
            }
        }

        return paras;
    }

    public static String logRequestPara(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        Map<String, String[]> map = request.getParameterMap();
        Iterator it = map.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, String[]> element = (Map.Entry)it.next();
            Object strKey = element.getKey();
            String[] value = (String[])((String[])element.getValue());

            for(int i = 0; i < value.length; ++i) {
                sb.append(strKey.toString()).append("=").append(value[i] + "&");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static Object getJsonp(String jsonp, Object object) {
        if (jsonp != null && PATTERN_JSONP.matcher(jsonp).matches()) {
            String json = JsonUtils.toJson(object);
            return jsonp + "(" + json + ")";
        } else {
            return object;
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String key, String value, int expiry) {
        try {
            value = URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException var6) {
            log.warn("setCookie warn:" + var6.getMessage());
        }

        Cookie cookaccount = new Cookie(key, value);
        if (expiry >= 0) {
            cookaccount.setMaxAge(expiry);
        }

        cookaccount.setPath("/");
        response.addCookie(cookaccount);
    }

    public static void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String key, String value) {
        setCookie(request, response, key, value, -1);
    }

    public static void setSessionCookieNotDomain(HttpServletRequest request, HttpServletResponse response, String key, String value) {
        setCookie(request, response, key, value, -1);
    }

    public static boolean isWeChatClient(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent.contains("MicroMessenger");
    }

    public static boolean isMobileClient(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            Matcher matcherPhone = phonePat.matcher(userAgent);
            Matcher matcherTable = tablePat.matcher(userAgent);
            if (matcherPhone.find() || matcherTable.find()) {
                return true;
            }
        }

        return false;
    }

    public static String parseDeviceType(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return "unknown";
        } else if (userAgent.contains("Android")) {
            return userAgent.toLowerCase().contains("native_android") ? "native_android" : "Android";
        } else if (userAgent.contains("iPhone")) {
            return userAgent.toLowerCase().contains("native_ios") ? "native_ios" : "iPhone";
        } else if (userAgent.contains("iPad")) {
            return "iPad";
        } else if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "Mac";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else {
            return userAgent.contains("Wukong") ? "Wukong" : "unknown";
        }
    }

}
