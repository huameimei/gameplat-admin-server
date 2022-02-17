package com.gameplat.admin.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gameplat.common.enums.ChatConfigEnum;
import com.gameplat.admin.mapper.ChatLeaderBoardMapper;
import com.gameplat.admin.model.doc.ChatPushCPBet;
import com.gameplat.admin.model.domain.ChatLeaderBoard;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.vo.ChatLeaderBoardVO;
import com.gameplat.admin.service.*;
import com.gameplat.common.util.HttpClient;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.HttpRespBean;
import com.gameplat.common.util.HttpClientUtils;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.alibaba.excel.util.DateUtils.DATE_FORMAT_10;
import static java.util.Arrays.asList;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatLeaderBoardServiceImpl extends ServiceImpl<ChatLeaderBoardMapper, ChatLeaderBoard> implements ChatLeaderBoardService {

    private String apiUrl = "/api/push/cpwin/one";
    private String lottUrl = "/api-manage/chatRoom/getAccountWinAndLoses";

    @Autowired
    private GameConfigService gameConfigService;

    @Autowired
    private ChatSideMenuService chatSideMenuService;

    @Autowired
    private ChatLeaderBoardMapper chatLeaderBoardMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TenantDomainService tenantDomainService;

    /** 聊天室彩票下注推送配置 */
    public static final String CHAT_PUSH_CP_BET = "CHAT_PUSH_CP_BET";
    /** 聊天室排行榜 */
    public static final String CHAT_LEADER_BOARD = "CHAT_LEADER_BOARD";

    /** 创建聊天室排行榜 */
    @Scheduled(cron = "0 0/1 * * * ?")
    @Override
    public void creatLeaderBoard(String jsonStrParam) throws ServiceException {
        log.info("开始创建排行榜ing"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String statTime = DateUtils.format(new Date(), DATE_FORMAT_10);
        JSONObject newLottConfig = gameConfigService.getGameConfig("kgnl");
        String proxy = newLottConfig.getString("proxy");
        JSONArray req = getChatRoom(proxy);
        if (req != null && req.size() > 0) {
            req.forEach(a -> {
                if (((JSONObject)a).getIntValue("isOpenLeaderboard") != 0) {
                    String games = ((JSONObject)a).getString("planGame");
                    if (StringUtils.isBlank(games)) {
                        return;
                    }
                    String[] split = games.split(",");
                    List<String> gameIds = asList(split);
                    Long id = ((JSONObject)a).getLong("id");
                    String leaderBoard = ((JSONObject)a).getString("leaderBoard");
                    List<ChatLeaderBoardVO> result = new ArrayList<>();
                    if (StringUtil.isNotBlank(leaderBoard)) {
                        List<ChatLeaderBoardVO> leaderBoardVOS = JSON.parseArray(leaderBoard, ChatLeaderBoardVO.class);
                        result.addAll(leaderBoardVOS);
                    }
                    Map<String, ChatLeaderBoardVO> resultMap =
                            result.stream().collect(Collectors.toMap(ChatLeaderBoardVO::getAccount, s -> s));
                    // 添加会员报表数据
                    List<ChatLeaderBoardVO> queryResult = slaveLottWin(newLottConfig, games);
                    if (queryResult != null && queryResult.size() > 0) {
                        // 重复账号盈利金额累加
                        for (ChatLeaderBoardVO l : queryResult) {
                            if (resultMap.get(l.getAccount()) != null) {
                                ChatLeaderBoardVO n = resultMap.get(l.getAccount());
                                n.setWinMoney(n.getWinMoney() + l.getWinMoney());
                            } else {
                                resultMap.put(l.getAccount(), l);
                            }
                        }
                    }


                    // 添加自定义推送中奖信息的会员数据
                    List<ChatLeaderBoardVO> pushWinList = chatLeaderBoardMapper.slaveQueryPushWinReport(id, gameIds, statTime);
                    if (pushWinList != null && pushWinList.size() > 0) {
                        // 重复账号盈利金额累加
                        for (ChatLeaderBoardVO l : pushWinList) {
                            if (resultMap.get(l.getAccount()) != null) {
                                ChatLeaderBoardVO n = resultMap.get(l.getAccount());
                                Double money = n.getWinMoney() + l.getWinMoney() / 2;
                                n.setWinMoney(money);
                            } else {
                                resultMap.put(l.getAccount(), l);
                            }
                        }
                    }

                    result = new ArrayList<>(resultMap.values());
                    //聊天室彩票下注推送配置
                    ChatPushCPBet chatPushCPBet = (ChatPushCPBet)redisTemplate.opsForValue().get(String.format("%s_%s", proxy, CHAT_PUSH_CP_BET));

                    int totalCount = 30;

                    if (chatPushCPBet == null) {
                        String chatConfig = chatSideMenuService.queryChatConfig(ChatConfigEnum.CHAT_PUSH_CP_BET);
                        chatPushCPBet = JSON.parseObject(chatConfig, ChatPushCPBet.class);
                        redisTemplate.opsForValue().set(String.format("%s_%s", proxy, CHAT_PUSH_CP_BET), chatPushCPBet);
                    }

                    if (chatPushCPBet != null) {
                        // 去掉排行榜黑名单会员
                        String leaderBoardBlackAccount = chatPushCPBet.getLeaderBoardBlackAccount();
                        if (StringUtils.isNotBlank(leaderBoardBlackAccount)) {
                            List<String> blackAccount = asList(leaderBoardBlackAccount.split(","));
                            if (blackAccount.size() > 0) {
                                result = result.stream().filter(r -> !blackAccount.contains(r.getAccount()))
                                        .collect(Collectors.toList());
                            }
                        }

                        if (chatPushCPBet.getLeaderBoardTotalCount() != null
                                && chatPushCPBet.getLeaderBoardTotalCount() != 0) {
                            totalCount = chatPushCPBet.getLeaderBoardTotalCount();
                        }
                    }
                    List<ChatLeaderBoardVO> chatLeaderBoardVOS = new ArrayList<>();
                    // 排行榜最高显示数
                    if (result.size() > 0) {
                        // 按输赢金额由大到小排序，默认只取前30位
                        result = result.stream().filter(b -> b.getWinMoney() > 0)
                                .sorted(Comparator.comparing(ChatLeaderBoardVO::getWinMoney).reversed())
                                .collect(Collectors.toList());
                        result = new ArrayList<>(result.subList(0, Math.min(result.size(), totalCount)));
                        List<String> userIds = result.stream().map(ChatLeaderBoardVO::getUserId).map(Object::toString)
                                .collect(Collectors.toList());
                        try {
                            // 查找聊天室现有成员，并设置排行榜头像昵称等必要信息
                            JSONArray userList = getChatUser(String.join(",", userIds), proxy);
                            if (userList != null && !userList.isEmpty()) {
                                result.forEach(c -> {
                                    userList.forEach(u -> {
                                        Long userId = ((JSONObject)u).getLong("userId");
                                        String avatar = ((JSONObject)u).getString("avatar");
                                        String nickName = ((JSONObject)u).getString("nickName");
                                        if (c.getUserId().equals(userId)) {
                                            c.setAvatar(avatar);
                                            c.setNickName(nickName);
                                            c.setWinMoney(Double.valueOf(String.format("%.2f", c.getWinMoney())));
                                            chatLeaderBoardVOS.add(c);
                                        }
                                    });
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (chatLeaderBoardVOS.isEmpty()) {
                        return;
                    }
                    // 排行榜排序
                    chatLeaderBoardVOS.sort((o1, o2) -> (int)(o2.getWinMoney() - o1.getWinMoney()));
                    redisTemplate.opsForValue().set(String.format("%s_%s_%s", proxy, id.toString(), CHAT_LEADER_BOARD), JSON.toJSONString(chatLeaderBoardVOS), 2, TimeUnit.MINUTES);
                    log.info("创建排行榜结束,房间id为{},内容为{}", id, chatLeaderBoardVOS);
                }
            });
        }

    }

    private List<ChatLeaderBoardVO> slaveLottWin(JSONObject json, String gameIds) {
        String host = json.getString("host");
        String platform = json.getString("platform");
        String proxy = json.getString("proxy");
        String url = host + "/" + lottUrl;
        String cpWin = null;
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("codes", gameIds);
            Header[] header = getHeader(platform + ":" + proxy);
            cpWin = HttpClientUtils.doPost(url, map, header);
            JSONObject jsonObject = JSONObject.parseObject(cpWin);
            Integer code = (Integer)jsonObject.get("code");
            if (code == 200) {
                String data = jsonObject.getString("data");
                List<ChatLeaderBoardVO> chatLeaderBoardVOS = JSON.parseArray(data, ChatLeaderBoardVO.class);
                if (!chatLeaderBoardVOS.isEmpty()) {
                    // 把彩票的账号名称转换成平台的
                    for (ChatLeaderBoardVO x : chatLeaderBoardVOS) {
                        x.setAccount(x.getAccount().replaceAll(proxy, ""));
                    }
                    List<String> accounts =
                            chatLeaderBoardVOS.stream().map(ChatLeaderBoardVO::getAccount).collect(Collectors.toList());

                    List<Member> userList = new ArrayList<>();
                    accounts.forEach(account -> userList.add(memberService.getByAccount(account).get()));
                    // 把id和username设置成键值对的关系
                    Map<String, Member> resultMap =
                            userList.stream().collect(Collectors.toMap(Member::getAccount, s -> s));
                    // 查到平台的userId并设置进去
                    for (ChatLeaderBoardVO x : chatLeaderBoardVOS) {
                        Member user = resultMap.get(x.getAccount());
                        x.setUserId(user.getId());
                    }
                }
                log.info("彩票服输赢记录为" + chatLeaderBoardVOS);
                return chatLeaderBoardVOS;
            }
        } catch (Exception e) {
            log.info("彩票服输赢记录获取失败");
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getChatRoom(String proxy) throws ServiceException {
        String chatDomain = tenantDomainService.getChatDomain();
        if (StringUtils.isBlank(chatDomain)) {
            throw new ServiceException("未配服务");
        }
        String apiUrl = chatDomain + "/api/room/query";

        Map<String, String> params = new HashMap<>();
        params.put("platCode", proxy);
        HttpClient httpClient = HttpClient.build().get(apiUrl);
        httpClient.setPara(params);
        httpClient.addHead("plat_code", proxy);
        HttpRespBean respBean = httpClient.execute();
        if (respBean != null) {
            JSONArray chatRoomList = JSONArray.parseArray(respBean.getRespBody());
            return chatRoomList;
        }
        return null;
    }

    public JSONArray getChatUser(String ids, String proxy) throws Exception {
        String chatDomain = tenantDomainService.getChatDomain();
        if (StringUtils.isBlank(chatDomain)) {
            throw new ServiceException("未配服务");
        }
        String apiUrl = chatDomain + "/api/u/simpleUserInfoList";

        Map<String, String> params = new HashMap<>();
        params.put("platCode", proxy);
        params.put("userIds", ids);
        HttpClient httpClient = HttpClient.build().get(apiUrl);
        httpClient.setPara(params);
        httpClient.addHead("plat_code", proxy);
        HttpRespBean respBean = httpClient.execute();
        if (respBean != null) {
            JSONArray chatUserList = JSONArray.parseArray(respBean.getRespBody());
            return chatUserList;
        }
        return null;
    }

    private Header[] getHeader(String tenant) {
        return new Header[] {new BasicHeader("client", "0"), new BasicHeader("locale", "zh"),
                new BasicHeader("attribution", tenant),};
    }
}
