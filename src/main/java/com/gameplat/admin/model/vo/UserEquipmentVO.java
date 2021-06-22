package com.gameplat.admin.model.vo;

import com.gameplat.admin.utils.HttpUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;
import jodd.util.StringUtil;

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

  private boolean isInnerEnvirornment = false;//是否是我们网站和app对应的请求 true 是 false 否

  public UserEquipmentVO() {

  }

  public String getBrowserMemo() {
    return browserMemo;
  }

  public void setBrowserMemo(String browserMemo) {
    this.browserMemo = browserMemo;
  }

  public String getMacOs() {
    return macOs;
  }

  public void setMacOs(String macOs) {
    this.macOs = macOs;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }


  public boolean isInnerEnvirornment() {
    return isInnerEnvirornment;
  }

  public void setInnerEnvirornment(boolean innerEnvirornment) {
    isInnerEnvirornment = innerEnvirornment;
  }

  public boolean isMobileDevice() {
	return isMobileDevice;
  }

  public void setMobileDevice(boolean isMobileDevice) {
	this.isMobileDevice = isMobileDevice;
  }

public String getBaseURL() {
    return baseURL;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  public static UserEquipmentVO create(String userAgentString, UserAgent clientUserAgent,
      HttpServletRequest request) {
    UserEquipmentVO userEquipmentVO = new UserEquipmentVO();
    // 操作系统、浏览器
    UserAgent userAgent = StringUtil.isBlank(userAgentString) ? clientUserAgent
        : UserAgent.parseUserAgentString(userAgentString);

    Browser browser = userAgent.getBrowser();
    OperatingSystem operatingSystem = userAgent.getOperatingSystem();

    userEquipmentVO.setMobileDevice(DeviceType.MOBILE.equals(operatingSystem.getDeviceType()));//设置是否为手机设备
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
    userEquipmentVO.setIpAddress(HttpUtils.getforwardedForIP(request));
    userEquipmentVO.setBaseURL(HttpUtils.getBaseUrl(request));
    return userEquipmentVO;
  }
}
