package com.gameplat.admin.controller.open.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.bean.ChatConfig;
import com.gameplat.admin.model.bean.ChatPushCPBet;
import com.gameplat.admin.model.bean.PushLotteryWin;
import com.gameplat.admin.service.OtthService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.util.HttpClientUtils;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 聊天室配置
 *
 * @author lily
 */
@Tag(name = "聊天配置")
@RestController
@RequestMapping("/api/admin/chat/config")
public class ChatConfigController {

  private static final String lottUrl = "/api-manage/chatRoom/updateChatRoomStatus";

  @Autowired private SysDictDataService dictDataService;

  @Autowired private OtthService otthService;

  @Operation(summary = "查看彩票下注分享配置")
  @GetMapping("/getLottPushBet")
  @PreAuthorize("hasAuthority('chat:lottPushBet:view')")
  public ChatPushCPBet getLottPushBet() {
    String chatConfig =
        dictDataService.getDictDataValue(
            DictDataEnum.CHAT_PUSH_CP_BET.getType().getValue(),
            DictDataEnum.CHAT_PUSH_CP_BET.getLabel());
    return JSON.parseObject(chatConfig, ChatPushCPBet.class);
  }

  @Operation(summary = "修改彩票下注分享配置")
  @PostMapping("/editLottPushBet")
  @PreAuthorize("hasAuthority('chat:lottPushBet:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天配置->修改彩票下注分享配置:' + #chatPushCpBet")
  public void editLottPushBet(@RequestBody ChatPushCPBet chatPushCpBet) {
    // 获取额度转换配置
    JSONObject json = otthService.getLottConfig();

    String host = json.getString("host");
    String platform = json.getString("platform");
    String proxy = json.getString("proxy");
    String url = host + "/" + lottUrl;
    try {
      HashMap<String, String> map = new HashMap<>();
      map.put("autoShare", String.valueOf(chatPushCpBet.getAutoShare()));
      map.put("share", String.valueOf(chatPushCpBet.getIsOpen()));
      map.put("betMoneyLimit", String.valueOf(chatPushCpBet.getTotalMoney()));
      map.put("lottCodes", String.valueOf(chatPushCpBet.getVipEnterLevels()));
      Header[] header = getHeader(platform + ":" + proxy);
      HttpClientUtils.doPost(url, map, header);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("网络超时");
    }
    // 修改字典数据
    dictDataService.updateByTypeAndLabel(
        new SysDictData() {
          {
            setDictType(DictDataEnum.CHAT_PUSH_CP_BET.getType().getValue());
            setDictLabel(DictDataEnum.CHAT_PUSH_CP_BET.getLabel());
            setDictValue(JSON.toJSONString(chatPushCpBet));
          }
        });
  }

  @Operation(summary = "查看彩票中奖推送配置")
  @GetMapping("/getPushLotteryWin")
  @PreAuthorize("hasAuthority('chat:pushLotteryWin:view')")
  public PushLotteryWin getPushLotteryWin() {
    String chatConfig =
        dictDataService.getDictDataValue(
            DictDataEnum.CHAT_PUSH_CP_WIN.getType().getValue(),
            DictDataEnum.CHAT_PUSH_CP_WIN.getLabel());
    return JSON.parseObject(chatConfig, PushLotteryWin.class);
  }

  @Operation(summary = "修改彩票中奖推送配置")
  @PostMapping("/editPushLotteryWin")
  @PreAuthorize("hasAuthority('chat:pushLotteryWin:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天配置->修改彩票中奖推送配置:' + #pushLotteryWin")
  public void editPushLotteryWin(@RequestBody PushLotteryWin pushLotteryWin) {
    // 获取额度转换配置
    JSONObject json = otthService.getLottConfig();

    String host = json.getString("host");
    String platform = json.getString("platform");
    String proxy = json.getString("proxy");
    String url = host + "/" + lottUrl;
    try {
      HashMap<String, String> map = new HashMap<>();
      map.put("winerPush", String.valueOf(pushLotteryWin.getIsOpen()));
      Header[] header = getHeader(platform + ":" + proxy);
      HttpClientUtils.doPost(url, map, header);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("网络超时");
    }
    // 修改字典数据
    dictDataService.updateByTypeAndLabel(
        new SysDictData() {
          {
            setDictType(DictDataEnum.CHAT_PUSH_CP_WIN.getType().getValue());
            setDictLabel(DictDataEnum.CHAT_PUSH_CP_WIN.getLabel());
            setDictValue(JSON.toJSONString(pushLotteryWin));
          }
        });
  }

  @Operation(summary = "查看聊天室浮窗配置")
  @GetMapping("/getChatConfig")
  @PreAuthorize("hasAuthority('chat:chatConfig:view')")
  public ChatConfig getChatConfig() {
    String chatConfig =
        dictDataService.getDictDataValue(
            DictDataEnum.CHAT_ROOM_CONFIG.getType().getValue(),
            DictDataEnum.CHAT_ROOM_CONFIG.getLabel());
    return JSON.parseObject(chatConfig, ChatConfig.class);
  }

  @Operation(summary = "修改聊天室浮窗配置")
  @PostMapping("/updateChatConfig")
  @PreAuthorize("hasAuthority('chat:chatConfig:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天配置->修改聊天室浮窗配置:' + #chatConfig")
  public void updateChatConfig(@RequestBody ChatConfig chatConfig) {
    // 获取额度转换配置
    JSONObject json = otthService.getLottConfig();
    String host = json.getString("host");
    String platform = json.getString("platform");
    String proxy = json.getString("proxy");
    String url = host + "/" + lottUrl;
    try {
      HashMap<String, String> map = new HashMap<>();
      map.put("showAttention", chatConfig.getShowAttention().toString());
      Header[] header = getHeader(platform + ":" + proxy);
      HttpClientUtils.doPost(url, map, header);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("网络超时");
    }
    // 修改字典数据
    dictDataService.updateByTypeAndLabel(
        new SysDictData() {
          {
            setDictType(DictDataEnum.CHAT_ROOM_CONFIG.getType().getValue());
            setDictLabel(DictDataEnum.CHAT_ROOM_CONFIG.getLabel());
            setDictValue(JSON.toJSONString(chatConfig));
          }
        });
  }

  private Header[] getHeader(String tenant) {
    return new Header[] {
      new BasicHeader("client", "0"),
      new BasicHeader("locale", "zh"),
      new BasicHeader("attribution", tenant),
    };
  }
}
