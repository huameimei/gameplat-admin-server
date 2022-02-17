package com.gameplat.admin.controller.open.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.doc.ChatConfig;
import com.gameplat.admin.model.doc.ChatPushCPBet;
import com.gameplat.admin.model.doc.PushLotteryWin;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.service.OtthService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.ChatConfigEnum;
import com.gameplat.common.util.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author lily
 * @description 聊天室配置
 * @date 2022/2/15
 */

@Api(tags = "聊天配置")
@RestController
@RequestMapping("/api/admin/chat/config")
public class ChatConfigController {

    @Autowired
    private SysDictDataService dictDataService;
    @Autowired
    private OtthService otthService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String lottUrl = "/api-manage/chatRoom/updateChatRoomStatus";

    private final String DICT_DATA_CACHE = "dict:data:";

    @ApiOperation(value = "查看彩票下注分享配置")
    @GetMapping("/getLottPushBet")
    @PreAuthorize("hasAuthority('chat:config:getLottPushBet')")
    public ChatPushCPBet getLottPushBet(){
        String chatConfig = dictDataService.getDictDataValue(ChatConfigEnum.CHAT_PUSH_CP_BET.getType().getValue(), ChatConfigEnum.CHAT_PUSH_CP_BET.getLabel());
        return JSON.parseObject(chatConfig, ChatPushCPBet.class);
    }

    @ApiOperation(value = "修改彩票下注分享配置")
    @PutMapping("/editLottPushBet")
    @PreAuthorize("hasAuthority('chat:config:getLottPushBet')")
    public void editLottPushBet(ChatPushCPBet chatPushCPBet) {
        //获取额度转换配置
        JSONObject json = otthService.getLottConfig();

        String host = json.getString("host");
        String platform = json.getString("platform");
        String proxy = json.getString("proxy");
        String url = host + "/" + lottUrl;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("autoShare", String.valueOf(chatPushCPBet.getAutoShare()));
            map.put("share", String.valueOf(chatPushCPBet.getIsOpen()));
            map.put("betMoneyLimit", String.valueOf(chatPushCPBet.getTotalMoney()));
            map.put("lottCodes", String.valueOf(chatPushCPBet.getVipEnterLevels()));
            Header[] header = getHeader(platform + ":" + proxy);
            HttpClientUtils.doPost(url, map, header);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("网络超时");
        }
        //修改字典数据
        dictDataService.updateByTypeAndLabel(new SysDictData(){{
            setDictType(ChatConfigEnum.CHAT_PUSH_CP_BET.getType().getValue());
            setDictLabel(ChatConfigEnum.CHAT_PUSH_CP_BET.getLabel());
            setDictValue(JSON.toJSONString(chatPushCPBet));
        }});
    }

    @ApiOperation(value = "查看彩票中奖推送配置")
    @GetMapping("/getPushLotteryWin")
    @PreAuthorize("hasAuthority('chat:config:getPushLotteryWin')")
    public PushLotteryWin getPushLotteryWin(){
        String chatConfig = dictDataService.getDictDataValue(ChatConfigEnum.CHAT_PUSH_CP_WIN.getType().getValue(), ChatConfigEnum.CHAT_PUSH_CP_WIN.getLabel());
        return JSON.parseObject(chatConfig, PushLotteryWin.class);
    }

    @ApiOperation(value = "修改彩票中奖推送配置")
    @PutMapping("/editPushLotteryWin")
    @PreAuthorize("hasAuthority('chat:config:editPushLotteryWin')")
    public void editPushLotteryWin(PushLotteryWin pushLotteryWin) {
        //获取额度转换配置
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
        //修改字典数据
        dictDataService.updateByTypeAndLabel(new SysDictData(){{
            setDictType(ChatConfigEnum.CHAT_PUSH_CP_WIN.getType().getValue());
            setDictLabel(ChatConfigEnum.CHAT_PUSH_CP_WIN.getLabel());
            setDictValue(JSON.toJSONString(pushLotteryWin));
        }});
    }

    @ApiOperation(value = "查看聊天室浮窗配置")
    @GetMapping("/getChatConfig")
    @PreAuthorize("hasAuthority('chat:config:getChatConfig')")
    public ChatConfig getChatConfig() {
        String chatConfig = dictDataService.getDictDataValue(ChatConfigEnum.CHAT_ROOM_CONFIG.getType().getValue(), ChatConfigEnum.CHAT_ROOM_CONFIG.getLabel());
        return JSON.parseObject(chatConfig, ChatConfig.class);
    }

    @ApiOperation(value = "修改聊天室浮窗配置")
    @PutMapping("/updateChatConfig")
    @PreAuthorize("hasAuthority('chat:config:updateChatConfig')")
    public void updateChatConfig(ChatConfig chatConfig) {
        //获取额度转换配置
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
        //修改字典数据
        dictDataService.updateByTypeAndLabel(new SysDictData(){{
            setDictType(ChatConfigEnum.CHAT_ROOM_CONFIG.getType().getValue());
            setDictLabel(ChatConfigEnum.CHAT_ROOM_CONFIG.getLabel());
            setDictValue(JSON.toJSONString(chatConfig));
        }});
    }

    private Header[] getHeader(String tenant) {
        return new Header[] {new BasicHeader("client", "0"), new BasicHeader("locale", "zh"),
                new BasicHeader("attribution", tenant),};
    }
}
