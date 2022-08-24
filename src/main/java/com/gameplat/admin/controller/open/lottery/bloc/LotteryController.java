package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.DateUtil;
import com.cz.framework.bean.PageBean;
import com.cz.framework.bean.PageData;
import com.cz.framework.exception.BusinessException;
import com.cz.framework.exception.ParaException;
import com.cz.gameplat.annotation.Session;
import com.cz.gameplat.game.bean.GatherLottery;
import com.cz.gameplat.game.bean.UnsettledLottery;
import com.cz.gameplat.game.entity.Game;
import com.cz.gameplat.game.entity.Lottery;
import com.cz.gameplat.game.entity.PreLottery;
import com.cz.gameplat.game.enums.LotteryStatus;
import com.cz.gameplat.game.mamager.GameWinManager;
import com.cz.gameplat.game.service.GameLotteryService;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.lottery.service.LotteryService;
import com.cz.gameplat.sys.entity.Admin;
import com.cz.gameplat.sys.service.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 开奖结果处理
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/api/admin/lottery")
public class LotteryController {

  @Resource
  private LotteryService lotteryService;
  @Resource
  private GameLotteryService gameLotteryService;
  @Resource
  private GameWinManager gameWinManager;
  @Resource
  private GameService1 gameService;
  @Resource
  private ConfigService configService;


  @RequestMapping(value = "/del", method = RequestMethod.POST)
  @ResponseBody
  public void del(Long id) throws Exception {
    if (id == null) {
      throw new ParaException("请求参数异常");
    }
    // lotteryService.delLhcLottery(id);
  }

  /**
   * 结算或重新结算
   */
  @RequestMapping(value = "/manual", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'开奖结果结算,结算彩种:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)+',期号：'+#turnNum")
  public void manual(Long id, int gameId, String turnNum, @Session Admin admin) throws Exception {
    Lottery lo = this.lotteryService.get(id);
    if (lo == null) {
      throw new BusinessException("LOTTERY/LOTTERY_IS_NULL", "数据不存在,注意缓存", null);
    }
    Game game = this.gameService.get(lo.getGameId());
    if (LotteryStatus.UNDO.getStatus() == lo.getStatus()) {
      throw new BusinessException("已撒消此期,不能进行此操作");
    }
    //OPEN 账号不受限制
    if(!"OPEN".equals(admin.getType())) {
      this.lotteryService.checkCanOperateTime(lo,game);
    }
    Date curDate = new Date();
    Date endDate = DateUtil.getDate(curDate,0,2,30,0); // 每天的2：30分前
    int d = DateUtil.daysBetween(lo.getOpenTime(),curDate);
    boolean isRest = (d == 0 || (d == 1 && DateUtil.dateCompareByYmdhms(curDate,endDate))); //第二天的2：30分前

    if (LotteryStatus.NOT_LOTTERY.getStatus() == lo.getStatus()) {
      Lottery lot = new Lottery();
      lot.setId(lo.getId());
      lot.setStatus(LotteryStatus.COUNT.getStatus());
      int count = this.lotteryService.updateStatus(lot, lo.getStatus()); // 先修改结果状态
      if(count > 0){
    	  this.gameWinManager.handle(game, lo);
      }else{
    	  throw new BusinessException("ERROR/LOTTERY", "期号状态不是未结算,不能进入结算操作", null);
      }
    } else if (LotteryStatus.LOTTERY.getStatus() == lo.getStatus() && isRest && "OPEN".equals(admin.getType())) {
      Lottery lot = new Lottery();
      lot.setId(lo.getId());
      lot.setStatus(LotteryStatus.REOPEN_LOTTERY.getStatus());
      int count = this.lotteryService.updateStatus(lot, lo.getStatus()); // 先修改结果状态
      if(count > 0){
    	  this.gameWinManager.rollback(game, lo);
      }else{
    	  throw new BusinessException("ERROR/LOTTERY", "期号状态不是已结算,不能进入从新结算操作", null);
      }
    } else {
      throw new BusinessException("ERROR/LOTTERY", LotteryStatus.getDesc(lo.getStatus()), null);
    }

  }

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  @ResponseBody
  public PageData<Lottery> query(Lottery lottery, PageBean pageBean) throws Exception {
    return lotteryService.query(lottery, pageBean);
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'添加开奖结果,彩票种类:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#lottery.gameId) + ',期号:' + #lottery.turn+'，开奖号码：'+ #lottery.openNum+'，开奖时间：'+T(com.cz.gameplat.log.LogUtil).DateToStr(#lottery.openTime)")
  public void add(Lottery lottery, @Session Admin admin) throws Exception {
    lottery.setAddTime(new Date());
    lottery.setStatus(LotteryStatus.NOT_LOTTERY.getStatus());
    lotteryService.save(lottery, admin);
  }

  @RequestMapping(value = "/edit", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'修改开奖结果,彩票种类:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#lottery.gameId)+',期号：'+#lottery.turn +'，开奖号码：'+ #lottery.openNum+'，开奖时间：'+T(com.cz.gameplat.log.LogUtil).DateToStr(#lottery.openTime)")
  public void edit(Lottery lottery, @Session Admin admin) throws Exception {
    // lottery.setAddTime(new Date());
    lottery.setUpdateTime(new Date());
    lotteryService.update(lottery, admin);
  }

  @RequestMapping(value = "/queryUnsettled/{gameId}", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UnsettledLottery> queryUnsettled(@PathVariable Integer gameId, String turnNum,
                                                   Date dateFrom, Date dateTo, PageBean pageBean) throws Exception {
    return lotteryService.queryUnsettled(gameId, turnNum, dateFrom, dateTo, pageBean);
  }

  @RequestMapping(value = "/regather/{gameId}", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'补采开奖结果,彩票种类:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)+',期号:'+#turnNums")
  public void regather(@PathVariable Integer gameId, String turnNums) throws Exception {
    lotteryService.regather(gameId, turnNums);
  }

  @RequestMapping(value = "/jsRegather/{gameId}", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'补采急速彩开奖结果,彩票种类:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)")
  public void jsRegather(@PathVariable Integer gameId, int nums) throws Exception {
    lotteryService.jsRegather(gameId, nums);
  }
  /**
   * 跟据code获取最近开奖结果
   * @param code
   * @param limit
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/queryByCode", method = RequestMethod.GET)
  @ResponseBody
  public List<GatherLottery> queryByCode(@NotNull(message = "{NoNull}") String code, String turns,
                                         Integer limit) throws Exception {

    return lotteryService.slaveQueryByCode(code, turns, limit);
  }
  /**
   * 获取极速彩下期开奖结果
   * @param gameId
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/getNextLottery", method = RequestMethod.GET)
  @ResponseBody
  public Lottery getNextLottery(Integer gameId) throws Exception {

	  return this.lotteryService.getNextLottery(gameId);
  }

  /**
   * 获取预设相关信息
   * @param gameId
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/getPresetLottery", method = RequestMethod.GET)
  @ResponseBody
  public Map getPresetLottery(Integer gameId, String turnNum,Integer count,Integer interval) throws Exception {
    return this.lotteryService.getPresetLottery(gameId,turnNum,count,interval);
  }

  @RequestMapping(value = "/queryPreset", method = RequestMethod.GET)
  @ResponseBody
  public PageData<PreLottery> queryPreset(Integer gameId, PageBean pageBean) throws Exception {
    return lotteryService.queryPreset(gameId,pageBean);
  }

  @RequestMapping(value = "/lookPresetOpenNum", method = RequestMethod.GET)
  @ResponseBody
  public PreLottery queryPreset(String vsCode, Integer gameId, String turnNum) throws Exception {
//    checkPresetAuth(vsCode);
    return lotteryService.getPresetLottery(gameId,turnNum);
  }

  @RequestMapping(value = "/deletePreset", method = RequestMethod.GET)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'删除自营彩种预设号码,彩票种类:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)+',期号:'+#turnNum")
  public void deletePreset(String vsCode,Integer gameId,String turnNum, @Session Admin admin) throws Exception {
//    checkePresetRole(admin,gameId);
//    checkPresetAuth(vsCode);
    lotteryService.deletePresetLottery(gameId,turnNum);
  }

  @RequestMapping(value = "/batchDeletePreset", method = RequestMethod.DELETE)
  @ResponseBody
  @Log(type = LogType.LOTTERY, content = "'批量删除自营彩种预设号码,彩票种类:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)+',期号:'+#turnNums")
  public  void batchDeletePreset(String vsCode,Integer gameId,String[] turnNums, @Session Admin admin) throws Exception {
//    checkePresetRole(admin,gameId);
//    checkPresetAuth(vsCode);
    if(turnNums.length == 0 ){
      throw  new BusinessException("参数异常，请重试！");
    }
    for(String turnNum:turnNums) {
      lotteryService.deletePresetLottery(gameId, turnNum);
    }
  }
    @RequestMapping(value = "/batchLookPresetOpenNum", method = RequestMethod.GET)
    @ResponseBody
    public List<PreLottery> batchLookPresetOpenNum(String vsCode, Integer gameId, String[] turnNums) throws Exception {
//        checkPresetAuth(vsCode);
        return lotteryService.getPresetLottery(gameId, turnNums);
    }


  @RequestMapping(value = "/addPreset", method = RequestMethod.POST)
  @ResponseBody
  public void addPreset(PreLottery lottery, String vsCode, @Session Admin admin, HttpServletRequest request) throws Exception {
//    checkPresetAuth(vsCode);
//    checkePresetRole(admin,lottery.getGameId());
    lotteryService.addPresetLottery(lottery,admin,request);
  }



  @RequestMapping(value = "/makeLuzhi", method = RequestMethod.GET)
  @ResponseBody
  public void makeLuzhiData() throws BusinessException {
      List<Game> gameList = gameService.queryAll();
      for (Game game : gameList) {
          lotteryService.makeLuzhiData(game);
      }
  }

//  private void checkePresetRole(Admin admin, Integer gameId) throws BusinessException {
//    if (!admin.getType().equalsIgnoreCase("ADMIN")) {
//      Role role = roleService.getRoleById(admin.getRoleId());
//      PresetAuth presetAuthServicePresetAuthByRoleId = presetAuthService.getPresetAuthByRoleId(role.getRoleId());
//      if (presetAuthServicePresetAuthByRoleId == null) {
//        throw new BusinessException("权限不足");
//      }
//      Optional<Integer> any = Arrays.stream(presetAuthServicePresetAuthByRoleId.getAuthGameIds().split(",")).map(s -> Integer.parseInt(s)).filter(integer -> integer == gameId.intValue()).findAny();
//      if (!any.isPresent()) {
//        throw new BusinessException("权限不足");
//      }else if (presetAuthServicePresetAuthByRoleId.getEditor() != 1){
//        throw new BusinessException("权限不足");
//      }
//    }
//  }
//
//  private void checkPresetAuth (String vsCode) throws BusinessException {
//    Config presetSecret = configService.getByKey("preset_lottery_auth_secret");
//    if (presetSecret == null) {
//      if (!GoogleAuth.authcode(vsCode, "U66JORZPITVPTGLQ")) {
//        throw new BusinessException("身份验证码错误");
//      }
//    }else{
//      if (!GoogleAuth.authcode(vsCode, presetSecret.getConfigValue())) {
//        throw new BusinessException("身份验证码错误");
//      }
//    }
//  }

}
