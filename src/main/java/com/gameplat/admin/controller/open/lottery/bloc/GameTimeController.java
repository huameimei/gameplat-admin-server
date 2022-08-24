package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.ValidatorUtil;
import com.cz.framework.exception.BusinessException;
import com.cz.framework.exception.ParaException;
import com.cz.gameplat.annotation.Session;
import com.cz.gameplat.game.bean.GameTimesArrayVO;
import com.cz.gameplat.game.entity.Game;
import com.cz.gameplat.game.entity.GameTime;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.game.service.GameTimeService;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.sys.entity.Admin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 游戏
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/admin/gameTime")
public class GameTimeController {

  @Resource
  private GameService1 gameService;
  @Resource
  private GameTimeService gameTimeService;

  /**
   *
   * @return
   * @throws ParaException
   */
  @RequestMapping(value = "/getListByGameId", method = RequestMethod.GET)
  @ResponseBody
  public List<GameTime> getListByGameId(Integer gameId) throws ParaException {
    if (ValidatorUtil.isNull(gameId)) {
      throw new ParaException("请求参数异常");
    }
    return gameTimeService.getByGameId(gameId);
  }

  /**
   * 保存返水下注金额设置
   * @throws Exception
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.GAME, content = "'游戏时间修改,游戏:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#vo.gameId)")
  public void update(@RequestBody GameTimesArrayVO vo, @Session Admin admin) throws Exception {
    if (ValidatorUtil.isNull(vo.getGameTimes(), vo.getGameId())) {
      throw new ParaException("请求参数异常");
    }
    Game game = gameService.get(vo.getGameId());
    if (("lhc".equals(game.getType()) && (game.getJsType() == 0 || game.getId() == 76)) || StringUtils.equals("OPEN", admin.getType())) {
      // 除了官方六合彩、澳门六合彩外,其他时间不允许客户修改
      gameTimeService.updateBatch(Arrays.asList(vo.getGameTimes()), vo.getGameId());
    } else {
      throw new BusinessException("游戏时间不允许修改，请联系技术客服");
    }
  }

}
