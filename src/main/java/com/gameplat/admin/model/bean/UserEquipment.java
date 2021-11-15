package com.gameplat.admin.model.bean;

import com.gameplat.admin.util.WebUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.ApiModelProperty;
import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;
import jodd.util.StringUtil;
import lombok.Data;

@Data
public class UserEquipment {

    @ApiModelProperty(value = "浏览器版本号")
    private String browserMemo;

    @ApiModelProperty(value = "操作系统")
    private String macOs;

    @ApiModelProperty(value = "完整的信息")
    private String userAgent;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "获取url根域名以及端口号")
    private String baseURL;

    @ApiModelProperty(value = "是否为手机设备")
    private boolean isMobileDevice;

    @ApiModelProperty(value = "是否是我们网站和app对应的请求 true 是 false 否")
    private boolean isInnerEnvirornment = false;//是否是我们网站和app对应的请求 true 是 false 否

    public UserEquipment() {

    }


    public static UserEquipment create(String userAgentString, UserAgent clientUserAgent,
                                          HttpServletRequest request) {
        UserEquipment userEquipmentDTO = new UserEquipment();
        // 操作系统、浏览器
        UserAgent userAgent = StringUtil.isBlank(userAgentString) ? clientUserAgent
                : UserAgent.parseUserAgentString(userAgentString);

        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        userEquipmentDTO
                .setMobileDevice(DeviceType.MOBILE.equals(operatingSystem.getDeviceType()));//设置是否为手机设备
        userEquipmentDTO.setBrowserMemo(browser.getName() + userAgent.getBrowserVersion());
        userEquipmentDTO.setMacOs(operatingSystem.getName());
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
        userEquipmentDTO.setUserAgent(uaString);
        userEquipmentDTO.setIpAddress(WebUtils.getforwardedForIP(request));
        userEquipmentDTO.setBaseURL(WebUtils.getBaseUrl(request));
        return userEquipmentDTO;
    }

}
