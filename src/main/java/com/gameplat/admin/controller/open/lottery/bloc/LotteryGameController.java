package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.AppContext;
import com.cz.framework.JsonUtil;
import com.cz.framework.StringUtil;
import com.cz.framework.exception.BusinessException;
import com.cz.framework.exception.ParaException;
import com.cz.gameplat.annotation.Session;
import com.cz.gameplat.ftl.GameHtmlManager;
import com.cz.gameplat.game.bean.DefaultPlaySettingArrayVO;
import com.cz.gameplat.game.entity.Game;
import com.cz.gameplat.game.enums.GameCollectTypes;
import com.cz.gameplat.game.enums.GameJsTypes;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.game.service.PlayCateService;
import com.cz.gameplat.game.service.PlayQuotaComService;
import com.cz.gameplat.game.service.PlayService;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.lottery.bean.GameBean;
import com.cz.gameplat.lottery.enums.Lottery.BanWay;
import com.cz.gameplat.lottery.enums.Lottery.IsBan;
import com.cz.gameplat.lottery.enums.PlayType;
import com.cz.gameplat.sys.entity.Admin;
import com.cz.gameplat.sys.entity.Config;
import com.cz.gameplat.sys.service.ConfigService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 游戏
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/admin/bloc/game")
public class LotteryGameController {
  @Resource
  PlayCateService playCateService;
  @Resource
  PlayService playService;
  @Resource
  ConfigService configService;
  @Resource
  GameService1 gameService;
  @Resource
  PlayQuotaComService playQuotaComService;

  @Resource
  GameHtmlManager gameHtmlManager;

  /**
   *
   * @return
   */
  @RequestMapping(value = "/getGameList", method = RequestMethod.GET)
  @ResponseBody
  public List<Game> getGameList(Integer model) {
	  return gameService.getGameList(model);
  }

  @RequestMapping(value = "/getGameUnion", method = RequestMethod.GET)
  @ResponseBody
  public List<Game> getGameList(Integer isOffcial, Integer isCredit) {
	  return gameService.getGameList( isOffcial,  isCredit);
  }

  /**
   *
   * @return
   */
  @RequestMapping(value = "/getGameListOpen", method = RequestMethod.GET)
  @ResponseBody
  public List<Game> getGameListOpen() {
    return gameService.queryAvailable(0,0);
  }
  /**
   *
   * @param admin
   * @param game
   * @throws Exception
   */
  @RequestMapping(value = "/settings", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.GAME, content = "'彩票设置,游戏:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#game.id)+',修改类型：'+T(com.cz.gameplat.log.LogUtil).gameSettingTypeTranslate(#game)")
  public void settings(@Session Admin admin, Game game) throws Exception {
    Game info = gameService.get(game.getId());
    if(null == info){
      throw new Exception("彩种信息不存在");
    }
    //更新游戏名称
    if(null != game.getName() && game.getName() != info.getName()){
      if (game.getName().length() <2 || game.getName().length() >8) {
        throw new Exception("游戏名称长度限制2-8位");
      }
      gameService.updateGameName(game.getId(), game.getName());
    }
    if (info.getRules().equals("additive")) {
      gameService.update(game);
    }
    if(game.getSort() != null){
       gameService.updateSort(game.getId(),game.getSort());
    }
    if(game.getCurTurnNum() != null){
      gameService.updateCurTurnNum(game.getId(),game.getCurTurnNum());
    }

    if (game.getIsBan() != null) {
      if(gameService.updateGameIsBan(game.getId(), game.getIsBan())>0){
        gameService.gameBanLog(game.getId(), BanWay.Manul, IsBan.get(game.getIsBan()), admin);
      }
    }
    if (game.getOpen() != null) {
      gameService.updateGameOpen(game.getId(), game.getOpen());
    }
    if (game.getRestEndDate() != null) {
      gameService.updateRestEndDate(game.getId(), game.getRestEndDate());
    }
    if (game.getRestStartDate() != null) {
      gameService.updateRestStartDate(game.getId(), game.getRestStartDate());
    }
  }

  /**
   * 修改公休日期
   *
   * @param admin
   * @param
   * @throws Exception
   */
  @RequestMapping(value = "/updateGameRestDate", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.GAME, content = "'修改彩票公休时间,游戏:'+T(com.cz.gameplat.log.LogUtil).gameUpdateTypeTranslate(#bean)+'，公休开始时间：'+T(com.cz.gameplat.log.LogUtil).DateToStr(#bean.restStartDate)+'，公休结束时间：'+T(com.cz.gameplat.log.LogUtil).DateToStr(#bean.restEndDate)")
  public void updateGameRestDate(@Session Admin admin, @RequestBody GameBean bean)
      throws Exception {
    List<Integer> ids = bean.getGameIdList();
    Date restStartDate = bean.getRestStartDate();
    Date restEndDate = bean.getRestEndDate();
    for (Integer id : ids) {
      gameService.updateGameRestDate(id, restStartDate, restEndDate);
      this.gameHtmlManager.gameCateHtml(id,1);
    }
    // operateLogService.saveOperateLog(userInfo.getUserId(), userInfo.getUserName(),
    // "设置游戏公休时间和排序号，游戏ID：" + game.getId());
  }

  @RequestMapping(value = "/getGameByCate", method = RequestMethod.GET)
  @ResponseBody
  public List<Game> getGameByCate(Integer cate) {
    return gameService.getGameByCate(cate);
  }

  @RequestMapping(value = "/cate", method = RequestMethod.GET)
  @ResponseBody
  public void cateHtml(Integer gameId) throws Exception {
    if (gameId == null) {
      throw new ParaException("请求参数异常");
    }
    this.gameHtmlManager.gameCateHtml(gameId, 1);
  }

  /**
   * 修改极速采集类型
   * @param admin
   * @param game
   * @throws Exception
   */
  @RequestMapping(value = "/updateCollectType", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.GAME, content = "'修改采集类型,游戏:'+#game.id")
  public void updateCollectType(@Session Admin admin, Game game, String vsCode) throws Exception {
    Game info = gameService.get(game.getId());
    if(info == null || GameJsTypes.JS.getValue() != info.getJsType()){
    	throw new BusinessException("GAME/UPDATE_JS", "游戏不是极速类型不能修改", null);
    }
    if(game.getCollectType() == GameCollectTypes.MANUAL.getValue()){ // 改手动模式时需要受权码
        if (StringUtil.isBlank(vsCode)) {
            throw new BusinessException("请输入授权码");
        }
        // 公共授权码和客户授权码都可以开启手动模式
//    	if(!GoogleAuth.authcode(vsCode, "BYU6NJ7JSEP36TPC", 10)) {
//            Config config = configService.getByNameAndKey("system_config", "collect_type_secret");
//            if (config == null || StringUtil.isBlank(config.getConfigValue()) || "-1".equals(config.getConfigValue())
//                    || !GoogleAuth.authcode(vsCode, config.getConfigValue(), 10)) {
//                throw new BusinessException("GAME/UPDATE_JS_VS_CODE_ERROR", "受权码不正确", null);
//            }
//    	}
    }

    gameService.updateCollectType(game);
  }

  @RequestMapping(value = "/setGameDefaultPlay", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.GAME,content = "#settingArrayVO.log")
  public void setGameDefaultPlay(@RequestBody DefaultPlaySettingArrayVO settingArrayVO) throws BusinessException {
      try {
          List<Map<String, String>> list = Stream.of(settingArrayVO.settings).map(setting -> {
              Map<String, String> map = new HashMap<>();
              map.put("code", setting.getGameCode());
              map.put("credit", setting.getCreditCode());
              map.put("official", setting.getOfficialCode());
              return map;
          }).collect(Collectors.toList());
          String fileDetailPath = AppContext.getInstance().getSysRealPath() + "data/json/game_play_setting_detail.json";
          String filePath = AppContext.getInstance().getSysRealPath() + "data/json/game_play_setting.json";
          FileUtils.writeStringToFile(new File(fileDetailPath), JsonUtil.toJson(settingArrayVO.settings), "utf-8");
          FileUtils.writeStringToFile(new File(filePath), JsonUtil.toJson(list), "utf-8");
      } catch (IOException e) {
          throw new BusinessException(e.getMessage());
      }
  }

  /**
   * 启用禁用官方玩法
   * @param game
   * @throws Exception
   */
  @RequestMapping(value = "/updateOfficial", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.GAME, content = "(#game.isOffcial==1?'启用':'禁用')+'官方玩法,游戏:'+#game.name")
  public void updateOfficial( Game game) throws Exception {
    Game info = gameService.get(game.getId());
    if(!((info.getPlayType()& PlayType.OFFCIAL.getValue())== PlayType.OFFCIAL.getValue())){
      throw new BusinessException("官方玩法不存在");
    }
    gameService.updateOfficial(game);
  }

}
