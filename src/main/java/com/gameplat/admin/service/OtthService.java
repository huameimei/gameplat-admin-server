package com.gameplat.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.vo.LotteryCodeVo;
import com.gameplat.admin.model.vo.LotteryTypeListVo;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.util.HttpClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

/** 聊天室业务层处理 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtthService {

    private final String lottUrl = "/api-manage/chatRoom/getLottTypeList";
    private final String Lott_CONFIG_URL = "/api-manage/chatRoom/updateChatRoomStatus";

    // 聊天室下注推送策略
    private final String CHAT_PUSH_SP_BET =
        "{\"autoShare\":0,\"gameIds\":\"23,25,29,27,,33,131,70,173\",\"isOpen\":1,\"notPushAccount\":\"baozi\",\"notPushPlayCode\":\"\",\"onlyPushAccount\":0,\"pushAccount\":\"jeff13,sss111\",\"pushPlayCode\":\"\",\"rechCount\":0,\"rechMoney\":0.0,\"showHeel\":1,\"showHeelMinMoney\":20.0,\"todayRechMoney\":0.0,\"totalMoney\":0.0,\"validBetMoney\":0.0}";

    @Autowired
    private ChatSideMenuService menuService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private GameConfigService gameConfigService;


    /**  */
    public List<LotteryCodeVo> getLottTypeList() {
        //获取额度转换配置
        JSONObject json = getLottConfig();
        String host = json.getString("host");
        String platform = json.getString("platform");
        String proxy = json.getString("proxy");
        String url = host + "/" + lottUrl;
        List<LotteryCodeVo> list = new ArrayList<>();
        String lotteryList = null;
        try {
            lotteryList = HttpClientUtils.doPost(url, "", getHeader(platform + ":" + proxy));
            JSONObject jsonObject = JSONObject.parseObject(lotteryList);
            Integer code = (Integer)jsonObject.get("code");
            if (code == 200) {
                String data = jsonObject.getString("data");
                List<LotteryTypeListVo> lotteryTypeListVos = JSON.parseArray(data, LotteryTypeListVo.class);
                for (LotteryTypeListVo x : lotteryTypeListVos) {
                    list.addAll(x.getData());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 获取额度转换配置 */
    private JSONObject getLottConfig() {
        JSONObject gameConfig = gameConfigService.queryGameConfigInfoByPlatCode(TransferTypesEnum.KGNL.getCode());
        return JSONObject.parseObject(gameConfig.get("config").toString());
    }

    /** 获取头信息  */
    private Header[] getHeader(String tenant) {
        return new Header[] {new BasicHeader("client", "0"), new BasicHeader("locale", "zh"),
                new BasicHeader("attribution", tenant),};
    }

}
