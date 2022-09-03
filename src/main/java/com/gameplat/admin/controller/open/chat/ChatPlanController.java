package com.gameplat.admin.controller.open.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.basepay.util.HttpUtil;
import com.gameplat.common.util.HttpClientUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Created by adu on 2018/12/10. */
@Controller
@RequestMapping("/api/admin/chat/plan")
public class ChatPlanController {
  private static final String BASE64_KEY = ",.698add&$%%&*)(!~fkwe";
  private static final String[] image_type = new String[] {"bmp", "jpg", "jpeg", "png", "gif"};
  @Autowired private OtthService otthService;
  @Autowired private SysDomainService sysDomainService;
  @Autowired private OssService ossService;

  private Logger logger = LoggerFactory.getLogger(ChatPlanController.class);

  @RequestMapping(value = "/sendImage", method = RequestMethod.POST)
  @ResponseBody
  public String uploadImage(
      HttpServletRequest request, @RequestParam("file") MultipartFile file, String key) {
    try {
      Map<String, Object> params = readKeyInfo(key);
      String url = ossService.upload(file,"chat_plan");
      BufferedImage image = ImageIO.read(file.getInputStream());
      JSONObject imageInfo = new JSONObject();
      imageInfo.put("img", url);
      imageInfo.put("sm", url);
      imageInfo.put("img_width", image.getWidth());
      imageInfo.put("img_height", image.getWidth());
      imageInfo.put("sm_width", image.getHeight());
      imageInfo.put("sm_height", image.getHeight());
      String requestIp = HttpUtil.getforwardedForIP(request);

      params.put("imageInfo", imageInfo);
      return proxyPost(requestIp, "api_push_imageV2", params);
    } catch (IOException e) {
      return "文件上传失败";
    } catch (ServiceException e) {
      return e.getMessage();
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  @RequestMapping(
      value = "/getKey",
      method = {RequestMethod.POST, RequestMethod.GET})
  @ResponseBody
  public String getKey(Long userId, String roomIds) throws Exception {
    return Xxtea.encryptToXxteaByBase64(String.format("%d|%s", userId, roomIds), BASE64_KEY);
  }

  @RequestMapping(
      value = "/sendMessage",
      method = {RequestMethod.POST, RequestMethod.GET})
  @ResponseBody
  public String sendMessage(HttpServletRequest request, String key, String text, String dt) {
    try {
      if (StringUtils.isBlank(text)) {
        throw new ServiceException("发送内容[text]不能为空");
      }
      String requestIp = HttpUtil.getforwardedForIP(request);
      Map<String, Object> params = readKeyInfo(key);
      params.put("text", text);
      params.put("dt", dt);
      return proxyPost(requestIp, "api_push_message", params);
    } catch (ServiceException e) {
      return e.getMessage();
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  private Map<String, Object> readKeyInfo(String key) throws ServiceException {
    if (StringUtils.isBlank(key)) {
      throw new ServiceException("key 不能为空");
    }
    try {
      String s = Xxtea.decryptToXxteaByBase64(key, BASE64_KEY);
      String[] array = s.split("\\|");
      if (array.length != 2) {
        throw new ServiceException();
      }
      Map<String, Object> params = new HashMap<>();
      params.put("userId", Long.parseLong(array[0]));
      params.put("roomIds", array[1]);
      return params;
    } catch (Exception e) {
      throw new ServiceException(String.format("key[%s] 值不正确", key));
    }
  }

  private String getApiUrl(String url) {
    String chatDomain = sysDomainService.getChatDomain();
    url = chatDomain + "/" + url.replace("_", "/");
    return url;
  }

  private String proxyPost(String requestIp, String apiPath, Map<String, Object> params)
      throws Exception {
    // 判断ip是否为白名单

    apiPath = apiPath.replace("_", "/");
    String chatServiceUrl = getApiUrl(apiPath);
    Header[] headers = new Header[] {new BasicHeader("plat_code", otthService.getLottTenantCode())};
    try {
      return HttpClientUtils.doPost(chatServiceUrl, JSON.toJSONString(params), headers);
    } catch (ServiceException e) {
      JSONObject json = JSON.parseObject(e.getMessage());
      throw new ServiceException(json.getString("msg"));
    }
  }
}
