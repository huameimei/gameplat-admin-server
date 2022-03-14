package com.gameplat.admin.controller.internal;

import com.gameplat.admin.model.dto.PushCPBetMessageReq;
import com.gameplat.admin.model.vo.PushLottWinVo;
import com.gameplat.admin.service.ChatPushPlanService;
import com.gameplat.admin.service.OtthService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author gray
 * @description
 * @date 2022/2/16
 */
@Slf4j
@Api(tags = "彩票聊天室内部接口")
@RestController
@RequestMapping("/api/internal/admin/chat")
public class InternalOtthController {
    @Autowired
    private OtthService otthService;
    @Autowired
    private ChatPushPlanService chatPushPlanService;

    /**
     * 分享彩票下注
     */
    @RequestMapping(value = "/cpbet", method = RequestMethod.POST)
    public void cpbet(@RequestBody List<PushCPBetMessageReq> req, HttpServletRequest request) {
        otthService.cpbet(req, request);
    }

    /**
     * 中奖推送接口
     */
    @PostMapping("/pushLotteryWin")
    public void pushLotteryWin(
            @RequestBody List<PushLottWinVo> lottWinVos, HttpServletRequest request) {
        otthService.pushLotteryWin(lottWinVos, request);
    }

    /**
     * 给游戏调用的更新游戏状态
     */
    @PostMapping("updateGameStatus")
    public void updateGameStuats(String gameId, int gameStatus) {
        // 游戏维护更新自定义中奖推送
        chatPushPlanService.updatePushPlan(gameId, gameStatus);
        // 更新房间管理
        otthService.updateGameStuats(gameId, gameStatus);
    }
}
