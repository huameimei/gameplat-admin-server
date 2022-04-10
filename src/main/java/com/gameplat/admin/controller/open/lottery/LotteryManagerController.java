package com.gameplat.admin.controller.open.lottery;

import com.gameplat.admin.service.NewLotteryForwardService;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/2/24 19:22 @Version 1.0
 */
@Slf4j
@ApiOperation("彩票管理")
@RestController
@RequestMapping("/api/admin/lottery/")
public class LotteryManagerController {

  @Autowired private NewLotteryForwardService serviceHandler;

  @ApiOperation("获取彩系和彩种列表")
  @PostMapping("/getLottTypeList")
  @PreAuthorize("hasAuthority('lottery:lottTypeList')")
  public Object getLottTypeList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取号码对应的12生肖")
  @PostMapping("/get12Sx")
  @PreAuthorize("hasAuthority('lottery:preset:get12Sx')")
  public Object get12Sx(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("开奖结果--列表")
  @PostMapping("/getOpenResult")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getOpenResult(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("开奖结果--编辑")
  @PostMapping("/editOpenNumber")
  @PreAuthorize("hasAuthority('lottery:open:editOpenNumber')")
  public Object editOpenNumber(HttpServletRequest request, String lottCode, Long expectNo) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("开奖结果--补彩列表")
  @PostMapping("/buCaiList")
  @PreAuthorize("hasAuthority('lottery:open:buCaiList')")
  public Object buCaiList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("开奖结果--补彩")
  @PostMapping("/buCai")
  @PreAuthorize("hasAuthority('lottery:open:buCai')")
  public Object buCai(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("开奖结果--结算")
  @PostMapping("/settlement")
  @PreAuthorize("hasAuthority('lottery:open:settlement')")
  public Object settlement(HttpServletRequest request, String lottCode, Long expectNo) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("新接入--预设开奖--彩系和彩种列表")
  @PostMapping("/getTypeLottList")
  @PreAuthorize("hasAuthority('lottery:open:getTypeLottList')")
  public Object getTypeLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--彩系和彩种列表")
  @PostMapping("/getPresetLottList")
  @PreAuthorize("hasAuthority('lottery:preset:getPresetLottList')")
  public Object getPresetLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("开奖预设开奖--批量添加预设号码")
  @PostMapping("/addPresetNumber")
  @PreAuthorize("hasAuthority('lottery:preset:addPresetNumber')")
  public Object addPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--列表")
  @PostMapping("/getPresetNumberList")
  @PreAuthorize("hasAuthority('lottery:preset:getOpenResult')")
  public Object getPresetNumberList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--批量删除/删除")
  @PostMapping("/deletePresetNumber")
  @PreAuthorize("hasAuthority('lottery:preset:deletePresetNumber')")
  public Object deletePresetNumber(HttpServletRequest request, String lottCode, String expectNos) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--批量查看/查看")
  @PostMapping("/showPresetNumber")
  @PreAuthorize("hasAuthority('lottery:preset:showPresetNumber')")
  public Object showPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--批量查看/查看--校验谷歌验证码")
  @PostMapping("/googleEvrify")
  @PreAuthorize("hasAuthority('lottery:preset:googleEvrify')")
  public Object googleEvrify(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("检查是否有编辑权限")
  @PostMapping("/checkEdit")
  @PreAuthorize("hasAuthority('lottery:checkEdit')")
  public Object checkEdit(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--可见彩种管理--权限列表")
  @PostMapping("/roleList")
  @PreAuthorize("hasAuthority('lottery:role:view')")
  public Object roleList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("预设开奖--可见彩种管理--保存/撤销权限/保存权限")
  @PostMapping("/roleUpdate")
  @PreAuthorize("hasAuthority('lottery:role:edit')")
  public Object roleUpdate(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取开奖时间")
  @PostMapping("/getOpenTime")
  @PreAuthorize("hasAuthority('lottery:open:getOpenTime')")
  public Object getOpenTime(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取彩系列表")
  @PostMapping("/getLottTypeLists")
  @PreAuthorize("hasAuthority('lottery:type:view')")
  public Object getLottTypeLists(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("彩系修改")
  @PostMapping("/updateLottType")
  @PreAuthorize("hasAuthority('lottery:type:edit')")
  public Object updateLottType(HttpServletRequest request, String groupCode) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取彩种列表")
  @PostMapping("/getLottList")
  @PreAuthorize("hasAuthority('lottery:kind:getLottList')")
  public Object getLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("修改彩种信息")
  @PostMapping("/updateLottInfo")
  @PreAuthorize("hasAuthority('lottery:kind:updateLottInfo')")
  public Object updateLottInfo(HttpServletRequest request, String lottCode) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("彩票设置--批量修改")
  @PostMapping("/batchUpdateLottInfo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object batchUpdateLottInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("投注额配置(信用/官方)--一级玩法列表")
  @PostMapping("/getOnePlayList")
  @PreAuthorize("hasAuthority('lottery:bet:getOnePlayList')")
  public Object getOnePlayList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getBetConfine")
  @PreAuthorize("hasAuthority('lottery:bet:getBetConfine')")
  @ApiOperation("投注额配置(信用/官方)--投注限制列表")
  public Object getBetConfine(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getBetRestriction")
  @ApiOperation("投注额配置(信用/官方)--获取单期最高投注额")
  @PreAuthorize("hasAuthority('lottery:bet:getBetRestriction')")
  public Object getBetRestriction(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBetRestriction")
  @ApiOperation("投注额配置(信用/官方)--修改单期最高投注额")
  @PreAuthorize("hasAuthority('lottery:bet:updateBetRestriction')")
  public Object updateBetRestriction(
      HttpServletRequest request, String lottCode, BigDecimal expectRestriction) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBetConfine")
  @ApiOperation("投注额配置(信用/官方)--保存修改")
  @PreAuthorize("hasAuthority('lottery:bet:updateBetConfine')")
  public Object updateBetConfine(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/lottConfineList")
  @ApiOperation("彩种配置--列表")
  @PreAuthorize("hasAuthority('lottery:play:lottConfineList')")
  public Object lottConfineList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateLottConfine")
  @ApiOperation("玩法配置--保存修改")
  @PreAuthorize("hasAuthority('lottery:play:updateLottConfine')")
  public Object updateLottConfine(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getPlayList")
  @PreAuthorize("hasAuthority('lottery:playList:view')")
  @ApiOperation("赔率设置(信用/官方)--获取玩法赔率列表")
  public Object getPlayList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("彩种玩法配置--一级玩法开关修改")
  @PostMapping("/onePlayUpdate")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object onePlayUpdate(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("赔率设置(信用/官方)--批量修改赔率")
  @PostMapping("/updatePlayOdds")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updatePlayOdds(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("赔率设置(信用/官方)--同步其它相同彩种相同玩法的赔率")
  @PostMapping("/syncUpdatePlayOdds")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object syncUpdatePlayOdds(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("赔率设置(信用/官方)--同彩种同步相同玩法的赔率")
  @PostMapping("/tSyncUpdatePlayOdds")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object tSyncUpdatePlayOdds(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("未结算注单--列表")
  @PostMapping("/noSettlementBet")
  @PreAuthorize("hasAuthority('lottery:noSettlementBet:view')")
  public Object noSettlementBet(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("未结算注单--删除")
  @PostMapping("/deleteNoSettlementBet")
  @PreAuthorize("hasAuthority('lottery:noSettlementBet:remove')")
  public Object deleteNoSettlementBet(HttpServletRequest request, String infoOrderNo) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("未结算注单--批量注销记录/注销")
  @PostMapping("/updateNoSettlementBet")
  @PreAuthorize("hasAuthority('lottery:noSettlementBet:update')")
  public Object updateNoSettlementBet(HttpServletRequest request, String infoOrderNos) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取一级玩法和二级玩法 ")
  @PostMapping("/getOnePlay")
  @PreAuthorize("hasAuthority('lottery:play:getOnePlay')")
  public Object getOnePlay(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("即时注单-列表数据")
  @PostMapping("/geFourPlay")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object geFourPlay(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("即时注单")
  @PostMapping("/betList")
  @PreAuthorize("hasAuthority('lottery:bet:betList')")
  public Object betList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取当前期号信息")
  @PostMapping("/getExpectNo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getExpectNo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("即时注单--玩法和彩种统计")
  @PostMapping("/playLottCount")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object playLottCount(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("即时注单汇总")
  @PostMapping("/betSum")
  @PreAuthorize("hasAuthority('lottery:bet:betSum')")
  public Object betSum(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("注单记录")
  @PostMapping("/betRecord")
  @PreAuthorize("hasAuthority('lottery:bet:record')")
  public Object betRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @SneakyThrows
  @ApiOperation("导出注单记录")
  @PostMapping("/exportBetRecord")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object exportBetRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("注单分析--列表")
  @PostMapping("/betAnalysis")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysis(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("注单分析--详情")
  @PostMapping("/betAnalysisDetails")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysisDetails(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("注单分析--详情--详情")
  @PostMapping("/betAnalysisBetDetails")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysisBetDetails(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("注单分析--对打分析")
  @PostMapping("/betAnalysisVsAnalysis")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object betAnalysisVsAnalysis(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("彩票输赢记录")
  @PostMapping("/lottWinRecord")
  @PreAuthorize("hasAuthority('lottery:winRecord:view')")
  public Object lottWinRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("杀率设置--获取杀率信息")
  @PostMapping("/getKillInfo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getKillInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("杀率设置--更新杀率信息")
  @PostMapping("/updateKillInfo")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateKillInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("批量获取随机号码")
  @PostMapping("/getRandomNumber")
  @PreAuthorize("hasAuthority('lottery:racing:getRandomNumber')")
  public Object getRandomNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取对打注单列表")
  @PostMapping("/getSparringOrderList")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getSparringOrderList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("获取对打注单详情")
  @GetMapping("/getSparringOrderDetails")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getSparringOrderDetails(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/playRuleExplain")
  @PreAuthorize("hasAuthority('lottery:play:playRuleExplain')")
  @ApiOperation("玩法说明-列表数据")
  public Object playRuleExplain(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getBannerList")
  @ApiOperation("彩票banner配置--列表数据")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getBannerList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBanner")
  @ApiOperation("彩票banner配置--修改数据/删除")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateBanner(HttpServletRequest request, Integer id) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/addBanner")
  @ApiOperation("彩票banner配置--添加数据")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object addBanner(HttpServletRequest request, String lang) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getNotice")
  @ApiOperation("公告配置-列表")
  @PreAuthorize("hasAuthority('lottery:notice:view')")
  public Object getNotice(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/addNotice")
  @ApiOperation("公告配置-新增")
  @PreAuthorize("hasAuthority('lottery:notice:add')")
  public Object addNotice(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateNotice")
  @ApiOperation("公告配置-编辑")
  @PreAuthorize("hasAuthority('lottery:notice:edit')")
  public Object updateNotice(HttpServletRequest request, Integer id) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/deleteNotice")
  @ApiOperation("公告配置-删除")
  @PreAuthorize("hasAuthority('lottery:notice:remove')")
  public Object deleteNotice(HttpServletRequest request, String id) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getLhcTmb")
  @ApiOperation("特码B-查询反水比例")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getLhcTmb(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateLhcTmb")
  @ApiOperation("特码B-反水比例修改")
  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateLhcTmb(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getToDayPayoutSet")
  @ApiOperation("今日派奖设置-设置")
  @PreAuthorize("hasAuthority('lottery:distribute:getToDayPayoutSet')")
  public Object getToDayPayoutSet(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/toDayPayoutSet")
  @ApiOperation("今日派奖设置-设置")
  @PreAuthorize("hasAuthority('lottery:distribute:toDayPayoutSet')")
  public Object toDayPayoutSet(
      HttpServletRequest request,
      BigDecimal initNumber,
      BigDecimal initMoney,
      BigDecimal addNumber,
      BigDecimal addMoney) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getFundRecord")
  @ApiOperation("资金流水记录")
//  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getFundRecord(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/changesType")
  @ApiOperation("资金类型")
//  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object changesType(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/getUserInfo")
  @ApiOperation("获取用户信息")
//  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object getUserInfo(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBlack")
  @ApiOperation("更新用户官彩黑名单")
//  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateBlack(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateBetLimit")
  @ApiOperation("更新用户投注限额")
//  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateBetLimit(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @PostMapping("/updateRebate")
  @ApiOperation("更新用户反水等级")
//  @PreAuthorize("hasAuthority('lottery:open:getOpenResult')")
  public Object updateRebate(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("竞速彩预设开奖--彩系和彩种列表")
  @PostMapping("getJscPresetLottList")
  @PreAuthorize("hasAuthority('lottery:racing:getJscPresetLottList')")
  public Object getJscPresetLottList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("竞速彩预设开奖--列表")
  @PostMapping("getJscPresetNumberList")
  @PreAuthorize("hasAuthority('lottery:racing:getJscPresetNumberList')")
  public Object getJscPresetNumberList(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }


  @ApiOperation("竞速彩预设开奖--批量删除-删除")
  @PostMapping("deleteJscPresetNumber")
  @PreAuthorize("hasAuthority('lottery:racing:deleteJscPresetNumber')")
  public Object deleteJscPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("竞速彩开奖预设--批量添加预设号码")
  @PostMapping("addJscPresetNumber")
  @PreAuthorize("hasAuthority('lottery:racing:addJscPresetNumber')")
  public Object addJscPresetNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }

  @ApiOperation("批量竞速彩获取随机号码")
  @PostMapping("getJscRandomNumber")
  @PreAuthorize("hasAuthority('lottery:racing:getJscRandomNumber')")
  public Object getJscRandomNumber(HttpServletRequest request) {
    return serviceHandler.serviceHandler(request);
  }
}
