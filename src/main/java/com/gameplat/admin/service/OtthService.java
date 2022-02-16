package com.gameplat.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.enums.ClientTypeEnum;
import com.gameplat.admin.feign.RemoteLogService;
import com.gameplat.admin.model.vo.LotteryCodeVo;
import com.gameplat.admin.model.vo.LotteryTypeListVo;
import com.gameplat.admin.util.HttpClient;
import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.log.SysLog;
import com.gameplat.base.common.util.HttpRespBean;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.util.HttpClientUtils;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** 聊天室业务层处理 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class OtthService {

    private final String lottUrl = "/api-manage/chatRoom/getLottTypeList";
    private final String Lott_CONFIG_URL = "/api-manage/chatRoom/updateChatRoomStatus";
    private static String IP_PCONLINE_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 聊天室下注推送策略
    private final String CHAT_PUSH_SP_BET =
        "{\"autoShare\":0,\"gameIds\":\"23,25,29,27,,33,131,70,173\",\"isOpen\":1,\"notPushAccount\":\"baozi\",\"notPushPlayCode\":\"\",\"onlyPushAccount\":0,\"pushAccount\":\"jeff13,sss111\",\"pushPlayCode\":\"\",\"rechCount\":0,\"rechMoney\":0.0,\"showHeel\":1,\"showHeelMinMoney\":20.0,\"todayRechMoney\":0.0,\"totalMoney\":0.0,\"validBetMoney\":0.0}";

    @Autowired
    private ChatSideMenuService menuService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private GameConfigService gameConfigService;
    @Autowired
    private TenantDomainService tenantDomainService;
    @Autowired
    private RemoteLogService remoteLogService;



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

    public void pushChatOpen(String body) {
        JSONObject json = getLottConfig();
        String host = json.getString("host");
        String platform = json.getString("platform");
        String proxy = json.getString("proxy");
        String url = host + "/" + Lott_CONFIG_URL;
        JSONObject jsonObjectNo = JSONObject.parseObject(body);
        String isOpen = jsonObjectNo.getString("chatOpen");
        Map<String, String> map = new HashMap<>();
        map.put("chatStatus", isOpen);
        try {
            HttpClientUtils.doPost(url, map, getHeader(platform + ":" + proxy));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 更新聊天室房间管理内的游戏状态
    public void updateGameStuats(String gameId, int gameStatus) {
        JSONArray roomInfoList = getRoomInfoList();
        if (roomInfoList != null && !roomInfoList.isEmpty()) {
            roomInfoList.forEach(x -> {
                String gameIds = ((JSONObject)x).getString("planGame");
                if (StringUtils.isNotBlank(gameIds)) {
                    List<String> list=new ArrayList<>();
                    Collections.addAll(list,gameIds.split(","));
                    if (list.contains(gameId)) {
                        //1代表正常运行
                        if (gameStatus != 1) {
                            list.remove(gameId);
                            String planGame = Joiner.on(",").join(list);
                            ((JSONObject)x).put("planGame", planGame);
                            updateRoom(x.toString());
                        }
                    }
                }
            });
        }
        log.info("彩票修改游戏状态成功,游戏id为:{},游戏状态为:{}",gameId,gameStatus);
    }

    public JSONArray getRoomInfoList() {
        String chatDomain = tenantDomainService.getChatDomain();
        if (org.apache.commons.lang3.StringUtils.isBlank(chatDomain)) {
            throw new ServiceException("未配服务");
        }
        String dbSuffix = DyDataSourceContextHolder.getTenant();

        String apiUrl = chatDomain + "/api/room/query";
        HttpClient httpClient = HttpClient.build().get(apiUrl);
        httpClient.addHead("plat_code", dbSuffix);
        Map<String, String> params = new HashMap<>();
        params.put("platCode", dbSuffix);
        httpClient.setPara(params);
        HttpRespBean respBean = httpClient.execute();
        if (respBean != null) {
            JSONArray roomList = JSONArray.parseArray(respBean.getRespBody());
            return roomList;
        }
        return null;
    }

    public void updateRoom(String body) {
        String chatDomain = tenantDomainService.getChatDomain();
        if (org.apache.commons.lang3.StringUtils.isBlank(chatDomain)) {
            throw new ServiceException("未配服务");
        }
        String apiUrl = chatDomain + "/api/room/update";
        Header[] header = new Header[] {new BasicHeader("Content-Type", "application/json"),
                new BasicHeader("plat_code", DyDataSourceContextHolder.getTenant())};
        try {
            HttpClientUtils.doPost(apiUrl, body, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String otthProxyHttpPost(String apiUrl, String body, HttpServletRequest request, String proxy) throws Exception {
        Header[] header =
                new Header[] {new BasicHeader("Content-Type", "application/json"), new BasicHeader("plat_code", proxy)};
        String result = HttpClientUtils.postRetrun(apiUrl, "json", body, header);
        String content = "";
        // String sysId="";

        if (JSON.parse(body).getClass().getName().contains("JSONArray")) {
            content = result;
        } else {
            content = JSONObject.parseObject(body).getString("logContent");
        }

        if (StringUtils.isNotEmpty(content)) {
            String finalContent = content;
            CompletableFuture.runAsync(() -> {
                long start = System.currentTimeMillis();
                SysLog sysLog = new SysLog();
                sysLog.setUsername(GlobalContextHolder.getContext().getUsername());
                sysLog.setUserType(GlobalContextHolder.getContext().getUserType());
                sysLog.setModule(ClientTypeEnum.CHAT_MANAGE.getClientName());
                sysLog.setParams(body);
                sysLog.setFlag(Boolean.TRUE);
                // 聊天室代码成功的时候无返回
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Object code = jsonObject.get("code");
                    if (code.equals("system_error") || code.equals("400")) {
                        sysLog.setFlag(Boolean.FALSE);
                    }
                }
                String ipaddress = IPUtils.getIpAddress(request);
                sysLog.setMethod("post");
                sysLog.setIp(ipaddress);
                // IP归属地
                sysLog.setIpDesc(getAddressByIp_third(ipaddress));
                sysLog.setDesc(finalContent);
                sysLog.setPath(apiUrl);
                sysLog.setDbSuffix(proxy);
                sysLog.setCreateTime(new Date());
                sysLog.setDoTime((System.currentTimeMillis() - start) + "");
                //保存操作日志
                remoteLogService.saveOperLog(sysLog);
            }, taskExecutor);
        }

        return result;
    }

    //获取ip归属地
    public static String getAddressByIp_third(String ip) {
        String serviceUrl = IP_PCONLINE_URL + "?json=true&ip=" + ip;
        HttpClient httpClient = HttpClient.build().get(serviceUrl);
        httpClient.setEncode("GB2312");
        HttpRespBean respBean = httpClient.execute();
        String result = respBean.getRespBody();//HttpUtils.getDocument(serviceUrl, "GBK");
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (StringUtils.isNotBlank(jsonObject.getString("addr"))) {
                return jsonObject.getString("addr");
            } else if (StringUtils.isNotBlank(jsonObject.getString("pro"))) {
                return jsonObject.getString("pro");
            }
        }
        return "未知";
    }

}
