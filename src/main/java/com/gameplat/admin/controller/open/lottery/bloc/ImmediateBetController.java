package com.gameplat.admin.controller.open.lottery.bloc;

import com.alibaba.fastjson.JSONObject;
import com.cz.gameplat.game.cache.ImmeBetCache;
import com.cz.gameplat.game.entity.ImmeConfig;
import com.cz.gameplat.game.entity.Lottery;
import com.cz.gameplat.game.mamager.LhcManager;
import com.cz.gameplat.game.service.ImmediateBetsService;
import com.cz.gameplat.lottery.bean.GameOpenInfo;
import com.cz.gameplat.lottery.service.LotteryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 彩票游戏即时注单的Controller层
 *
 * @describe:
 * @author:
 * @date:
 * @jdk:1.6
 * @version:1.0
 */
@Validated
@Controller
@RequestMapping("/api/admin/bloc/immediateBets")
public class ImmediateBetController {

  @Resource
  private ImmediateBetsService immediateBetsService;

  @Resource
  private LhcManager lhcManager;

  @Resource
  private ImmeBetCache immeBetCache;

  @Resource
  private LotteryService lotteryService;

  @RequestMapping("/query")
  @ResponseBody
  public Map<String, Object> query(Integer gameId, String turnNum, Integer immeConfigId,String searchAccount,String searchType) throws Exception {
    GameOpenInfo cur = null;
    if (StringUtils.isBlank(turnNum)) {
      cur = lotteryService.getCurLottery(gameId);
      turnNum = cur.getTurnNum();
    }
    return StringUtils.isBlank(searchAccount) ? immeBetCache.getImmeBets(gameId, turnNum, immeConfigId, cur) : immeBetCache.getImmeBets(gameId, turnNum, immeConfigId, searchAccount,searchType, cur);
  }

  @RequestMapping("/queryLhcWinOrLose")
  @ResponseBody
  public Double queryLhcWinOrLose(Integer gameId, String turnNum, String playCateCodes)
      throws Exception {
    return immediateBetsService.queryImmediateLhcWinOrLoseMoney(gameId, turnNum, playCateCodes);
  }

  @RequestMapping("/queryLhcShengxiao")
  @ResponseBody
  public Map<String, Object> queryLhcShengxiao() throws Exception {
    return lhcManager.getInfo();
  }

  @RequestMapping("/queryCurLottery")
  @ResponseBody
  public GameOpenInfo queryCurLottery(Integer gameId) throws Exception {
    return immediateBetsService.queryCurLottery(gameId);
  }

  @RequestMapping("/query/immeConfig")
  @ResponseBody
  public List<ImmeConfig> query(Integer gameId, Integer superId, Integer isPlay) throws Exception {
    return immediateBetsService.queryAll(gameId, superId, isPlay);
  }
  @RequestMapping("/getImmediateBets")
  @ResponseBody
  public List<JSONObject> getImmediateBets(Integer gameId, String turnNum){
    return immediateBetsService.queryImmediateBet(gameId,turnNum);
  }
  @RequestMapping("/getImmediateGames")
  @ResponseBody
  public List<Lottery> getImmediateGames(){
    return immediateBetsService.queryImmediateBetGames();
  }
}
