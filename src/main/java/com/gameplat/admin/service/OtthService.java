package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.PushCPBetMessageReq;
import com.gameplat.admin.model.vo.ChatUserVO;
import com.gameplat.admin.model.vo.LotteryCodeVo;
import com.gameplat.admin.model.vo.PushLottWinVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/** 聊天室业务层处理 */
public interface OtthService {

  List<LotteryCodeVo> getLottTypeList();

  JSONObject getLottConfig();

  /**
   * 获取租户标识
   * @return
   */
  public String getLottTenantCode();

  void pushChatOpen(String body);

  // 更新聊天室房间管理内的游戏状态
  void updateGameStuats(String gameId, int gameStatus);

  JSONArray getRoomInfoList();

  void updateRoom(String body);

  String otthProxyHttpPost(String apiUrl, String body, HttpServletRequest request, String proxy);

  Object otthProxyHttpGet(
      String apiUrl, HttpServletRequest request, HttpServletResponse response, PageDTO page);

  /** 中奖推送接口 */
  void pushLotteryWin(List<PushLottWinVo> lottWinVos, HttpServletRequest request);

  /** 分享彩票下注 */
  void cpbet(List<PushCPBetMessageReq> req, HttpServletRequest request);

  /** 查找聊天室会员 */
  ChatUserVO getChatUser(String account);
}
