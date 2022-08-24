package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.CollectionUtils;
import com.cz.framework.exception.BusinessException;
import com.cz.gameplat.annotation.Session;
import com.cz.gameplat.ftl.GameHtmlManager;
import com.cz.gameplat.game.bean.PlayArrayVO;
import com.cz.gameplat.game.bean.PlayCateArrayVO;
import com.cz.gameplat.game.entity.Game;
import com.cz.gameplat.game.entity.Play;
import com.cz.gameplat.game.entity.PlayCate;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.game.service.PlayCateService;
import com.cz.gameplat.game.service.PlayService;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.sys.entity.Admin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 玩法分类型
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/api/admin/bloc/play")
public class PlayController {

    @Resource
    PlayCateService playCateService;
    @Resource
    PlayService playService;
    @Resource
    GameService1 gameService;
    @Resource
    GameHtmlManager gameHtmlManager;

    /**
     * 游戏所有分类
     *
     * @param gameId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cate/getByGameId", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, List<PlayCate>> queryByTopLayer(Integer gameId, Integer model) throws Exception {
        List<PlayCate> list = this.playCateService.queryPlayCates(gameId, model);
        Map<Integer, List<PlayCate>> map = CollectionUtils.changeListToMapList(list, "superCode");
        return map;
    }

    /**
     * 游戏所有玩法
     *
     * @param gameId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getPlaysByGameId", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayCate> getByGameId(Integer gameId,
                                      Integer model, Long userId) throws Exception {
        List<PlayCate> list = this.playCateService.queryPlayCates(gameId, model, userId);
        return list;
    }

    /**
     * 获取子分类
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/getChildCates", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayCate> getPlayCatesBySuperCode(String code, Integer model) throws Exception {
        return playCateService.getCatesBySuperCode(code, model);
    }

    @RequestMapping(value = "/getPlayByCate", method = RequestMethod.GET)
    @ResponseBody
    public List<Play> getPlayByCate(String code, int rateType) throws Exception {
        return playService.getPalyByCateCode(code, rateType);
    }

    /**
     * 跟据游戏获取玩法赔率
     *
     * @param gameId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getByGameId", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, List<Play>> getPlayByGameId(Integer gameId) throws Exception {
        List<Play> list = this.playService.slaveQueryPlays(gameId);
        Map<Integer, List<Play>> map = CollectionUtils.changeListToMapList(list, "code");
        return map;
    }

    /**
     * 保存返水下注金额设置
     *
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Log(type = LogType.LOTTERY, content = "'修改系统退水金额,游戏:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#vo.gameId)")
    public void update(@RequestBody PlayCateArrayVO vo) throws Exception {
        playCateService.updatePlayCates(Arrays.asList(vo.getPlayCates()), vo.getGameId(),
                vo.getUserId());
        gameHtmlManager.createCreditJson(vo.getGameId());
        gameHtmlManager.gameCateHtml(vo.getGameId(),1);
        return;
    }

    /**
     * 修改陪率
     *
     * @param data
     * @param gameId
     * @throws Exception
     */
    @RequestMapping(value = "/savePlayMaxOdds/{gameId}", method = RequestMethod.POST)
    @ResponseBody
    @Log(type = LogType.LOTTERY, content = "'修改赔率,游戏:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)")
    public void savePlayMaxOdds(@RequestBody PlayArrayVO data, @PathVariable Integer gameId, @Session Admin admin) throws Exception {
        checkChangeOddsLimit(gameId, data, admin);
        playService.updatePlayMaxOdds(Arrays.asList(data.getPlays()));
        gameHtmlManager.createCreditJson(gameId);
        gameHtmlManager.gameCateHtml(gameId,1);
    }

    private void checkChangeOddsLimit(Integer gameId, PlayArrayVO data, Admin admin) throws Exception {
        checkChangeOddsLimit(gameService.get(gameId), data, admin);
    }

    private static final Set<String> DEV_ADMIN = Stream.of("dev001", "dev002", "dev003","open001", "open002", "open003").collect(Collectors.toSet());

    private void checkChangeOddsLimit(Game game, PlayArrayVO data, Admin admin) throws Exception {
        if (game == null) {
            throw new BusinessException("游戏不存在！");
        }
        // 非 dev 帐号且时间处于可调整范围外不允许修改赔率
        if (!isBoss(admin) && !isChangeOddsTimeValid(game.getId() == 70 || game.getId() == 76|| game.getId() == 151)) {
            throw new BusinessException("为了确保赔率的安全合理，请于【早上8点至晚上8点】进行修改操作，以便我司风控人员为您进一步核对！（香港六合彩、澳门六合彩可额外于【晚上9点30至晚上11点】操作）");
        }
        List<Play> playList = Stream.of(data.getPlays())
            .filter(p -> (p.getMaxOdds() > p.getOdds()) && (p.getLimitOdds() != 1))
            .collect(Collectors.toList());
        if (playList.size() > 0) {
            throw new BusinessException(String.format("存在%d个赔率大于允许修改的最大赔率的玩法：%s", playList.size(),playList.stream().map(Play::getValue).collect(Collectors.joining(","))));
        }
//        if (70 == game.getId()) {
//            int maxCount = 17;
//            Double maxOdds = 49D;
//            String tmCode = String.format("%s200101001", game.getId());
//            if (Stream.of(data.getPlays()).filter(p -> StringUtils.equals(p.getCode(), tmCode) && p.getMaxOdds() > maxOdds).count() > maxCount) {
//                throw new BusinessException(String.format("六合彩只允许%d个特码赔率超过%s！", maxCount, maxOdds));
//            }
//        } else if (StringUtils.equalsIgnoreCase(game.getType(), "lhc")) {
//            int maxCount = 5;
//            Double maxOdds = 49D;
//            String tmCode = String.format("%s200101001", game.getId());
//            if (Stream.of(data.getPlays()).filter(p -> StringUtils.equals(p.getCode(), tmCode) && p.getMaxOdds() > maxOdds).count() > maxCount) {
//                throw new BusinessException(String.format("六合彩只允许%d个特码赔率超过%s！", maxCount, maxOdds));
//            }
//        }
    }

    private boolean isBoss(Admin admin) {
        return DEV_ADMIN.contains(admin.getAccount()) && StringUtils.equalsIgnoreCase(admin.getType(), "OPEN");
    }

    private boolean isChangeOddsTimeValid(boolean isLhc) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // 8:00 ~ 20:00 允许修改
        boolean isTimeValid = hour >= 8 && hour < 20;
        // 香港六合彩额外允许 21:30 ~ 23:00 修改（封盘之后调整下期赔率）
        if (!isTimeValid && isLhc) {
            isTimeValid = hour == 22 || hour == 23 || (hour == 21 && calendar.get(Calendar.MINUTE) >= 30);
        }
        return isTimeValid;
    }

    /**
     * 批量修改陪率
     *
     * @param data
     * @param gameId
     * @throws Exception
     */
    @RequestMapping(value = "/batchSavePlayMaxOdds/{gameId}", method = RequestMethod.POST)
    @ResponseBody
    @Log(type = LogType.LOTTERY, content = "'批量修改同一彩票赔率,游戏：'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)")
    public void batchSavePlayMaxOdds(@RequestBody PlayArrayVO data, @PathVariable Integer gameId, @Session Admin admin) throws Exception {
        checkChangeOddsLimit(gameId, data, admin);
        //无限制的不允许同步
        List<Play> plays = Arrays.stream(data.getPlays()).filter(p -> p.getLimitOdds() != 1)
            .collect(Collectors.toList());
        playService.batchUpdatePlayMaxOdds(plays);
        gameHtmlManager.createCreditJson(gameId);
        gameHtmlManager.gameCateHtml(gameId,1);
    }

    private static final Set<String> TM_OR_YXZ = Stream.of("200101001", "202101").collect(Collectors.toSet());
    private static final Set<String> ZM_1_6 = Stream.of("200103005", "200104005", "200105005", "200106005", "200107005", "200108005","200103002","200104002","200105002","200106002","200107002","200108002").collect(Collectors.toSet());

    /**
     * 批量修改陪率根据游戏类型
     *
     * @param data
     * @param gameId
     * @throws Exception
     */
    @RequestMapping(value = "/batchSaveGamePlayMaxOdds/{gameId}", method = RequestMethod.POST)
    @ResponseBody
    @Log(type = LogType.GAME, content = "'批量修改同一彩种游戏赔率,游戏：'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)")
    public void batchSaveGamePlayMaxOdds(@RequestBody PlayArrayVO data, @PathVariable Integer gameId, @Session Admin admin) throws Exception {
        Game game = gameService.get(gameId);
        checkChangeOddsLimit(game, data, admin);
        //无限制的不允许同步
        List<Play> plays = Arrays.stream(data.getPlays()).filter(p -> p.getLimitOdds() != 1)
            .collect(Collectors.toList());
        List<Game> gameList = gameService.getGameByCate(game.getCate());
        if (StringUtils.equals("lhc", game.getType()) && Stream.of(data.getPlays()).anyMatch(p -> TM_OR_YXZ.contains(p.getCodeBase()))) {
            // 2019-11-30 花花
            // 香港六合彩、极速六合彩互不同步特码、一肖中玩法赔率
            List<Game> lhc = gameList.stream().filter(g -> g.getId() == 70 || g.getId() == 76).collect(Collectors.toList());
            List<Game> jsLhc = gameList.stream().filter(g -> g.getId() != 70 && g.getId() != 76).collect(Collectors.toList());
            List<Play> lhcPlays;
            List<Play> jsLhcPlays;
            if (game.getId() == 70 || game.getId() == 76) {
                lhcPlays = plays;
                jsLhcPlays = lhcPlays.stream().filter(p -> !TM_OR_YXZ.contains(p.getCodeBase())).collect(Collectors.toList());
            } else {
                jsLhcPlays = plays;
                lhcPlays = jsLhcPlays.stream().filter(p -> !TM_OR_YXZ.contains(p.getCodeBase())).collect(Collectors.toList());
            }
            playService.batchUpdateGamePlayMaxOdds(lhcPlays, lhc);
            playService.batchUpdateGamePlayMaxOdds(jsLhcPlays, jsLhc);
        } else if (StringUtils.equals("lhc", game.getType()) && Stream.of(data.getPlays()).anyMatch(p -> ZM_1_6.contains(p.getCodeBase()))) {
            // 2020-04-06 花花
            // 新加坡六合彩、其它六合彩互不同步正码1-6玩法赔率
            List<Game> xjpLhc = gameList.stream().filter(g -> g.getId() == 146).collect(Collectors.toList());
            List<Game> otherLhc = gameList.stream().filter(g -> g.getId() != 146).collect(Collectors.toList());
            List<Play> xjpLhcPlays;
            List<Play> otherLhcPlays;
            if (game.getId() == 146) {
                xjpLhcPlays = plays;
                otherLhcPlays = xjpLhcPlays.stream().filter(p -> !ZM_1_6.contains(p.getCodeBase())).collect(Collectors.toList());
            } else {
                otherLhcPlays = plays;
                xjpLhcPlays = otherLhcPlays.stream().filter(p -> !ZM_1_6.contains(p.getCodeBase())).collect(Collectors.toList());
            }
            playService.batchUpdateGamePlayMaxOdds(xjpLhcPlays, xjpLhc);
            playService.batchUpdateGamePlayMaxOdds(otherLhcPlays, otherLhc);
        } else {
            playService.batchUpdateGamePlayMaxOdds(plays, gameList);
        }
        for (Game game1 : gameList) {
            gameHtmlManager.createCreditJson(game1.getId());
            gameHtmlManager.gameCateHtml(game1.getId(),1);
        }
    }
}
