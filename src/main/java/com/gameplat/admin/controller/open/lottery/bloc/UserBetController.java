package com.gameplat.admin.controller.open.lottery.bloc;

import com.cz.framework.CollectionUtils;
import com.cz.framework.DateUtil;
import com.cz.framework.LogUtil;
import com.cz.framework.StringUtil;
import com.cz.framework.bean.PageBean;
import com.cz.framework.bean.PageData;
import com.cz.framework.exception.BusinessException;
import com.cz.framework.exception.ParaException;
import com.cz.framework.redis.lock.Lock;
import com.cz.gameplat.annotation.Session;
import com.cz.gameplat.core.export.Export;
import com.cz.gameplat.core.export.ExportCache;
import com.cz.gameplat.core.export.Status;
import com.cz.gameplat.core.export.Task;
import com.cz.gameplat.game.bean.UserPlayLotteryTypes;
import com.cz.gameplat.game.entity.*;
import com.cz.gameplat.game.enums.UserBetStatus;
import com.cz.gameplat.game.service.BetService1;
import com.cz.gameplat.game.service.GameService1;
import com.cz.gameplat.game.service.UserBetReportService;
import com.cz.gameplat.game.service.UserBetService;
import com.cz.gameplat.log.Log;
import com.cz.gameplat.log.LogType;
import com.cz.gameplat.lottery.service.LotteryService;
import com.cz.gameplat.sys.bean.SystemConfig;
import com.cz.gameplat.sys.cache.GenericDataCache;
import com.cz.gameplat.sys.entity.Admin;
import com.cz.gameplat.sys.enums.SysUserTypes;
import com.cz.gameplat.user.bean.UserInfoVO;
import com.cz.gameplat.user.service.UserService;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/bet")
public class UserBetController {

  @Autowired
  private BetService1 betService;
  @Autowired
  private UserBetService userBetService;
  @Autowired
  private GameService1 gameService;
  @Autowired
  private UserBetReportService userBetReportService;
  @Autowired
  private LotteryService lotteryService;
  @Autowired
  private SystemConfig systemConfig;
  @Autowired
  private UserService userService;
  @Autowired
  ExportCache exportCache;

  /**
   * 查询注单
   */
  @RequestMapping(value = "/getBets", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UserBetRep> getBets(UserBetQueryReq params, PageBean pageBean) throws BusinessException {
    if(StringUtil.isNotBlank(params.getSuperName())){
      UserInfoVO userInfoVO = userService.get(params.getSuperName());
      if(userInfoVO!=null && userInfoVO.getUserInfo()!=null){
        params.setSuperName(null);
        params.setSuperPath(userInfoVO.getUserInfo().getSuperPath());
      }else {
        throw new BusinessException("代理账号不存在!");
      }
    }
    return userBetService.slaveQueryNonShardUserBetPage(params, pageBean);
  }

  /**
   * 查询六合彩期数
   */
  @RequestMapping(value = "/getLhcTurnNum")
  @ResponseBody
  public List<String> queryLhcTurnNum(UserBetQueryReq po) throws BusinessException {
    if(StringUtil.isNotBlank(po.getSuperName())){
      UserInfoVO userInfoVO = userService.get(po.getSuperName());
      if(userInfoVO!=null && userInfoVO.getUserInfo()!=null){
        po.setSuperPath(userInfoVO.getUserInfo().getSuperPath());
      }else {
        throw new BusinessException("代理账号不存在!");
      }
    }
    return userBetService.slaveQueryLhcTurnNum(po);
  }

  @RequestMapping(value = "/getLhcBetsHis")
  @ResponseBody
  public PageData<UserBetRep> getBetsHis(UserBetQueryReq po, PageBean pageBean) throws BusinessException {
    if(StringUtil.isNotBlank(po.getSuperName())){
      UserInfoVO userInfoVO = userService.get(po.getSuperName());
      if(userInfoVO!=null && userInfoVO.getUserInfo()!=null){
        po.setSuperPath(userInfoVO.getUserInfo().getSuperPath());
      }else {
        throw new BusinessException("代理账号不存在!");
      }
    }
    return userBetService.slaveQueryBetsHisPage(po, pageBean);
  }

  /**
   * 导出六合彩历史注单
   */
  @RequestMapping(value = "/exportLhc", method = RequestMethod.GET)
  @Log(type = LogType.EXPORT_HY,content = "'导出七日历史注单'")
  @ResponseBody
  public Task exportLhc(UserBetQueryReq po, @Session Admin admin, HttpServletResponse response) throws BusinessException {
    if(StringUtil.isNotBlank(po.getSuperName())){
      UserInfoVO userInfoVO = userService.get(po.getSuperName());
      if(userInfoVO!=null && userInfoVO.getUserInfo()!=null){
        po.setSuperPath(userInfoVO.getUserInfo().getSuperPath());
      }else {
        throw new BusinessException("代理账号不存在!");
      }
    }
    Task<UserBetQueryReq> task = new Task<>(po, admin.getId(), "七日历史注单记录");
    String taskId = exportCache.add(task);
    Export.getExecutor().execute(() -> {
      long startTime = System.currentTimeMillis();
      LogUtil.info("开始导七日历史注单款记录");
      OutputStream outStream = null;

      try {
        Task<UserBetQueryReq> todoTask = exportCache.get(taskId);
        //获取总记录数
        int count = userBetService.slaveQueryLhcBetsHisCount(po);
        LogUtil.info(String.format("导出七日历史注单记录，数据大小：%d条数据, 导出条件：%s", count, po.toString()));
        if (count == 0) {
          exportCache.update(todoTask.setStatus(Status.NoData));
        } else if (count > 600000) {
          exportCache.update(todoTask.setStatus(Status.DataToBig));
        } else {
          List<Game> gameList = gameService.getGameList(null);
          if (CollectionUtils.isNotEmpty(gameList)) {
            Map<String, String> gameInfo = new HashMap<>();
            gameList.forEach(game -> {
              //PlayType: 1仅官方，2仅信用，3官方和信用
              if (Objects.equals(game.getPlayType(), 1) || Objects.equals(game.getPlayType(), 3)) {
                gameInfo.put(game.getId() + "0", game.getName() + "[官]");
              }
              if (Objects.equals(game.getPlayType(), 2) || Objects.equals(game.getPlayType(), 3)) {
                gameInfo.put(game.getId() + "1", game.getName() + "[信]");
              }
            });
            GenericDataCache.setData(GenericDataCache.lottery_game_info_map, gameInfo);
          }
          //分页查询每次只查50000条数据，
          int maxCache = 50000;
          //查询次数
          int findNum = 0;
          if (count % maxCache == 0) {
            findNum = count / maxCache;
          } else {
            findNum = count / maxCache + 1;
          }
          Map<String, Integer> map = new HashMap<>();
          // 获取模板、sheet页
          SXSSFWorkbook workBook = new SXSSFWorkbook(1000);
          Sheet sheet = workBook.createSheet(todoTask.getName());
          //定义行坐标
          int rowIndex = 0;
          //创建表头行
          Row xRow = sheet.createRow(rowIndex++);
          //分页导出数据
          for (int i = 0; i < findNum; i++) {
            long pageStart = System.currentTimeMillis();
            map.put("start", i * maxCache);
            map.put("end", maxCache);

            List<LhcBetRep> results = userBetService.slaveQueryLhcBetsHisByPage(po, map);
            rowIndex = Export.exportPoiByPage(workBook, rowIndex, sheet, xRow, results, LhcBetRep.class);
            long pageEnd = System.currentTimeMillis();
            LogUtil.info(String.format("导出第%d页七日历史注单记录成功，耗时%d秒", i + 1, (pageEnd - pageStart) / 1000));
          }
          //获得输出流
          outStream = new FileOutputStream(todoTask.getExportFilePath());
          // 将内容写入Excel
          workBook.write(outStream);
          exportCache.update(todoTask.setStatus(Status.Success));
        }
      } catch (Exception ep) {
        exportCache.update(exportCache.get(taskId).setStatus(Status.Fail));
        LogUtil.error(ep.toString());
      } finally {
        GenericDataCache.removeGenericData(GenericDataCache.lottery_game_info_map);
        long endTime = System.currentTimeMillis();
        LogUtil.info(String.format("导出七日历史注单记录成功，总耗时；%d秒", (endTime - startTime) / 1000));
        try {
          if (outStream != null) {
            outStream.flush();
            outStream.close();
          }
        } catch (IOException e) {
          LogUtil.error(e.toString());
        }
      }
    });
    return task;
  }


  /**
   * 查询用户对应的注单
   */
  @RequestMapping(value = "/queryUserBets", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UserBetRep> queryUserBets(UserBetQueryReq params, PageBean pageBean)
      throws Exception {
    return userBetService.slaveQueryUserBetPage(params,false, pageBean);
  }

  /**
   * 查询用户对应的注单
   */
  @RequestMapping(value = "/queryUserBetsHis", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UserBetRep> queryUserBetsHis(UserBetQueryReq params, PageBean pageBean)
          throws Exception {
    return userBetService.slaveAnalysisUserBetPage(params, pageBean);
  }
  /**
   * 用户注单分析
   */
  @RequestMapping(value = "/queryUserAnalysis", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UserBetAnalysis> queryUserAnalysis(
      @RequestParam(required = false) Integer gameId,
      @RequestParam(required = false) String account,
      @RequestParam(required = false) String turnNum,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(defaultValue = "winRate") String orderBy,
      @RequestParam(defaultValue = "game_id") String groupBy,
      PageBean pageBean) throws Exception {
    SimpleDateFormat sdf = DateUtil.simpleDateFormat(DateUtil.YYYY_MM_DD);
    Date timeFrom = DateUtil
        .getDateStart(StringUtils.isBlank(startDate) ? new Date() : sdf.parse(startDate));
    Date timeTo = DateUtil
        .getDateEnd(StringUtils.isBlank(endDate) ? new Date() : sdf.parse(endDate));
    return userBetService.slaveAnalysisUserBet(gameId, account,turnNum, timeFrom, timeTo, groupBy, orderBy, pageBean);
  }

  /**
   * 用户注单对打分析
   */
  @RequestMapping(value = "/queryUserDuiDaAnalysis", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UserBetAnalysis> queryUserDuiDaAnalysis(
      @RequestParam(required = false) Integer gameId,
      @RequestParam(required = false) String account,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String cateCode,
      @RequestParam(required = false) String turnNum,
      @RequestParam(defaultValue = "winRate") String orderBy,
      @RequestParam(defaultValue = "game_id") String groupBy,
      PageBean pageBean) throws Exception {
    SimpleDateFormat sdf = DateUtil.simpleDateFormat(DateUtil.YYYY_MM_DD);
    Date timeFrom = DateUtil
        .getDateStart(StringUtils.isBlank(startDate) ? new Date() : sdf.parse(startDate));
    Date timeTo = DateUtil
        .getDateEnd(StringUtils.isBlank(endDate) ? new Date() : sdf.parse(endDate));
    return userBetService
        .slaveDuidaAnalysisUserBet(gameId, cateCode, turnNum, timeFrom, timeTo, groupBy,
            orderBy, pageBean);
  }

  /**
   *  导出
   */
  @RequestMapping(value = "/export", method = RequestMethod.GET)
  @Log(type = LogType.EXPORT_HY,content = "'导出注单记录，导出时间区间：'+T(com.cz.gameplat.log.LogUtil).logExportTime(#param.getStartDate(),#param.getEndDate())")
  public void export(UserBetQueryReq param, HttpServletResponse response) throws Exception {
    List<UserBetRep> results = userBetService.slaveQueryUserBetList(param);
    String title = "注单记录";
    String fileName = URLEncoder
        .encode((title + DateUtil.getCurrentTime() + ".xlsx"), CharEncoding.UTF_8);
    response.setContentType("application/msexcel");
    response.setHeader("Content-disposition", "attachment;filename=" + fileName);

    //缓存gameList信息供bean中显示名称使用
    GenericDataCache.cacheGenericData(GenericDataCache.Key_getGameList, gameService, "getGameList",
        Integer.class, null);
    Export.exportPoi(results, UserBetRep.class, response.getOutputStream(), null, title);
    GenericDataCache.removeGenericData(GenericDataCache.Key_getGameList);
  }

  @RequestMapping(value = "/getTurnNums", method = RequestMethod.GET)
  @ResponseBody
  public List<String> getTurnNums(String aliasId, String cateCode, Integer status, String account)
      throws Exception {
    if (StringUtils.isEmpty(aliasId)) {
      throw new ParaException("请求参数异常");
    }
    return userBetService.slaveQueryTurnNums(aliasId, cateCode, status, account);
  }

  /**
   * 获取下注内容的玩法信息
   */
  @RequestMapping(value = "/getBetPlayCate", method = RequestMethod.GET)
  @ResponseBody
  public List<Map<String, String>> getBetPlayCate(String aliasId, Integer status, String account) {
    return this.userBetService.slaveBetPlayCate(aliasId, status, account);

  }

  /**
   * 删除注单
   */
  @RequestMapping(value = "/delBet", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.USER_BET, content = "'删除注单,订单号:'+#orderNo+ '并没收本金:'+#betMoney ")
  public void delBet(String orderNo, Integer gameId,String betMoney) throws Exception {
    if (StringUtil.isBlank(orderNo)) {
      throw new ParaException("请求参数异常");
    }
    userBetService.changeCurrDayBetStaus(gameId, orderNo, UserBetStatus.DELETE);
  }

  /**
   * 注销注单
   */
  @RequestMapping(value = "/cancelBet", method = RequestMethod.POST)
  @ResponseBody
  @Lock(value = "admin_cancel_bet")
  @Log(type = LogType.USER_BET, content = "'注销注单,订单号:'+#orderNo")
  public void cancelBet(String orderNo) throws Exception {
    if (StringUtil.isBlank(orderNo)) {
      throw new ParaException("请求参数异常");
    }
    betService.revocation(orderNo);
  }

  /**
   * 修改注单内容
   */
  @RequestMapping(value = "/updateBet", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.USER_BET, content = "'修改注单,订单号:'+#po.orderNo")
  public void updateBet(UserBetRep po, @Session Admin admin) throws Exception {
//        if (po.getGameId() == null || po.getGameId() <= 0) {
//            throw new ParaException("请求参数异常");
//        }
//        userBetService.updateCurrDayBet(po, admin.getAccount());
    throw new ParaException("不允许修改注单");
  }

  /**
   * 查找修改记录
   */
  @RequestMapping(value = "/queryModifyRecord", method = RequestMethod.GET)
  @ResponseBody
  public List<UserBetModifyRecord> queryModifyRecord(String orderNo, Integer gameId)
      throws Exception {
    if (StringUtil.isBlank(orderNo) || gameId == null || gameId.intValue() <= 0) {
      throw new ParaException("请求参数异常");
    }
    return userBetService.queryModifyRecord(orderNo, gameId);
  }

  /**
   * 撤销整期
   */
  @RequestMapping(value = "/revocate", method = RequestMethod.POST)
  @ResponseBody
  @Log(type = LogType.USER_BET, content = "'撤销整期,期号:'+#turnNum+',游戏:'+T(com.cz.gameplat.log.LogUtil).lotteryTypeTranslate(#gameId)")
  public void revocateBets(Integer gameId, Integer model, String turnNum, @Session Admin admin,
      HttpServletRequest request) throws BusinessException {
    if (gameId == null || gameId.intValue() <= 0) {
      throw new BusinessException("请求参数异常");
    }
    if (gameId == 70) {
      String url = request.getServerName();
      if (StringUtils.isNotBlank(admin.getType())) {
        // && admin.getType().equals("OPEN") && url.contains(systemConfig.getDomain())
        //如果是open类型的账号，并且是规定的域名，可以注销整期六合彩
      } else {
        throw new BusinessException("六合彩不允许注销整期");
      }
    }
    betService.revocationByTurnNum(gameId, model, turnNum);
  }

  /**
   * 查询及时注单某天下注明细（正式会员）
   */
  @RequestMapping(value = "/queryImmediateBets", method = RequestMethod.GET)
  @ResponseBody
  public PageData<UserBetRep> queryImmediateBets(UserBetQueryReq params, PageBean pageBean)
      throws BusinessException {
    params.setUserId(null);
    //不清楚为什么设为NULL
//    params.setAccount(null);
    if(params.getSearchType().equals("DL")){
      UserInfoVO userInfoVO = userService.get(params.getAccount());
      if(userInfoVO==null){
        return new PageData<>();
      }
      params.setAccount(null);
      params.setSuperPath(userInfoVO.getUserInfo().getSuperPath());
    }
    params.setUserType(SysUserTypes.HY.getCode());
    Integer gameId = params.getGameId();
    String turnNum = params.getTurnNum();
    // 非当前期且主表无数据的情况下，查询历史表
    if (!StringUtils.equals(turnNum, lotteryService.getCurLottery(gameId).getTurnNum())
        && userBetService.slaveCountByGameIdAndTurnNum(gameId, turnNum) == 0) {
      return userBetService.slaveQueryUserBetHisPage(params, pageBean);
    }
    return userBetService.slaveQueryNonShardUserBetPage(params, pageBean);
  }

  @RequestMapping(value = "/queryLotteryTypes", method = RequestMethod.GET)
  @ResponseBody
  public List<UserPlayLotteryTypes> queryLotteryTypes(Long userId) {
    return this.userBetReportService.queryReportTypesByUserId(userId);
  }

}

