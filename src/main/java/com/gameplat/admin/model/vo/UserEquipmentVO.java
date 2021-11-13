package com.gameplat.admin.model.vo;

import com.gameplat.common.ip.IpAddressParser;
import com.gameplat.common.util.ServletUtils;
import com.gameplat.common.util.StringUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

@Data
public class UserEquipmentVO {

  /**
   * 浏览器+版本号
   */
  private String browserMemo;
  /**
   * 操作系统
   */
  private String macOs;
  /**
   * 完整的信息
   */
  private String userAgent;
  /**
   * IP地址
   */
  private String ipAddress;
  /**
   * 获取url根域名以及端口号
   */
  private String baseURL;

  /**
   * 是否为手机设备
   */
  private boolean isMobileDevice;

  /**
   * 是否是我们网站和app对应的请求 true 是 false 否
   */
  private boolean isInnerEnvirornment = false;

  public static UserEquipmentVO create(UserAgent userAgent,
      HttpServletRequest request) {
    UserEquipmentVO userEquipmentVO = new UserEquipmentVO();
    // 操作系统、浏览器
    Browser browser = userAgent.getBrowser();
    OperatingSystem operatingSystem = userAgent.getOperatingSystem();
    //设置是否为手机设备
    userEquipmentVO.setMobileDevice(DeviceType.MOBILE.equals(operatingSystem.getDeviceType()));
    userEquipmentVO.setBrowserMemo(browser.getName() + userAgent.getBrowserVersion());
    userEquipmentVO.setMacOs(operatingSystem.getName());
    String uaString = userAgent.toString();
    Field f;
    try {
      f = userAgent.getClass().getDeclaredField("userAgentString");
      f.setAccessible(true);
      uaString = (String) f.get(userAgent);
      if (uaString != null && uaString.length() > 250) {
        uaString = uaString.substring(0, 250);
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
    userEquipmentVO.setUserAgent(uaString);
    userEquipmentVO.setIpAddress(IpAddressParser.getClientIp(request));
    userEquipmentVO.setBaseURL(ServletUtils.getBaseUrl(request));
    return userEquipmentVO;
  }

}
