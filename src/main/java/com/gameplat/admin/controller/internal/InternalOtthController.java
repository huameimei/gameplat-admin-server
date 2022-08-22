package com.gameplat.admin.controller.internal;

import com.gameplat.admin.model.dto.PushCPBetMessageReq;
import com.gameplat.admin.model.vo.PushLottWinVo;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.admin.service.ChatPushPlanService;
import com.gameplat.admin.service.MemberYubaoService;
import com.gameplat.admin.service.OtthService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author gray
 * @date 2022/2/16
 */
@Slf4j
@Tag(name = "彩票聊天室内部接口")
@RestController
@RequestMapping("/api/internal/admin/chat")
public class InternalOtthController {

  @Autowired private OtthService otthService;

  @Autowired private ChatPushPlanService chatPushPlanService;

  @Autowired private ActivityQualificationService activityQualificationService;

  @Autowired private MemberYubaoService memberYubaoService;

  /** 分享彩票下注 */
  @RequestMapping(value = "/cpbet", method = RequestMethod.POST)
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.LOTTERY,
    desc = "分享彩票下注")
  public void cpbet(@RequestBody List<PushCPBetMessageReq> req, HttpServletRequest request) {
    otthService.cpbet(req, request);
  }

  /** 中奖推送接口 */
  @PostMapping("/pushLotteryWin")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.LOTTERY,
    desc = "中奖推送接口")
  public void pushLotteryWin(
      @RequestBody List<PushLottWinVo> lottWinVos, HttpServletRequest request) {
    otthService.pushLotteryWin(lottWinVos, request);
  }

  /** 给游戏调用的更新游戏状态 */
  @PostMapping("updateGameStatus")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.LOTTERY,
    desc = "'KG彩票调用更新游戏状态,gameId:' + #gameId + 'gameStatus:' + #gameStatus")
  public void updateGameStuats(String gameId, int gameStatus) {
    // 游戏维护更新自定义中奖推送
    chatPushPlanService.updatePushPlan(gameId, gameStatus);
    // 更新房间管理
    otthService.updateGameStuats(gameId, gameStatus);
  }

  /**
   * 生成红包雨资格 work调用
   */
  @PostMapping("activityRedEnvelopeQualification")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.OTHER,
    desc = "生成红包雨资格")
  public void activityRedEnvelopeQualification() {
    activityQualificationService.activityRedEnvelopeQualification();
  }

  /**
   * 余额宝结算 work调用
   */
  @PostMapping("yubaoSettle")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.OTHER,
    desc = "余额宝结算")
  public void yubaoSettle() {
    memberYubaoService.settle();
  }
}
