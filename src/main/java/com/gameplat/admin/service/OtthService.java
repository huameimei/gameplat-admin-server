package com.gameplat.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.config.TenantConfig;
import com.gameplat.admin.enums.ClientTypeEnum;
import com.gameplat.admin.feign.RemoteLogService;
import com.gameplat.admin.model.bean.ChatPushCPBet;
import com.gameplat.admin.model.bean.PushCPMessageReq;
import com.gameplat.admin.model.bean.PushLotteryWin;
import com.gameplat.admin.model.dto.MemberActivationDTO;
import com.gameplat.admin.model.dto.PushCPBetMessageReq;
import com.gameplat.admin.model.vo.*;
import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.log.SysLog;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.HttpRespBean;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.common.enums.ChatConfigEnum;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.util.HttpClient;
import com.gameplat.common.util.HttpClientUtils;
import com.gameplat.model.entity.member.Member;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alibaba.excel.util.DateUtils.DATE_FORMAT_10;

/** 聊天室业务层处理 */
@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class OtthService {
  private final String lottUrl = "/api-manage/chatRoom/getLottTypeList";
  private final String Lott_CONFIG_URL = "/api-manage/chatRoom/updateChatRoomStatus";
  private static String IP_PCONLINE_URL = "http://whois.pconline.com.cn/ipJson.jsp";

  /** 聊天室彩票中奖推送设置 */
  public static final String CHAT_PUSH_CP_WIN = "CHAT_PUSH_CP_WIN";
  /** 聊天室彩票下注推送配置 */
  public static final String CHAT_PUSH_CP_BET = "CHAT_PUSH_CP_BET";
  // 聊天室下注推送策略
  private final String CHAT_PUSH_SP_BET =
      "{\"autoShare\":0,\"gameIds\":\"23,25,29,27,,33,131,70,173\",\"isOpen\":1,\"notPushAccount\":\"baozi\",\"notPushPlayCode\":\"\",\"onlyPushAccount\":0,\"pushAccount\":\"jeff13,sss111\",\"pushPlayCode\":\"\",\"rechCount\":0,\"rechMoney\":0.0,\"showHeel\":1,\"showHeelMinMoney\":20.0,\"todayRechMoney\":0.0,\"totalMoney\":0.0,\"validBetMoney\":0.0}";

  @Autowired private TenantConfig tenantConfig;

  @Autowired private ChatSideMenuService menuService;

  @Autowired private Executor asyncExecutor;

  @Autowired private GameConfigService gameConfigService;

  @Autowired private TenantDomainService tenantDomainService;

  @Autowired private RemoteLogService remoteLogService;

  @Autowired private MemberService memberService;

  @Autowired private RedisTemplate redisTemplate;

  @Autowired private RechargeOrderService rechargeOrderService;

  public List<LotteryCodeVo> getLottTypeList() {
    // 获取额度转换配置
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
      Integer code = (Integer) jsonObject.get("code");
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
  public JSONObject getLottConfig() {
    JSONObject gameConfig =
        gameConfigService.queryGameConfigInfoByPlatCode(TransferTypesEnum.KGNL.getCode());
    return gameConfig;
  }

  /** 获取头信息 */
  private Header[] getHeader(String tenant) {
    return new Header[] {
      new BasicHeader("client", "0"),
      new BasicHeader("locale", "zh"),
      new BasicHeader("attribution", tenant),
    };
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
      roomInfoList.forEach(
          x -> {
            String gameIds = ((JSONObject) x).getString("planGame");
            if (StringUtils.isNotBlank(gameIds)) {
              List<String> list = new ArrayList<>();
              Collections.addAll(list, gameIds.split(","));
              if (list.contains(gameId)) {
                // 1代表正常运行
                if (gameStatus != 1) {
                  list.remove(gameId);
                  String planGame = Joiner.on(",").join(list);
                  ((JSONObject) x).put("planGame", planGame);
                  updateRoom(x.toString());
                }
              }
            }
          });
    }
    log.info("彩票修改游戏状态成功,游戏id为:{},游戏状态为:{}", gameId, gameStatus);
  }

  public JSONArray getRoomInfoList() {
    String chatDomain = tenantDomainService.getChatDomain();
    if (org.apache.commons.lang3.StringUtils.isBlank(chatDomain)) {
      throw new ServiceException("未配服务");
    }
    String dbSuffix = tenantConfig.getTenantCode();

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
    Header[] header =
        new Header[] {
          new BasicHeader("Content-Type", "application/json"),
          new BasicHeader("plat_code", DyDataSourceContextHolder.getTenant())
        };
    try {
      HttpClientUtils.doPost(apiUrl, body, header);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String otthProxyHttpPost(
      String apiUrl, String body, HttpServletRequest request, String proxy) throws Exception {
    Header[] header =
        new Header[] {
          new BasicHeader("Content-Type", "application/json"), new BasicHeader("plat_code", proxy)
        };
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
      CompletableFuture.runAsync(
          () -> {
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
            sysLog.setCreateTime(DateUtils.get0ZoneDate(new Date(),DateUtils.DATE_TIME_PATTERN));
            sysLog.setDoTime((System.currentTimeMillis() - start) + "");
            // 保存操作日志
            remoteLogService.saveOperLog(sysLog);
          },
          asyncExecutor);
    }

    return result;
  }

  public Object otthProxyHttpGet(
      String apiUrl, HttpServletRequest request, HttpServletResponse response, PageDTO page)
      throws IOException, ServiceException {
    String dbSuffix = tenantConfig.getTenantCode();
    Enumeration<String> names = request.getParameterNames();
    Map<String, String> params = new HashMap<>();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      params.put(name, request.getParameter(name));
    }
    params.put("platCode", dbSuffix);
    params.put("page", String.valueOf(page.getCurrent()));
    params.put("rows", String.valueOf(page.getSize()));
    HttpClient httpClient = HttpClient.build().get(apiUrl);
    httpClient.setPara(params);
    httpClient.addHead("plat_code", dbSuffix);
    httpClient.addHead("Cookie", request.getHeader("Cookie"));
    HttpRespBean respBean = httpClient.execute();
    return handleResponse(respBean, response);
  }

  private Object handleResponse(HttpRespBean respBean, HttpServletResponse response)
      throws IOException {
    if (respBean.getStatus() == 200) {
      String respBody = respBean.getRespBody();
      if (StringUtils.isBlank(respBody)) {
        return new Object();
      }
      return JSON.parse(respBody);
    } else {
      try {
        JSONObject jsonObject = JSON.parseObject(respBean.getRespBody());
        if (jsonObject.containsKey("code") && jsonObject.containsKey("msg")) {
          throw new ServiceException(
              Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("msg"));
        }
      } catch (Throwable e) {
        throw new ServiceException(e.getMessage());
      }
      throw new ServiceException(respBean.getStatus(), respBean.getRespBody());
    }
  }

  /** 中奖推送接口 */
  public void pushLotteryWin(List<PushLottWinVo> lottWinVos, HttpServletRequest request) {
    // 中奖推送接口地址
    String apiUrl = getApiUrl("api/push/cpwin");
    String dbSuffix = tenantConfig.getTenantCode();
    if (dbSuffix == null) {
      throw new ServiceException("推送失败,获取不到租户标识");
    }
    List<PushCPMessageReq> list = new ArrayList<>();
    // 填充开奖必须数据
    for (PushLottWinVo lottWinVo : lottWinVos) {
      Member user =
          memberService.getByAccount(lottWinVo.getAccount().replaceAll(dbSuffix, "")).get();
      if (user == null) {
        log.info("找不到用户:" + lottWinVo.getAccount());
        continue;
      }
      PushCPMessageReq pushCPMessageReq = new PushCPMessageReq();
      pushCPMessageReq
          .setGameName(lottWinVo.getGameName())
          .setGameId(lottWinVo.getGameId())
          .setWinMoney(lottWinVo.getWinMoney())
          .setAccount(user.getAccount())
          .setUserId(user.getId());
      list.add(pushCPMessageReq);
    }

    if (list.isEmpty()) {
      throw new ServiceException("没有此用户数据");
    }
    Stream<PushCPMessageReq> stream = list.stream();
    // 获取配置数据
    PushLotteryWin pushLotteryWin =
        (PushLotteryWin)
            redisTemplate.opsForValue().get(String.format("%s_%s", dbSuffix, CHAT_PUSH_CP_WIN));
    if (pushLotteryWin == null) {
      String chatConfig = menuService.queryChatConfig(ChatConfigEnum.CHAT_PUSH_CP_WIN);
      pushLotteryWin = JSON.parseObject(chatConfig, PushLotteryWin.class);
      if (pushLotteryWin != null) {
        redisTemplate
            .opsForValue()
            .set(String.format("%s_%s", dbSuffix, CHAT_PUSH_CP_WIN), pushLotteryWin);
      }
    }
    PushLotteryWin finalPushLotteryWin = pushLotteryWin;
    // 开始过滤数据
    if (pushLotteryWin != null) {
      // if (pushLotteryWin.getIsOpen()==0){
      // return RetUtils.error("中奖推送已关闭");
      // }
      String[] lottCodes = pushLotteryWin.getVipEnterLevels().split(",");
      String[] accouts = pushLotteryWin.getBlackAccounts().split(",");

      // 过滤掉设置不推送的账号
      List<String> accountList = Arrays.asList(accouts);
      stream = stream.filter(x -> !accountList.contains(x.getAccount()));
      // 过滤掉设置不推送的游戏
      List<String> lottList = Arrays.asList(lottCodes);
      stream = stream.filter(x -> lottList.contains(x.getGameId()));
      // 过滤掉中奖金额设置
      if (pushLotteryWin.getWinMoney() != null && pushLotteryWin.getWinMoney() > 0) {
        stream = stream.filter(x -> x.getWinMoney() > finalPushLotteryWin.getWinMoney());
      }
      // 最终发送数据
      list = stream.collect(Collectors.toList());

      // 只显示中奖最高前几名
      if (pushLotteryWin.getTopNum() != null
          && pushLotteryWin.getTopNum() > 0
          && list.size() > pushLotteryWin.getTopNum()) {
        list.sort((o1, o2) -> (int) (o2.getWinMoney() - o1.getWinMoney()));
        list = list.subList(0, pushLotteryWin.getTopNum());
      }
    }

    //
    if (list.isEmpty()) {
      log.info("开奖推送数据过滤之后为空");
      return;
    }
    try {
      otthProxyHttpPost(apiUrl, JSON.toJSONString(list), request, dbSuffix);
      log.info("开奖推送成功:" + list);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("开奖推送失败,错误为:" + e);
    }
    return;
  }

  /** 分享彩票下注 */
  public void cpbet(List<PushCPBetMessageReq> req, HttpServletRequest request) {
    log.info("收到彩票服传来的分享订单" + req);
    if (req == null || req.isEmpty()) {
      throw new ServiceException("数据不能为空");
    }
    // 分享推送接口地址
    String apiUrl = getApiUrl("/api/push/cpbet");
    String dbSuffix = tenantConfig.getTenantCode();
    if (dbSuffix == null) {
      throw new ServiceException("推送失败,获取不到租户标识");
    }
    // 获取分享配置
    ChatPushCPBet config =
        (ChatPushCPBet)
            redisTemplate.opsForValue().get(String.format("%s_%s", dbSuffix, CHAT_PUSH_CP_BET));
    if (config == null) {
      String chatConfig = menuService.queryChatConfig(ChatConfigEnum.CHAT_PUSH_CP_BET);
      config = JSON.parseObject(chatConfig, ChatPushCPBet.class);
      if (config != null) {
        redisTemplate.opsForValue().set(String.format("%s_%s", dbSuffix, CHAT_PUSH_CP_BET), config);
      }
    }
    ChatPushCPBet finalConfig = config;
    req.forEach(
        x -> {
          x.setAccount(x.getAccount().replaceAll(dbSuffix, ""));
          Member user = memberService.getByAccount(x.getAccount()).get();
          if (user == null) {
            log.info("找不到用户:" + x.getAccount());
            return;
          }
          x.setUserId(user.getId());
        });
    // 过滤掉设置不推送的游戏
    if (config != null) {
      // 是否只直推会员
      if (config.getOnlyPushAccount() != null && config.getOnlyPushAccount() == 1) {
        // 直推会员的名单
        String pushAccount = config.getPushAccount();
        if (StringUtils.isNotBlank(pushAccount)) {
          String[] split = pushAccount.split(",");
          List<String> list = Arrays.asList(split);
          req =
              req.stream().filter(x -> list.contains(x.getAccount())).collect(Collectors.toList());
        }
      }
      // 推送黑名单
      if (StringUtils.isNotBlank(config.getNotPushAccount())) {
        String noPushAccount = config.getNotPushAccount();
        String[] split = noPushAccount.split(",");
        List<String> list = Arrays.asList(split);
        req = req.stream().filter(x -> !list.contains(x.getAccount())).collect(Collectors.toList());
      }

      if (req.isEmpty()) {
        throw new ServiceException("账号禁止推送到聊天室");
      }
      String statTime = DateUtils.format(new Date(), DATE_FORMAT_10);
      MemberActivationDTO memberActivationDTO = new MemberActivationDTO();
      // 总充值次数过滤
      if (config.getRechCount() != null && config.getRechCount() > 0) {
        req =
            req.stream()
                .filter(
                    x -> {
                      memberActivationDTO.setUsername(x.getAccount());
                      MemberActivationVO todayMoney =
                          rechargeOrderService.getRechargeInfoByNameAndUpdateTime(
                              memberActivationDTO);
                      if (todayMoney == null || todayMoney.getRechargeCount() == null) {
                        return false;
                      }
                      return todayMoney.getRechargeCount() > finalConfig.getRechCount();
                    })
                .collect(Collectors.toList());
      }
      if (req.isEmpty()) {
        throw new ServiceException(String.format("充值次数未达到要求%d次", finalConfig.getRechCount()));
      }
      // 总充值金额过滤
      if (config.getRechMoney() != null && config.getRechMoney().compareTo(BigDecimal.ZERO) > 0) {
        req =
            req.stream()
                .filter(
                    x -> {
                      memberActivationDTO.setUsername(x.getAccount());
                      MemberActivationVO todayMoney =
                          rechargeOrderService.getRechargeInfoByNameAndUpdateTime(
                              memberActivationDTO);
                      if (todayMoney == null || todayMoney.getRechargeMoney() == null) {
                        return false;
                      }
                      return todayMoney.getRechargeMoney().compareTo(finalConfig.getRechMoney())
                          > 0;
                    })
                .collect(Collectors.toList());
      }
      if (req.isEmpty()) {
        throw new ServiceException("总充值金额未达到要求" + finalConfig.getRechMoney() + "元");
      }
      // 当日总充值金额过滤
      if (config.getTodayRechMoney() != null
          && config.getTodayRechMoney().compareTo(BigDecimal.ZERO) > 0) {
        memberActivationDTO.setEndTime(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date())));
        memberActivationDTO.setBeginTime(statTime);
        req =
            req.stream()
                .filter(
                    x -> {
                      memberActivationDTO.setUsername(x.getAccount());
                      MemberActivationVO todayMoney =
                          rechargeOrderService.getRechargeInfoByNameAndUpdateTime(
                              memberActivationDTO);
                      if (todayMoney == null || todayMoney.getRechargeMoney() == null) {
                        return false;
                      }
                      return todayMoney
                              .getRechargeMoney()
                              .compareTo(finalConfig.getTodayRechMoney())
                          > 0;
                    })
                .collect(Collectors.toList());
      }

      if (req.isEmpty()) {
        throw new ServiceException("今日充值金额不达标,还需要" + finalConfig.getTodayRechMoney() + "元");
      }

      // 过滤只推送哪个玩法
      if (StringUtils.isNotBlank(config.getPushPlayCode())) {
        String[] split = config.getPushPlayCode().split(",");
        List<String> list = Arrays.asList(split);
        req = req.stream().filter(x -> list.contains(x.getPlayCode())).collect(Collectors.toList());
      }

      // 过滤不推送哪个玩法
      if (StringUtils.isNotBlank(config.getNotPushPlayCode())) {
        String[] split = config.getNotPushPlayCode().split(",");
        List<String> list = Arrays.asList(split);
        req =
            req.stream().filter(x -> !list.contains(x.getPlayCode())).collect(Collectors.toList());
      }

      if (req.isEmpty()) {
        throw new ServiceException("该玩法不支持分享");
      }
      // 过滤游戏
      if (StringUtils.isBlank(config.getVipEnterLevels())) {
        throw new ServiceException("该彩种不支持分享");
      } else {
        String[] split = config.getVipEnterLevels().split(",");
        List<String> list = Arrays.asList(split);
        req = req.stream().filter(x -> list.contains(x.getGameId())).collect(Collectors.toList());
      }

      if (req.isEmpty()) {
        throw new ServiceException("该彩种不支持分享");
      }
    }
    try {
      otthProxyHttpPost(apiUrl, JSON.toJSONString(req), request, dbSuffix);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException("开奖推送失败,错误为:" + e);
    }
    ;
  }

  /** 查找聊天室会员 */
  public ChatUserVO getChatUser(String account) throws Exception {
    Member user = memberService.getByAccount(account).orElseThrow(() -> new ServiceException("用户不存在"));
    String chatDomain = tenantDomainService.getChatDomain();
    if (org.apache.commons.lang3.StringUtils.isBlank(chatDomain)) {
      throw new ServiceException("未配服务");
    }
    String apiUrl = chatDomain + "/api/u/simpleUserInfoList";
    String proxy = tenantConfig.getTenantCode();
    Map<String, String> params = new HashMap<>();
    params.put("userIds", user.getId().toString());
    params.put("platCode", proxy);
    HttpClient httpClient = HttpClient.build().get(apiUrl);
    httpClient.setPara(params);
    httpClient.addHead("plat_code", proxy);
    HttpRespBean respBean = httpClient.execute();
    if (respBean != null) {
      List<ChatUserVO> chatUserVOS = JSON.parseArray(respBean.getRespBody(), ChatUserVO.class);
      if (chatUserVOS != null && !chatUserVOS.isEmpty()) {
        return chatUserVOS.get(0);
      }
    }
    throw new ServiceException("聊天室不存在此账号");
  }

  // 获取ip归属地
  public static String getAddressByIp_third(String ip) {
    String serviceUrl = IP_PCONLINE_URL + "?json=true&ip=" + ip;
    HttpClient httpClient = HttpClient.build().get(serviceUrl);
    httpClient.setEncode("GB2312");
    HttpRespBean respBean = httpClient.execute();
    String result = respBean.getRespBody(); // HttpUtils.getDocument(serviceUrl, "GBK");
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

  private String getApiUrl(String url) throws ServiceException {
    String chatDomain = tenantDomainService.getChatDomain();
    url = chatDomain + "/" + url.replace("_", "/");
    return url;
  }
}
