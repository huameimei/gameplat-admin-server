package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.exception.ParaException;
import com.cz.gameplat.ftl.GameHtmlManager;
import com.cz.gameplat.game.bean.AnimalsArrayVO;
import com.cz.gameplat.game.entity.Game;
import com.cz.gameplat.game.service.AnimalsService;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.lottery.entity.Animals;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/admin/bloc/animals")
public class AnimalsController {

  @Resource
  AnimalsService animalsService;

  @Resource
  GameHtmlManager gameHtmlManager;

  @Resource
  private GameService1 gameService;

  @RequestMapping(value = "/getAnimals", method = RequestMethod.GET)
  @ResponseBody
  public List<Animals> getAnimals() {
    return animalsService.getAnimals();
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'本年生肖设置修改:'+T(com.cz.gameplat.log.LogUtil).animalTranslate(#animals)")
  public void update(@RequestBody AnimalsArrayVO animals) throws Exception {
    if (animals.getAnimals().length == 0) {
      throw new ParaException("请求参数异常");
    }
    animalsService.updatebBatch(Arrays.asList(animals.getAnimals()));
    gameHtmlManager.createSXJson();
    List<Game> games = gameService.queryAll();
    for (Game game: games) {
      if ("lhc".equalsIgnoreCase(game.getType())) {
        gameHtmlManager.gameCateHtml(game.getId(),1);
      }
    }
    return;
  }
}
