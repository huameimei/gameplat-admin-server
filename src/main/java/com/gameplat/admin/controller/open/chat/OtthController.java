package com.gameplat.admin.controller.open.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.RoomMember;
import com.gameplat.admin.model.dto.PushCPBetMessageReq;
import com.gameplat.admin.model.vo.ChatUserVO;
import com.gameplat.admin.model.vo.LotteryCodeVo;
import com.gameplat.admin.model.vo.PushLottWinVo;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.json.JsonUtils;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */
@Slf4j
@Api(tags = "聊天室侧滑菜单管理")
@RestController
@RequestMapping("/api/admin/chat")
public class OtthController {

  public static final String API_PLAT_UPDATE = "api_plat_update";
  private static final String ROOM_MEMBER_BATCHADD_URL = "api_room_batchAddMember";
  private static final String API_ROOM_UPDATE = "api_room_update";
  @Autowired private OtthService otthService;
  @Autowired private SysDomainService sysDomainService;
  @Autowired private ChatLeaderBoardService chatLeaderBoardService;
  @Autowired private SysTenantSettingService sysTenantSettingService;
  @Autowired private ChatPushPlanService chatPushPlanService;
  //@Autowired private TenantConfig tenantConfig;

  @ApiOperation(value = "聊天室排行榜热任务")
  @GetMapping(value = "/chatLeaderBoardTask", produces = MediaType.APPLICATION_JSON_VALUE)
  public void get() {
    chatLeaderBoardService.creatLeaderBoard(null);
  }

  @SneakyThrows
  @ApiOperation(value = "平台聊天室限制配置/聊天室成员管理/关键词管理/聊天室房间管理/角色管理/聊天室自定义消息管理")
  @PostMapping(value = "/{url}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('chat:edit')")
  public String post(
      @PathVariable String url, @RequestBody String body, HttpServletRequest request) {
    String apiUrl = getApiUrl(url);
    // 获取当前租户标识
    String dbSuffix = otthService.getLottTenantCode();
    // 聊天室批量添加请求特殊处理处理
    if (StringUtils.contains(url, ROOM_MEMBER_BATCHADD_URL)) {
      body = dealAddRoomMemberBody(body, dbSuffix);
    }
    // 聊天室修改平台开关特殊处理处理
    if (StringUtils.contains(url, API_PLAT_UPDATE)) {
      otthService.pushChatOpen(body);
      updateChatEnable(body);
    }
    // 聊天室房间管理特殊处理
    if (StringUtils.contains(url, API_ROOM_UPDATE)) {
      body = checkGameStatus(body);
    }
    log.info("API=" + apiUrl + ":" + body);
    return otthService.otthProxyHttpPost(apiUrl, body, request, dbSuffix);
  }

  @ApiOperation(value = "平台聊天室限制配置/聊天室成员管理/关键词管理/聊天室房间管理/角色管理/聊天室自定义消息管理")
  @GetMapping(value = "/{url}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('chat:get')")
  public Object get(
      @PathVariable String url,
      HttpServletRequest request,
      HttpServletResponse response,
      PageDTO<?> page)
      throws Exception {
    try {
      return otthService.otthProxyHttpGet(getApiUrl(url), request, response, page);
    } catch (Exception e) {
      log.error(" 聊天室聊接口异常 {}",e);
      throw e;
    }
  }

  @ApiOperation(value = "获取彩票游戏类型")
  @GetMapping(value = "/getLottTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('chat:ottType:list')")
  public List<LotteryCodeVo> getLottTypeList() throws Exception {
    return otthService.getLottTypeList();
  }

  @ApiOperation(value = "中奖推送接口")
  @PostMapping("/pushLotteryWin")
  @PreAuthorize("hasAuthority('chat:push')")
  public void pushLotteryWin(
      @RequestBody List<PushLottWinVo> lottWinVos, HttpServletRequest request) {
    otthService.pushLotteryWin(lottWinVos, request);
  }

  @ApiOperation(value = "分享彩票下注")
  @RequestMapping(value = "/cpbet", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('chat:share:bet')")
  public void cpbet(@RequestBody List<PushCPBetMessageReq> req, HttpServletRequest request) {
    otthService.cpbet(req, request);
  }

  @ApiOperation(value = "修改平台聊天室开关")
  @PreAuthorize("hasAuthority('chat:edit:flag')")
  private void updateChatEnable(String body) {
    JSONObject json = JSONObject.parseObject(body);
    Integer chatOpen = json.getInteger("chatOpen");
    String cpChatEnable = EnableEnum.DISABLED.match(chatOpen) ? "off" : "on";
    sysTenantSettingService.updateChatEnable(cpChatEnable);
  }

  @SneakyThrows
  @ApiOperation(value = "查找聊天室会员")
  @PreAuthorize("hasAuthority('chat:room:member')")
  @RequestMapping(value = "/getChatUser", method = RequestMethod.GET)
  public ChatUserVO getChatUser(String account) {
    return otthService.getChatUser(account);
  }

  @ApiOperation(value = "给游戏调用的更新游戏状态")
  @PostMapping("updateGameStatus")
  @PreAuthorize("hasAuthority('chat:game:updateGameStatus')")
  public void updateGameStuats(String gameId, int gameStatus) {
    // 游戏维护更新自定义中奖推送
    chatPushPlanService.updatePushPlan(gameId, gameStatus);
    // 更新房间管理
    otthService.updateGameStuats(gameId, gameStatus);
  }

  private String getApiUrl(String url) {
    String chatDomain = sysDomainService.getChatDomain();
    url = chatDomain + "/" + url.replace("_", "/");
    return url;
  }

  private String dealAddRoomMemberBody(String body, String plateCode) {
    List<RoomMember> roomMembers = JSONArray.parseArray(body, RoomMember.class);
    roomMembers.forEach(
        roomMember -> {
          roomMember.setPlatCode(plateCode);
          // 設置會員層級與平臺層級一樣
          roomMember.setLevel(1);
        });
    return JsonUtils.writeValue(roomMembers);
  }

  private String checkGameStatus(String body) {
    List<LotteryCodeVo> lottTypeList = otthService.getLottTypeList();
    List<String> lottCodes =
        lottTypeList.stream().map(LotteryCodeVo::getLottCode).collect(Collectors.toList());
    JSONObject jsonObject = JSONObject.parseObject(body);
    String game = jsonObject.getString("planGame");
    if (StringUtils.isNotBlank(game)) {
      List<String> list = new ArrayList<>();
      Collections.addAll(list, game.split(","));
      list.removeIf(x -> !lottCodes.contains(x));
      jsonObject.put("planGame", Joiner.on(",").join(list));
    }
    return jsonObject.toJSONString();
  }
}
